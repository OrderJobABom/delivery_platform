package com.example.orderjobabom.payment.infrastructure.dto;

import lombok.Getter;

@Getter
public class TossPaymentResponse {
    private String paymentKey;
    private String orderId;
    private String status;
    private String lastTransactionKey;
    private int totalAmount;
    private String approvedAt;
}