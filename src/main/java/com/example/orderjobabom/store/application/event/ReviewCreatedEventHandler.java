package com.example.orderjobabom.store.application.event;

import com.example.orderjobabom.review.domain.ReviewRepository;
import com.example.orderjobabom.review.domain.ReviewCreatedEvent;
import com.example.orderjobabom.store.domain.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewCreatedEventHandler {

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;

    @Async // 비동기 실행
    @EventListener
    public void handle(ReviewCreatedEvent event) {
        double avg = reviewRepository.calculateAverageByStoreId(event.storeId().getId());
        storeRepository.updateAverageRating(event.storeId().getId(), avg);

        System.out.println("평균 별점 갱신 완료: storeId="
                + event.storeId().getId() + ", avg=" + avg);
    }
}
