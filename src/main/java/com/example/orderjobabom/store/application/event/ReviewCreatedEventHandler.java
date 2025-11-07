package com.example.orderjobabom.store.application.event;

import com.example.orderjobabom.review.domain.ReviewCreatedEvent;
import com.example.orderjobabom.review.domain.ReviewRepository;
import com.example.orderjobabom.store.domain.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ReviewCreatedEventHandler {

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;

    @Async
    @Transactional
    @TransactionalEventListener
    public void handle(ReviewCreatedEvent event) {
        double avg = reviewRepository.calculateAverageByStoreId(event.storeId().getId());
        storeRepository.updateAverageRating(event.storeId().getId(), avg);

        System.out.println("평균 별점 갱신 완료: storeId="
                + event.storeId().getId() + ", avg=" + avg);
    }
}
