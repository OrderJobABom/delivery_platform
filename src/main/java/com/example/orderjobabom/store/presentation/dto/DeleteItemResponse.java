package com.example.orderjobabom.store.presentation.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record DeleteItemResponse(
        UUID id,
        String name
) {
}
