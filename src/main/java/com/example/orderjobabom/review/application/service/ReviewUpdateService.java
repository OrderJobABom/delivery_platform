package com.example.orderjobabom.review.application.service;

import com.example.orderjobabom.review.domain.*;
import com.example.orderjobabom.review.infrastructure.security.SecurityReviewRoleCheckHelper;
import com.example.orderjobabom.review.presentation.dto.ReviewUpdateRequest;
import com.example.orderjobabom.user.domain.UserId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewUpdateService {

    private final ReviewRepository reviewRepository;
    private final SecurityReviewRoleCheckHelper reviewRoleCheckHelper;

    @Transactional
    public void update(UUID reviewId, UUID userUuid, ReviewUpdateRequest request) {
        Review review = reviewRepository.findById(new ReviewId(reviewId))
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        // 작성자 검증 (JWT의 userId와 리뷰 작성자 비교)
        reviewRoleCheckHelper.validateReviewOwner(review, new UserId(userUuid));

        // 내용 및 별점 수정
        review.updateContent(request.content(), new Rating(request.rating()));
    }
}
