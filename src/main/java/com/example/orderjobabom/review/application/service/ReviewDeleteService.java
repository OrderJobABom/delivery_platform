package com.example.orderjobabom.review.application.service;

import com.example.orderjobabom.review.domain.*;
import com.example.orderjobabom.review.infrastructure.security.SecurityReviewRoleCheckHelper;
import com.example.orderjobabom.user.domain.UserId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewDeleteService {

    private final ReviewRepository reviewRepository;
    private final SecurityReviewRoleCheckHelper reviewRoleCheckHelper;

    @Transactional
    public void delete(UUID reviewId, UUID userUuid) {
        Review review = reviewRepository.findById(new ReviewId(reviewId))
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        // 작성자 검증
        reviewRoleCheckHelper.validateReviewOwner(review, new UserId(userUuid));

        // 답글 존재 시 삭제 불가
        if (review.getReply() != null) {
            throw new IllegalStateException("사장님 답글이 달린 리뷰는 삭제할 수 없습니다.");
        }

        // 소프트 삭제 처리
        review.delete();
    }
}
