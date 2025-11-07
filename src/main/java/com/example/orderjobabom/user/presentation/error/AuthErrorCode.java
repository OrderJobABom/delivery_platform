package com.example.orderjobabom.user.presentation.error;

import com.example.orderjobabom.global.presentation.error.BaseErrorCode;
import com.example.orderjobabom.global.presentation.error.ErrorReasonDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AuthErrorCode implements BaseErrorCode {

    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A401", "리프레시 토큰이 만료되었습니다."),
    REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "A402", "리프레시 토큰이 유효하지 않습니다."),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A403", "액세스 토큰이 만료되었습니다."),
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "A404", "다시 로그인해주세요.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .httpStatus(this.httpStatus)
                .isSuccess(false)
                .code(this.code)
                .message(this.message)
                .build();
    }
}