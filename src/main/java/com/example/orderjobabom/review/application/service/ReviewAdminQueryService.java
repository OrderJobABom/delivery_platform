package com.example.orderjobabom.review.application.service;

import com.example.orderjobabom.review.domain.*;
import com.example.orderjobabom.review.presentation.dto.AdminReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewAdminQueryService {

    private final ReviewAdminQueryRepository reviewAdminQueryRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewReportRepository reviewReportRepository;

    /** 관리자용 리뷰 전체 조회 */
    public Page<AdminReviewResponse> getReviews(String reviewerName,
                                                String storeName,
                                                ReviewStatus reviewStatus,
                                                ReportStatus reportStatus,
                                                Pageable pageable) {
        return reviewAdminQueryRepository.findAllForAdmin(reviewerName, storeName, reviewStatus, reportStatus, pageable);
    }

    /**
     * 관리자 리뷰 또는 답글 삭제
     */
    public void deleteReviewOrReply(UUID reviewId, TargetType targetType, String adminName) {
        Review review = reviewRepository.findById(new ReviewId(reviewId))
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        if (targetType == TargetType.REVIEW) {
            review.deleteByAdmin(adminName);
        } else if (targetType == TargetType.REPLY) {
            review.deleteReplyByAdmin(adminName);
        } else {
            throw new IllegalArgumentException("올바르지 않은 TargetType입니다. (REVIEW or REPLY)");
        }
    }
}
