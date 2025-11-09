package com.example.orderjobabom.order.presentation.dto.requestDTO;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
public record OrderItemRequestDTO(
        UUID itemId,
        int price,
        int count) {
}
