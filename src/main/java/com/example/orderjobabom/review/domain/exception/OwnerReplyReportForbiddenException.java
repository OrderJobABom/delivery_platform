package com.example.orderjobabom.review.domain.exception;


import com.example.orderjobabom.global.presentation.exception.FailException;

public class OwnerReplyReportForbiddenException extends FailException {
    public OwnerReplyReportForbiddenException() {
        super(ReviewErrorCode.OWNER_REPLY_REPORT_FORBIDDEN);
    }
}