package com.example.orderjobabom.menu.presentation.dto.request;

import com.example.orderjobabom.global.infrastructure.persistence.Price;
import com.example.orderjobabom.menu.domain.ItemOption;
import com.example.orderjobabom.menu.domain.Stock;
import com.example.orderjobabom.store.domain.Category;

import java.util.List;
import java.util.UUID;

public record UpdateItemRequestDTO(
        UUID itemId,
        Category category,
        Price price,
        String name,
        Stock stock,
        List<ItemOption> itemOptions
) {
}
