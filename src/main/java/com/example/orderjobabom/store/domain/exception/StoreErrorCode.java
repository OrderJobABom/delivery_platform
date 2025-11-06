package com.example.orderjobabom.store.domain.exception;

import com.example.orderjobabom.global.presentation.error.BaseErrorCode;
import com.example.orderjobabom.global.presentation.error.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StoreErrorCode implements BaseErrorCode {
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "관련된 카테고리는 존재하지 않습니다."),
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "관련된 가게는 존재하지 않습니다.")
    ;


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }
}
