package com.example.orderjobabom.global.infrastructure.gemini;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@WebFluxTest(controllers = GeminiController.class)
//@Import(SecurityConfig.class)
class GeminiControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private GeminiService geminiService;

    @Test
    @WithMockUser("OWNER")
    void 내용_생성_성공() {
        String prompt = "점심시간에 먹기 좋은 1인 음식 추천해줘";
        String responseText = "점심시간에 먹기 좋음 음식은...";
        PromptResponse response = new PromptResponse(responseText);

        given(geminiService.getGeminiResponse(anyString()))
                .willReturn(Mono.just(response));

        webTestClient
                .post()
                .uri("/v1/gemini/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"prompt\": \"" + prompt + "\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.responseText").isEqualTo(responseText);
    }

}