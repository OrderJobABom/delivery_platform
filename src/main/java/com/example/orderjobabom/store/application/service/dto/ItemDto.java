package com.example.orderjobabom.store.application.service.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record ItemDto(
        UUID id,
        int price,
        String name,
        boolean outOfStock,
        List<ItemOptionDto> options,
        LocalDateTime createdAt // 상품 등록일시
)  {}
