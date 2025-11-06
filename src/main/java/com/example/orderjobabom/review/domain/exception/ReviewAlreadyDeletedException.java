package com.example.orderjobabom.review.domain.exception;

import com.example.orderjobabom.global.presentation.exception.FailException;

public class ReviewAlreadyDeletedException extends FailException {
    public ReviewAlreadyDeletedException() {
        super(ReviewErrorCode.REVIEW_ALREADY_DELETED);
    }
}
