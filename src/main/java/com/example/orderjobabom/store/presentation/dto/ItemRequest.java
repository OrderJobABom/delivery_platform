package com.example.orderjobabom.store.presentation.dto;

import com.example.orderjobabom.menu.domain.ItemStatus;
import com.example.orderjobabom.store.domain.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ItemRequest (
      @Size Integer price,
      @NotBlank String name,
      ItemStatus status,
      Category category,
      boolean active,
      Integer stock,
      List<ItemOptionRequest> itemOptions
){}
