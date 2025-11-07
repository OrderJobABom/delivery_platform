package com.example.orderjobabom.review.domain.exception;

import com.example.orderjobabom.global.presentation.exception.FailException;

public class ReplyNotFoundException extends FailException {
    public ReplyNotFoundException() {
        super(ReviewErrorCode.REVIEW_REPLY_NOT_FOUND);
    }
}
