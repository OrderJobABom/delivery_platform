package com.example.orderjobabom.store.domain.dto;

import com.example.orderjobabom.store.domain.Category;

import java.util.List;

public record StoreSearch(
        List<Category> category,
        String storeName,
        String storeTel,
        String sido,
        String sigugun,
        String dong,
        String keyword // 통합 검색
) {}