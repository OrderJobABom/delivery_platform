package com.example.orderjobabom.user.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record TokenRequest(
        @NotBlank
        @Schema(title="로그인 아이디", example="test02")
        String username,

        @NotBlank
        @Schema(title="로그인 비밀번호", example="A123adad@")
        String password
) {}
