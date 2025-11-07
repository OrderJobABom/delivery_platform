package com.example.orderjobabom.global.infrastructure.gemini;

import java.util.List;

public record GeminiRequestDto(List<Content> content) {

    public static record Content(List<Part> parts) {}
    public static record Part(String text) {}

    public static GeminiRequestDto fromPrompt(String prompt) {
        return new GeminiRequestDto(
                List.of(new Content(List.of(new Part(prompt))))
        );

    }
}
