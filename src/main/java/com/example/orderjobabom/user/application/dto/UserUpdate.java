package com.example.orderjobabom.user.application.dto;


import lombok.Builder;

@Builder
public record UserUpdate(
        String email,
        String firstName,
        String lastName,
        String mobile
) {}