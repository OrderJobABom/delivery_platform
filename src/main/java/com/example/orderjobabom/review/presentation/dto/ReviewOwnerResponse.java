package com.example.orderjobabom.review.presentation.dto;

import com.example.orderjobabom.review.domain.ReplyStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Schema(description = "사장님 리뷰 조회 응답 DTO")
public record ReviewOwnerResponse(
        @Schema(description = "리뷰 ID")
        UUID reviewId,

        @Schema(description = "별점 (1~5)")
        int rating,

        @Schema(description = "리뷰 내용")
        String content,

        @Schema(description = "답글 내용 (없으면 null)")
        String replyContent,

        @Schema(description = "답글 상태 (ACTIVE, DELETED, BLOCKED)")
        ReplyStatus replyStatus,

        @Schema(description = "답글 작성일시 (없으면 null)")
        LocalDateTime replyCreatedAt,

        @Schema(description = "리뷰 작성일시")
        LocalDateTime createdAt
) {}
