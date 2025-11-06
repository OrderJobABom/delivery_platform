package com.example.orderjobabom.store.presentation.dto;

import com.example.orderjobabom.menu.domain.ItemStatus;
import com.example.orderjobabom.store.application.service.dto.ItemOptionDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record UpdateItemResponse(
        UUID storeId,
        UUID itemId,
        int price,
        String name,
        ItemStatus status,
        boolean active,
        Integer stock,
        boolean outOfStock,
        List<ItemOptionDto> options,
        LocalDateTime modifiedAt

) {

}
