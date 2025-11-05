package com.example.orderjobabom.store.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ItemRequest (
      @Size Integer price,
      @NotBlank String name,
//      ItemStatus status, TODO 머지 후 엔티티 생기면 주석 풀기
      boolean active,
      Integer stock,
      List<ItemOptionRequset> itemOptions
){}
