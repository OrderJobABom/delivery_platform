package com.example.orderjobabom.global.presentation.exception;

import com.example.orderjobabom.global.presentation.CustomResponse;
import com.example.orderjobabom.global.presentation.error.BaseErrorCode;
import com.example.orderjobabom.global.presentation.error.ErrorReasonDTO;
import com.example.orderjobabom.global.presentation.error.GeneralErrorCode;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {

    // 일반 예외
    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<CustomResponse<?>> handleGeneralException(GeneralException e) {

        BaseErrorCode baseErrorCode = e.getBaseErrorCode();
        ErrorReasonDTO dto = baseErrorCode.getReasonHttpStatus();

        log.error("General Exception: [{}]", dto.getMessage());

        return ResponseEntity.status(dto.getHttpStatus()).body(CustomResponse.onFailure(dto.getMessage(), dto.getCode(), null));
    }

    // 일반 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse<?>> handleException(Exception e) {
        ErrorReasonDTO dto = GeneralErrorCode.INTERNAL_SERVER_500.getReasonHttpStatus();
        log.error("Exception : [{}] , Message : [{}]", e.getClass().getSimpleName(), e.getMessage());

        String failMessage = e.getClass().getSimpleName() + " " + e.getMessage();

        return ResponseEntity.status(dto.getHttpStatus()).body(CustomResponse.onFailure(dto.getMessage(), dto.getCode(), failMessage));
    }

    // 컨트롤러 파라미터 유효성 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomResponse<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        Map<String, String> errorMap = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach((fieldError) -> {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        ErrorReasonDTO dto = GeneralErrorCode.BAD_REQUEST_400.getReasonHttpStatus();
        log.error("MethodArgumentNotValidException: [{}]", errorMap);

        return ResponseEntity.status(dto.getHttpStatus()).body(CustomResponse.onFailure(dto.getMessage(), dto.getCode(), errorMap));
    }

    // 유효성 검사 실패
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomResponse<List<String>>> constraintViolationException(ConstraintViolationException e) {

        log.error(Arrays.toString(e.getStackTrace()));

        List<String> errorMessages = e.getConstraintViolations().stream().map(constraintViolation -> constraintViolation.getMessage()).toList();

        ErrorReasonDTO dto = GeneralErrorCode.BAD_REQUEST_400.getReasonHttpStatus();
        log.error("ConstraintViolationException: [{}]", dto.getMessage());

        return ResponseEntity.status(dto.getHttpStatus()).body(CustomResponse.onFailure(dto.getMessage(), dto.getCode(), errorMessages));

    }

}
