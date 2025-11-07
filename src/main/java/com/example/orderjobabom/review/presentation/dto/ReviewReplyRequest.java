package com.example.orderjobabom.review.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "답글 요청 DTO")
public record ReviewReplyRequest(
        @Schema(description = "답글 내용", example = "좋은 리뷰 감사합니다!")
        String content
) {}
