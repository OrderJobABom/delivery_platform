package com.example.orderjobabom.payment.application.service;

import com.example.orderjobabom.order.domain.OrderId;
import com.example.orderjobabom.payment.domain.Payment;
import com.example.orderjobabom.payment.domain.PaymentRepository;
import com.example.orderjobabom.payment.infrastructure.api.TossPaymentClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentCancelService {

    private final PaymentRepository paymentRepository;
    private final TossPaymentClient tossPaymentClient;

    /**
     * 결제 환불 (Toss API 호출 포함)
     *
     * @param orderId 주문 ID
     * @param jwt     로그인한 사용자 (Keycloak)
     * @param reason  취소 사유
     */
    public void cancel(OrderId orderId, Jwt jwt, String reason) {
        Payment payment = paymentRepository.findByOrderId(orderId.getId())
                .orElseThrow(() -> new IllegalArgumentException("결제 내역을 찾을 수 없습니다."));

        // Keycloak에서 roles 가져오기
        List<String> roles = jwt.getClaimAsMap("realm_access") != null
                ? (List<String>) ((List<?>) ((java.util.Map<?, ?>) jwt.getClaimAsMap("realm_access")).get("roles"))
                : List.of();

        boolean isManager = roles.contains("ROLE_MANAGER") || roles.contains("ROLE_MASTER");

        // 일반 사용자(USER)는 결제 승인 후 5분 이내만 환불 가능
        if (!isManager) {
            if (payment.getApprovedAt() == null)
                throw new IllegalStateException("아직 승인되지 않은 결제입니다.");

            Duration elapsed = Duration.between(payment.getApprovedAt(), LocalDateTime.now());
            if (elapsed.toMinutes() > 5) {
                throw new IllegalStateException("결제 후 5분이 지나 환불이 불가능합니다.");
            }
        }

        // Toss API 호출 (실제 환불 요청)
        tossPaymentClient.cancelPayment(payment.getPaymentKey(), reason);

        // 결제 상태 변경
        payment.cancel();
        paymentRepository.save(payment);
    }
}
