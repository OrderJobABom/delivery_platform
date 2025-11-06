package com.example.orderjobabom.store.presentation.dto;

import com.example.orderjobabom.store.domain.Category;

public record CategoryDto (
        Category category,
        boolean active
) {}
