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
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "관련된 가게는 존재하지 않습니다."),

    CAN_NOT_START_COOKING(HttpStatus.BAD_REQUEST, "COOKING_001", "주문 접수가 되지 않아 조리를 할 수 없습니다."),
    CAN_NOT_COMPLETE_COOKING(HttpStatus.BAD_REQUEST, "COOKING_002", "조리가 시작되지 않아 완료할 수 없습니다."),
    CAN_NOT_ACCESS(HttpStatus.FORBIDDEN, "COOKING_003", "해당 주문에 대한 접근 권한이 없습니다."),
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
