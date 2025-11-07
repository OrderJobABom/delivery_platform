package com.example.orderjobabom.payment.presentation.dto;

import java.util.UUID;

public record SaveAmountRequest(UUID orderId, int amount) {}