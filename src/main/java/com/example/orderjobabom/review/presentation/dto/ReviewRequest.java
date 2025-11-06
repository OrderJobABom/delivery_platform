package com.example.orderjobabom.review.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "리뷰 작성 요청 DTO")
public record ReviewRequest(

        @Schema(
                description = "주문 ID",
                example = "33333333-3333-3333-3333-333333333333"
        )
        UUID orderId,

        @Schema(
                description = "가게 ID",
                example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
        )
        UUID storeId,

        @Schema(
                description = "별점 (1~5)",
                example = "4"
        )
        int rating,

        @Schema(
                description = "리뷰 본문 내용",
                example = "맛있어요"
        )
        String content
) {}
