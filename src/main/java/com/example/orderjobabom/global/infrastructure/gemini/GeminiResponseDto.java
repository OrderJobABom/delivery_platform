package com.example.orderjobabom.global.infrastructure.gemini;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeminiResponseDto(
        List<Candidate> candidates
) {


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Candidate(
            Content content // 'content' 필드만 추출
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Content(
            List<Part> parts, // 'parts' 배열 추출
            String role      // 'role' 필드 (예: "model")
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Part(
            String text // 우리가 원하는 최종 텍스트
    ) {}

    public String getFirstCandidateText() {
        try {
            // 중첩 구조를 안전하게 탐색하여 text를 반환
            return candidates.get(0)
                    .content()
                    .parts()
                    .get(0)
                    .text();
        } catch (Exception e) {
            // 빈 문자열 반환
            return "";
        }
    }
}