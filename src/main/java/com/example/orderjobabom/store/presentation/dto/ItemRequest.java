package com.example.orderjobabom.store.presentation.dto;

import com.example.orderjobabom.menu.domain.ItemStatus;
import com.example.orderjobabom.store.domain.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record ItemRequest (
      @Size Integer price,
      @NotBlank String name,
      ItemStatus status,
      Category category,
      boolean active,
      Integer stock,
      Boolean genAi,
      List<ItemOptionRequest> itemOptions
){}
