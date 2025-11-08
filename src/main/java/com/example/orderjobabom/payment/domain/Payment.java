package com.example.orderjobabom.payment.domain;

import com.example.orderjobabom.order.domain.OrderId;
import com.example.orderjobabom.user.domain.UserId;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "p_payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Payment {

    @EmbeddedId
    private PaymentId id;

    @Column(nullable = false)
    private UUID orderId;

    @Column(nullable = false)
    private UUID userId;

    private String paymentKey;
    private String transactionKey;

    @Column(nullable = false)
    private int amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;

    /**
     * Toss 결제 승인 후 Payment 생성 팩토리
     */
    public static Payment approve(OrderId orderId, UserId userId, int amount,
                                  String paymentKey, String transactionKey) {

        return Payment.builder()
                .id(PaymentId.create())
                .orderId(orderId.getId())
                .userId(userId.getId())
                .amount(amount)
                .paymentKey(paymentKey)
                .transactionKey(transactionKey)
                .status(PaymentStatus.APPROVED)
                .createdAt(LocalDateTime.now())
                .approvedAt(LocalDateTime.now())
                .build();
    }

    /**
     * 결제 취소 처리
     */
    public void cancel() {
        this.status = PaymentStatus.CANCELLED;
    }
}
