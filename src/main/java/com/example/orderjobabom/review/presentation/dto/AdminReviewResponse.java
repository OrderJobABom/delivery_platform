package com.example.orderjobabom.review.presentation.dto;

import com.example.orderjobabom.review.domain.*;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record AdminReviewResponse(
        UUID reviewId,
        String reviewerName,
        String reviewerEmail,
        String storeName,
        String content,
        String replyContent,
        String replyerName,
        String replyerRole,
        ReplyStatus replyStatus,
        ReviewStatus status,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        boolean reviewReported,          // 리뷰가 신고되었는가?
        ReportStatus reviewReportStatus, // 리뷰 신고 상태
        boolean replyReported,           // 답글이 신고되었는가?
        ReportStatus replyReportStatus   // 답글 신고 상태
) {
    public static AdminReviewResponse from(Review review) {
        Reply reply = review.getReply();

        return AdminReviewResponse.builder()
                .reviewId(review.getId().getId())
                .reviewerName(review.getReviewer().getName())
                .reviewerEmail(review.getReviewer().getEmail())
                .storeName(review.getStore().getName())
                .content(review.getContent())
                .replyContent(reply != null ? reply.getContent() : null)
                .replyerName(reply != null && reply.getReplyer() != null ? reply.getReplyer().getName() : null)
                .replyerRole(reply != null && reply.getReplyer() != null ? reply.getReplyer().getRole() : null)
                .replyStatus(reply != null ? reply.getStatus() : null)
                .status(review.getStatus())
                .createdAt(review.getCreatedAt())
                .modifiedAt(review.getModifiedAt())
                .build();
    }
}
