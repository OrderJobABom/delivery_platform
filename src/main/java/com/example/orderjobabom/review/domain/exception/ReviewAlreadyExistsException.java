package com.example.orderjobabom.review.domain.exception;

import com.example.orderjobabom.global.presentation.exception.FailException;

public class ReviewAlreadyExistsException extends FailException {
    public ReviewAlreadyExistsException() {
        super(ReviewErrorCode.REVIEW_ALREADY_EXISTS);
    }
}
