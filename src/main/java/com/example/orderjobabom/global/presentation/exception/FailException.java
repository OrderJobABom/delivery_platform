package com.example.orderjobabom.global.presentation.exception;

import com.example.orderjobabom.global.presentation.error.BaseErrorCode;

public class FailException extends GeneralException {

    public FailException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
