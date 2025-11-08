package com.example.orderjobabom.global.infrastructure.gemini;

import com.example.orderjobabom.global.presentation.CustomResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/gemini")
@RequiredArgsConstructor
public class GeminiController {

    private final GeminiService geminiService;

    @PostMapping("/generate")
    public CustomResponse<Mono<ResponseEntity<PromptResponse>>> generateContent(
            @Valid @RequestBody PromptRequest request
    ) {
        Mono<ResponseEntity<PromptResponse>> response = geminiService.getGeminiResponse(request.prompt())
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());

        return CustomResponse.onSuccess(response);
    }
}
