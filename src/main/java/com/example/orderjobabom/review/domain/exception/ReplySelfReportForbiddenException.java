package com.example.orderjobabom.review.domain.exception;

import com.example.orderjobabom.global.presentation.exception.FailException;

public class ReplySelfReportForbiddenException extends FailException {
    public ReplySelfReportForbiddenException() {
        super(ReviewErrorCode.REPLY_SELF_REPORT_FORBIDDEN);
    }
}