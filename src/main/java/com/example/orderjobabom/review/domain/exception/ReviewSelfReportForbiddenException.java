package com.example.orderjobabom.review.domain.exception;

import com.example.orderjobabom.global.presentation.exception.FailException;

public class ReviewSelfReportForbiddenException extends FailException {
    public ReviewSelfReportForbiddenException() {
        super(ReviewErrorCode.REVIEW_SELF_REPORT_FORBIDDEN);
    }
}