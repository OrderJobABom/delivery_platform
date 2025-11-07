package com.example.orderjobabom.review.presentation.dto;

public record ReviewUpdateRequest(
        int rating,
        String content
) {}
