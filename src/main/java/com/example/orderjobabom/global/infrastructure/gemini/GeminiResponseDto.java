package com.example.orderjobabom.global.infrastructure.gemini;

import java.util.List;

public record GeminiResponseDto(List<Candidate> candidates) {

    public static record Candidate(GeminiRequestDto.Content content) {}
    public static record Content(List<GeminiRequestDto.Part> parts, String role) {}
    public static record Part(String text){}

    /**
     * 응답 DTO에서 첫 번째 텍스트 응답을 추출합니다.
     * 응답이 없거나 비어있는 경우 빈 문자열을 반환합니다.
     */
    public String getFirstCandidateText() {
        if (candidates == null || candidates.isEmpty() ||
                candidates.get(0).content() == null ||
                candidates.get(0).content().parts() == null ||
                candidates.get(0).content().parts().isEmpty()) {
            return "";
        }
        return candidates.get(0).content().parts().get(0).text();
    }

}
