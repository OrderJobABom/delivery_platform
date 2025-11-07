package com.example.orderjobabom.review.presentation.dto;

import com.example.orderjobabom.review.domain.TargetType;

public record ReviewReportRequest(
        String reason,
        TargetType targetType
) {}
