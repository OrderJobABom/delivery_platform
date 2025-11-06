package com.example.orderjobabom.review.presentation.dto;

import com.example.orderjobabom.review.domain.Reply;
import com.example.orderjobabom.review.domain.ReplyStatus;
import com.example.orderjobabom.review.domain.Review;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ReviewResponse(
        UUID reviewId,              // 리뷰 ID
        UUID orderId,               // 주문 ID
        String reviewerName,        // 리뷰 작성자 이름
        String storeName,           // 가게 이름
        int rating,                 // 별점
        String content,             // 리뷰 본문
        String replyContent,        // 답글 내용 (없으면 null)
        String replyerName,         // 답글 작성자 이름 (없으면 null)
        String replyerRole,         // 답글 작성자 역할 (없으면 null)
        ReplyStatus replyStatus,    // 답글 상태
        LocalDateTime replyCreatedAt, // 답글 작성 시간
        LocalDateTime createdAt     // 리뷰 작성 시간
) {

    public static ReviewResponse from(Review review) {
        Reply reply = review.getReply();

        return ReviewResponse.builder()
                .reviewId(review.getId().getId())
                .orderId(review.getOrderId().getId())
                .reviewerName(review.getReviewer().getName())
                .storeName(review.getStore().getName())
                .rating(review.getRating().getValue())
                .content(review.getContent())
                .replyContent(reply != null ? reply.getContent() : null)
                .replyerName(reply != null && reply.getReplyer() != null ? reply.getReplyer().getName() : null)
                .replyerRole(reply != null && reply.getReplyer() != null ? reply.getReplyer().getRole() : null)
                .replyStatus(reply != null ? reply.getStatus() : null)
                .replyCreatedAt(reply != null ? reply.getCreatedAt() : null)
                .createdAt(review.getCreatedAt())
                .build();
    }
}
