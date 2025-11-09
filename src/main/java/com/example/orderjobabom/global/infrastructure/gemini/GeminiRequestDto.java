package com.example.orderjobabom.global.infrastructure.gemini;

import java.util.List;

public record GeminiRequestDto(
        List<Content> contents
) {

    public static GeminiRequestDto fromPrompt(String prompt) {
        Part part = new Part(prompt);
        Content content = new Content(List.of(part));
        return new GeminiRequestDto(List.of(content));
    }
}

record Content(
        List<Part> parts
) {}

record Part(
        String text
) {}
