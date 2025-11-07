package com.example.orderjobabom.review.domain.exception;

import com.example.orderjobabom.global.presentation.exception.FailException;

public class ReportAlreadyExistsException extends FailException {
    public ReportAlreadyExistsException() {
        super(ReviewErrorCode.REVIEW_REPORT_ALREADY_EXISTS);
    }
}
