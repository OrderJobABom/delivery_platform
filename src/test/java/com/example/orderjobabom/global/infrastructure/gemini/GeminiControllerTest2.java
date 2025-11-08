package com.example.orderjobabom.global.infrastructure.gemini;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class GeminiControllerTest2 {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    GeminiService service;

    @Autowired
    ObjectMapper om;

    @Test
    @DisplayName("AI 메뉴 추천 테스트")
    void aiRequestTest() throws Exception {
        String prompt = "점심시간에 먹기 좋은 1인 음식 추천해줘";
        PromptRequest request = new PromptRequest(prompt);


        mockMvc.perform(post("/v1/gemini/generate")
        .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(request)))
                .andDo(print());
    }
}
