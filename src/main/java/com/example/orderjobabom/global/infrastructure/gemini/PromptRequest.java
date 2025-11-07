package com.example.orderjobabom.global.infrastructure.gemini;

import jakarta.validation.constraints.NotBlank;

public record PromptRequest(@NotBlank(message = "프롬프트는 비어 있을 수 있습니다.") String prompt) {}
