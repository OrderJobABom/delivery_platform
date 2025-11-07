package com.example.orderjobabom.payment.presentation.dto;

import com.example.orderjobabom.payment.domain.Payment;
import lombok.Builder;

import java.util.UUID;

@Builder
public record PaymentResponse(UUID orderId, String paymentKey, int amount, String status, String approvedAt) {
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getOrderId(),
                payment.getPaymentKey(),
                payment.getAmount(),
                payment.getStatus().name(),
                payment.getApprovedAt() != null ? payment.getApprovedAt().toString() : null
        );
    }
}