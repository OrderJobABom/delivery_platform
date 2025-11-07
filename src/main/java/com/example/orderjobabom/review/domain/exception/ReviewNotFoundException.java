package com.example.orderjobabom.review.domain.exception;

import com.example.orderjobabom.global.presentation.exception.FailException;

public class ReviewNotFoundException extends FailException {
    public ReviewNotFoundException() {
        super(ReviewErrorCode.REVIEW_NOT_FOUND);
    }
}
