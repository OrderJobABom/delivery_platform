package com.example.orderjobabom.global.infrastructure.gemini;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class Gemini2Service {
    @Value("${gemini.api.key}")
    private String apiKey;

    private final ObjectMapper om;

    public <T> T inference(String prompt, Class<T> clazz) {
        if (!StringUtils.hasText(apiKey) || !StringUtils.hasText(prompt)) return null;

        Client client = Client.builder().apiKey(apiKey).build();

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-2.5-flash",
                        prompt,
                        null);

        String res = response.text();

        try {
            return clazz == String.class ? (T)res : om.readValue(res, clazz);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }

        return  null;
    }
}
