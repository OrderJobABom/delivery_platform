package com.example.orderjobabom.global.presentation.exception;

import com.example.orderjobabom.global.presentation.error.BaseErrorCode;
import com.example.orderjobabom.global.presentation.error.ErrorReasonDTO;
import org.springframework.http.HttpStatus;

public class BadRequestException extends GeneralException {

    public BadRequestException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }

    // 비즈니스 실패와 요청 실패에 대한 처리를 다르게하기 위해 만듬
    public BadRequestException(String message) {
        super(() -> ErrorReasonDTO.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .isSuccess(false)
                .code("400")
                .message(message)
                .build()
        );
    }
}
