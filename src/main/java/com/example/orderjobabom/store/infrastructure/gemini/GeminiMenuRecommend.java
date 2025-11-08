package com.example.orderjobabom.store.infrastructure.gemini;

import com.example.orderjobabom.global.infrastructure.gemini.Gemini2Service;
import com.example.orderjobabom.store.domain.Category;
import com.example.orderjobabom.store.domain.MenuAiRecommend;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeminiMenuRecommend implements MenuAiRecommend {
    private final Gemini2Service service;

    @Override
    public String getMenu(Category category, String menuName) {

        String prompt = String.format("음식분류는 %s이고 %s을(를) 판매할거야 20대에 어필할 수 있는 재치있고 웃음이 나는 메뉴명 한개를 추천하는데 한 단어로 출력해줘", category.toString(), menuName);

        return service.inference(prompt, String.class);
    }
}
