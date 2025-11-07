package com.example.orderjobabom.payment.presentation.controller;

import com.example.orderjobabom.order.domain.OrderId;
import com.example.orderjobabom.order.domain.OrderRepository;
import com.example.orderjobabom.payment.application.service.PaymentCancelService;
import com.example.orderjobabom.payment.application.service.PaymentService;
import com.example.orderjobabom.payment.domain.Payment;
import com.example.orderjobabom.payment.infrastructure.redis.PaymentTempService;
import com.example.orderjobabom.payment.presentation.dto.CancelPaymentRequest;
import com.example.orderjobabom.payment.presentation.dto.ConfirmPaymentRequest;
import com.example.orderjobabom.payment.presentation.dto.PaymentResponse;
import com.example.orderjobabom.payment.presentation.dto.SaveAmountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentCancelService paymentCancelService;
    private final PaymentTempService paymentTempService;
    private final OrderRepository orderRepository;

    /** 결제 금액 임시 저장 (검증용) */
    @PostMapping("/saveAmount")
    public ResponseEntity<?> tempSave(@RequestBody SaveAmountRequest req) {
        if (!orderRepository.existsById(OrderId.of(req.orderId()))) {
            return ResponseEntity.badRequest().body("존재하지 않는 주문입니다.");
        }

        paymentTempService.saveAmount(req.orderId(), req.amount());
        return ResponseEntity.ok("결제 금액 임시 저장 성공 (Redis)");
    }

    /** 결제 승인 (금액 검증 포함) */
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody ConfirmPaymentRequest req) {
        // 1Redis 임시 금액 조회
        Integer savedAmount = paymentTempService.getAmount(req.orderId());
        if (savedAmount == null) {
            return ResponseEntity.badRequest().body("Redis에 결제 금액 정보가 없습니다.");
        }

        // 주문 DB에서 orderId로 조회
        var order = orderRepository.findById(OrderId.of(req.orderId()))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        // 주문 금액 일치 검증
        if (!Integer.valueOf(order.getOrderTotalPrice().getValue()).equals(savedAmount)) {
            return ResponseEntity.badRequest().body("결제 금액이 주문 금액과 일치하지 않습니다.");
        }

        // Toss API를 이용한 결제 승인
        var tossResponse = paymentService.requestTossApproval(req);

        //  결제 성공 시 DB에 Payment 엔티티 생성 후 저장
        Payment payment = paymentService.createApprovedPayment(
                OrderId.of(req.orderId()),
                order.getOrderer().getId(),
                savedAmount,
                tossResponse.getPaymentKey(),
                tossResponse.getLastTransactionKey()
        );

        // Redis 임시데이터 삭제
        paymentTempService.delete(req.orderId());

        // 주문 상태 업데이트 (결제 완료)
        //order.updateStatus(OrderStatus.PAYMENT_CONFIRM);

        return ResponseEntity.ok(PaymentResponse.from(payment));
    }


    /** 결제 취소 */
    @PostMapping("/cancel")
    @PreAuthorize("hasAnyRole('USER','MANAGER','MASTER')")
    public ResponseEntity<?> cancelPayment(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody CancelPaymentRequest req
    ) {
        try {
            paymentCancelService.cancel(OrderId.of(req.orderId()), jwt, req.cancelReason());
            return ResponseEntity.ok("결제 환불이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("환불 실패: " + e.getMessage());
        }
    }
}