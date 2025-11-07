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

import java.nio.file.AccessDeniedException;
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
        e.printStackTrace();
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

    // JWT 또는 OAuth2 인증 관련 예외 처리 (Keycloak 세션 만료 포함)
    @ExceptionHandler({org.springframework.security.oauth2.core.OAuth2AuthenticationException.class,
            org.springframework.security.oauth2.jwt.JwtValidationException.class})
    public ResponseEntity<CustomResponse<?>> handleJwtAuthException(Exception e) {

        // 401 Unauthorized 정의 불러오기
        ErrorReasonDTO dto = GeneralErrorCode.UNAUTHORIZED_401.getReasonHttpStatus();

        log.error("Token invalid or expired: {}", e.getMessage());

        String failMessage = "토큰이 만료되었거나 세션이 종료되었습니다. 다시 로그인해주세요.";

        return ResponseEntity.status(dto.getHttpStatus())
                .body(CustomResponse.onFailure(dto.getMessage(), dto.getCode(), failMessage));
    }


    // 6. 권한(인가) 거부 — 추가해야 할 부분!
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CustomResponse<?>> handleAccessDeniedException(AccessDeniedException e) {
        ErrorReasonDTO dto = GeneralErrorCode.FORBIDDEN_403.getReasonHttpStatus();

        log.error("AccessDeniedException: {}", e.getMessage());

        return ResponseEntity.status(dto.getHttpStatus())
                .body(CustomResponse.onFailure("권한이 없는 잘못된 접근입니다.", dto.getCode(), null));
    }
}
