package com.example.orderjobabom.user.presentation.dto;

public record TokenResponse(
        String accessToken,
        int expiresIn,
        int refreshExpiresIn,
        String refreshToken,
        String tokenType
) {}
