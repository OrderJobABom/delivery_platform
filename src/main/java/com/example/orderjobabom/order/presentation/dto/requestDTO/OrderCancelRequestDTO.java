package com.example.orderjobabom.order.presentation.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record OrderCancelRequestDTO(
        @NotBlank String ordererName

) {
}
