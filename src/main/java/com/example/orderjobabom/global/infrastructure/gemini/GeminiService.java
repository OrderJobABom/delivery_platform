package com.example.orderjobabom.global.infrastructure.gemini;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiService {

    private final WebClient geminiWebClient;
    private final GeminiProperties geminiProperties;

    private static final String API_URL_TEMPLATE = "/v1beta/models/gemini-pro:generateContent?key=%s";

    public Mono<PromptResponse> getGeminiResponse(String prompt) {

        String apiUrl = String.format(API_URL_TEMPLATE, geminiProperties.key());
        GeminiRequestDto requestDto = GeminiRequestDto.fromPrompt(prompt);

        Mono<PromptResponse> promptResponseMono = geminiWebClient.post()
                .uri(apiUrl)
                .bodyValue(requestDto)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(
                                        new GeminiApiException("Gemini Api Error: " + errorBody, clientResponse.statusCode())
                                ))
                )
                .bodyToMono(GeminiResponseDto.class)
                .map(responseDto -> new PromptResponse(responseDto.getFirstCandidateText()))
                .doOnError(throwable -> {
                    log.info("Gemini Api error: " + throwable.getMessage());
                    System.err.println("Gemini Api Error: " + throwable.getMessage());
                });

        return promptResponseMono;
    }
}
