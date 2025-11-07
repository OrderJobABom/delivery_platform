package com.example.orderjobabom.global.infrastructure.gemini;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GeminiServiceTest {

    @Autowired
    Gemini2Service service;

    @Test
    void geminiServiceTest() {
        String res = service.inference("음식분류는 한식이고 일본식 돈가스를 판매할거야 20대에 어필할 수 있는 재치있고 웃음이 나는 메뉴명 한개를 추천하는데 한 단어로 출력해줘", String.class);
        System.out.println(res);
    }
}
