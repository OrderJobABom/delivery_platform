package com.example.orderjobabom.global.presentation.exception;

import com.example.orderjobabom.global.presentation.error.BaseErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final BaseErrorCode errorCode;


    public CustomException(BaseErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
