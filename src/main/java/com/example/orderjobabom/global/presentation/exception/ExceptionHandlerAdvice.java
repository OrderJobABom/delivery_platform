package com.example.orderjobabom.global.presentation.exception;

import com.example.orderjobabom.global.presentation.CustomResponse;
import com.example.orderjobabom.global.presentation.error.BaseErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {

    // 커스텀 예외
    @ExceptionHandler(FailException.class)
    public ResponseEntity<CustomResponse<?>> handleCustomException(FailException e) {

        BaseErrorCode errorCode = e.getErrorCode();
        CustomResponse<?> response =

        return new ResponseEntity<>()

    }

    // 일반 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse<?>> handleException(Exception e) {}

    // DTO 검증 처리


}
