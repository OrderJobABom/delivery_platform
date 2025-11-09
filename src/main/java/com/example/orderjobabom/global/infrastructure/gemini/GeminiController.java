package com.example.orderjobabom.global.infrastructure.gemini;

import com.example.orderjobabom.global.presentation.CustomResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Optional;

@RestController
@RequestMapping("/v1/gemini")
@RequiredArgsConstructor
public class GeminiController {

    private final GeminiService geminiService;

    @PostMapping("/generate")
    public ResponseEntity<CustomResponse<PromptResponse>> generateContent(
            @Valid @RequestBody PromptRequest request
    ) {

        Optional<PromptResponse> responseOptional = geminiService.getGeminiResponse(request.prompt())
                .blockOptional(Duration.ofSeconds(20));

        // 3. Optional의 존재 여부로 분기
        if (responseOptional.isPresent()) {
            PromptResponse promptResponse = responseOptional.get();
            return ResponseEntity.ok(CustomResponse.onSuccess(promptResponse));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(CustomResponse.onSuccess(null));
        }
    }
}
