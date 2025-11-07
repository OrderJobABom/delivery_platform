package com.example.orderjobabom.review.application.service;

import com.example.orderjobabom.review.domain.*;
import com.example.orderjobabom.review.domain.exception.ReviewAccessDeniedException;
import com.example.orderjobabom.review.domain.exception.ReviewNotFoundException;
import com.example.orderjobabom.review.presentation.dto.ReviewOwnerResponse;
import com.example.orderjobabom.store.domain.Store;
import com.example.orderjobabom.store.domain.StoreId;
import com.example.orderjobabom.store.domain.StoreRepository;
import com.example.orderjobabom.user.domain.UserId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewOwnerService {

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;

    /** 1. 내 가게 리뷰 전체 조회 */
    public List<ReviewOwnerResponse> getReviewsByOwner(UserId ownerId) {
        Store store = storeRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        List<Review> reviews = reviewRepository.findByStoreId(store.getId().getId());

        return reviews.stream()
                .map(this::toOwnerResponse)
                .collect(Collectors.toList());
    }

    /** 2. 내가 작성한 답글(있는 리뷰만) 조회 */
    public List<ReviewOwnerResponse> getRepliedReviewsByOwner(UserId ownerId) {
        Store store = storeRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        List<Review> reviews = reviewRepository.findRepliedReviewsByStoreId(store.getId().getId());

        return reviews.stream()
                .map(this::toOwnerResponse)
                .collect(Collectors.toList());
    }

    /** 3. 답글 작성 */
    @Transactional
    public void addReply(UUID reviewId, UUID ownerUuid, String ownerName, String ownerRole, String content) {
        Review review = getValidReview(reviewId, new UserId(ownerUuid));

        Replyer replyer = Replyer.of(ownerUuid, ownerName, ownerRole);

        review.addReply(Reply.of(replyer, content));
    }


    /** 4. 답글 수정 */

    @Transactional
    public void updateReply(UUID reviewId, UUID ownerUuid, String ownerName, String ownerRole, String content) {
        Review review = getValidReview(reviewId, new UserId(ownerUuid));

        Replyer replyer = Replyer.of(ownerUuid, ownerName, ownerRole);

        review.updateReply(Reply.of(replyer, content));
    }

    /** 5. 답글 삭제 */
    @Transactional
    public void deleteReply(UUID reviewId, UserId ownerId) {
        Review review = getValidReview(reviewId, ownerId);
        review.removeReply();
    }

    /** 내부 검증 — 사장님 본인 가게의 리뷰인지 확인 */
    private Review getValidReview(UUID reviewId, UserId ownerId) {
        Review review = reviewRepository.findById(new ReviewId(reviewId))
                .orElseThrow(ReviewNotFoundException::new);

        // 사장님의 가게 찾기
        Store store = storeRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("가게 정보를 찾을 수 없습니다."));

        // 해당 리뷰가 그 가게의 리뷰인지 확인
        if (!store.getId().getId().equals(review.getStore().getId())) {
            throw new ReviewAccessDeniedException();
        }

        return review;
    }

    /** DTO 변환 */
    private ReviewOwnerResponse toOwnerResponse(Review review) {
        return ReviewOwnerResponse.builder()
                .reviewId(review.getId().getId())
                .rating(review.getRating().getValue())
                .content(review.getContent())
                .replyContent(review.getReply() != null ? review.getReply().getContent() : null)
                .replyCreatedAt(review.getReply() != null ? review.getReply().getCreatedAt() : null)
                .createdAt(review.getCreatedAt())
                .build();
    }

}
