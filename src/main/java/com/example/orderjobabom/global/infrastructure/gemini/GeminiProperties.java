package com.example.orderjobabom.global.infrastructure.gemini;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gemini.api")
public record GeminiProperties(
        String key,
        String baseUrl
) {
}
