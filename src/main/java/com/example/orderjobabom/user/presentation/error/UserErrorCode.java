package com.example.orderjobabom.user.presentation.error;

import com.example.orderjobabom.global.presentation.error.BaseErrorCode;
import com.example.orderjobabom.global.presentation.error.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements BaseErrorCode {

    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "U001", "이미 존재하는 아이디입니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "U002", "이미 존재하는 이메일입니다."),
    KEYCLOAK_REGISTER_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "U003", "회원 생성 중 오류가 발생했습니다.");

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
