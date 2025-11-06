package com.example.orderjobabom.review.domain.exception;

import com.example.orderjobabom.global.presentation.exception.FailException;

public class ReviewNotEditableException extends FailException {
    public ReviewNotEditableException() {
        super(ReviewErrorCode.REVIEW_NOT_EDITABLE);
    }
}
