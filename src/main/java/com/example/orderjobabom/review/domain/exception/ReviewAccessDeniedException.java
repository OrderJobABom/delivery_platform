package com.example.orderjobabom.review.domain.exception;

import com.example.orderjobabom.global.presentation.exception.FailException;

public class ReviewAccessDeniedException extends FailException {
    public ReviewAccessDeniedException() {
        super(ReviewErrorCode.REVIEW_ACCESS_DENIED);
    }
}
