package com.example.orderjobabom.payment.presentation.dto;


import java.util.UUID;

public record CancelPaymentRequest(UUID orderId, String paymentKey, String cancelReason) {}