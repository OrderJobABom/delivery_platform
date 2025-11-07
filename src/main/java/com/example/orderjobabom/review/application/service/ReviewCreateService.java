package com.example.orderjobabom.review.application.service;

import com.example.orderjobabom.global.presentation.event.Events;
import com.example.orderjobabom.order.domain.OrderId;
import com.example.orderjobabom.review.domain.*;
import com.example.orderjobabom.review.domain.exception.ReviewAlreadyDeletedException;
import com.example.orderjobabom.review.domain.exception.ReviewAlreadyExistsException;
import com.example.orderjobabom.store.domain.Store;
import com.example.orderjobabom.store.domain.StoreId;
import com.example.orderjobabom.store.domain.StoreRepository;
import com.example.orderjobabom.user.domain.UserId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewCreateService {

    private final ReviewRepository reviewRepository;
    private final ReviewPolicy reviewPolicy;
    private final StoreRepository storeRepository;

    @Transactional
    public ReviewId create(OrderId orderId,
                           UUID reviewerId,
                           String reviewerName,
                           String reviewerEmail,
                           StoreId storeId,
                           Rating rating,
                           String content) {

        // 리뷰 중복 확인
        reviewRepository.findByOrderId(orderId).ifPresent(existing -> {
            switch (existing.getStatus()) {
                case ACTIVE -> throw new ReviewAlreadyExistsException();
                case DELETED -> throw new ReviewAlreadyDeletedException();
            }
        });

        // Reviewer 생성
        Reviewer reviewer = Reviewer.of(reviewerId, reviewerName, reviewerEmail);

        // 주문자 검증
        reviewPolicy.validateCanWriteReview(orderId, reviewer.getId());

        // StoreSummary 생성
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다."));
        StoreSummary storeSummary = StoreSummary.of(store.getId().getId(), store.getStoreName());

        // Review 생성
        Review review = Review.builder()
                .orderId(orderId)
                .reviewer(reviewer)
                .store(storeSummary)
                .rating(rating)
                .content(content)
                .build();

        Review saved = reviewRepository.save(review);

        // 도메인 이벤트 발행
        Events.raise(new ReviewCreatedEvent(storeId, saved.getId().getId()));

        return saved.getId();
    }
}
