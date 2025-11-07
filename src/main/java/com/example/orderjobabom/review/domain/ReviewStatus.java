package com.example.orderjobabom.review.domain;

public enum ReviewStatus {
    ACTIVE,      // 정상 노출
    DELETED,     // 사용자 직접 삭제
    BLOCKED,
    HIDDEN
}