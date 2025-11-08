package com.example.orderjobabom.payment.presentation.dto;

import java.util.UUID;

public record ConfirmPaymentRequest(String paymentKey, UUID orderId, int amount) {}