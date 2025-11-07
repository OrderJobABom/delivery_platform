package com.example.orderjobabom.review.infrastructure.security;

import com.example.orderjobabom.review.domain.Review;
import com.example.orderjobabom.user.domain.UserId;
import org.springframework.stereotype.Component;

@Component
public class SecurityReviewRoleCheckHelper {

    /**
     * 리뷰 작성자 본인인지 검증
     */
    public void validateReviewOwner(Review review, UserId currentUserId) {
        if (review.getReviewer() == null || review.getReviewer().getId() == null) {
            throw new IllegalArgumentException("리뷰 작성자 정보가 존재하지 않습니다.");
        }

        // UUID 끼리 비교해야 함
        if (!review.getReviewer().getId().equals(currentUserId.getId())) {
            throw new IllegalArgumentException("리뷰 작성자만 수정 또는 삭제할 수 있습니다.");
        }
    }
}
