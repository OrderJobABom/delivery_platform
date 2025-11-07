package com.example.orderjobabom.store.domain.dto;

import com.example.orderjobabom.store.domain.StoreId;
import lombok.Builder;

@Builder
public record StoreCreateDto(
        StoreId storeId
) {}