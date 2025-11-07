package com.example.orderjobabom.payment.application.service;

import com.example.orderjobabom.order.domain.OrderId;
import com.example.orderjobabom.payment.domain.Payment;
import com.example.orderjobabom.payment.domain.PaymentRepository;
import com.example.orderjobabom.payment.infrastructure.api.TossPaymentClient;
import com.example.orderjobabom.payment.infrastructure.dto.TossPaymentResponse;
import com.example.orderjobabom.payment.presentation.dto.ConfirmPaymentRequest;
import com.example.orderjobabom.user.domain.UserId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final TossPaymentClient tossPaymentClient;

    /**
     * Toss 결제 승인 요청 (Toss API 호출)
     */
    public TossPaymentResponse requestTossApproval(ConfirmPaymentRequest req) {
        try {
            return tossPaymentClient.confirmPayment(
                    req.paymentKey(),
                    req.orderId().toString(),
                    req.amount()
            );
        } catch (Exception e) {
            tossPaymentClient.cancelPayment(req.paymentKey(), "결제 승인 중 오류 발생 → 자동 취소");
            throw new IllegalStateException("Toss 결제 승인 실패", e);
        }
    }

    /**
     * Toss 결제 승인 후 DB에 Payment 생성 및 저장
     */
    public Payment createApprovedPayment(OrderId orderId, UserId userId, int amount,
                                         String paymentKey, String transactionKey) {
        Payment payment = Payment.approve(orderId, userId, amount, paymentKey, transactionKey);
        return paymentRepository.save(payment);
    }
}

