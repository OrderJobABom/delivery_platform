package com.example.orderjobabom.global.presentation.exception;

import com.example.orderjobabom.global.presentation.error.BaseErrorCode;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {

    private final BaseErrorCode baseErrorCode;

    public GeneralException(BaseErrorCode baseErrorCode) {
        this.baseErrorCode = baseErrorCode;
    }
}
