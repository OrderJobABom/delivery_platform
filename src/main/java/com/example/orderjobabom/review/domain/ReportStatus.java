package com.example.orderjobabom.review.domain;

public enum ReportStatus {
    PENDING,    // 접수됨 (대기)
    REVIEWED,   // 관리자 검토 완료 (승인)
    REJECTED, // 반려됨
    UNBLOCKED// 정지 해제
}