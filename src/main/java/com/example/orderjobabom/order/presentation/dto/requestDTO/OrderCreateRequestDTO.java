package com.example.orderjobabom.order.presentation.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;

@Builder
public record OrderCreateRequestDTO(
        @NotBlank List<OrderItemRequestDTO> orderItemRequestList,
        @NotBlank String address,
        @NotBlank String memo,
        int payPrice,
        String username
        ) {
}
