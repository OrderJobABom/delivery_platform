package com.example.orderjobabom.store.domain.exception;

import com.example.orderjobabom.global.presentation.success.BaseSuccessCode;
import com.example.orderjobabom.global.presentation.success.SuccessReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StoreOrderSuccessCode implements BaseSuccessCode {

    STORE_ORDER_OK(HttpStatus.OK, "201", "가게 주문 내역을 조회했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


    @Override
    public SuccessReasonDTO getReasonHttpStatus() {
        return SuccessReasonDTO.builder()
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }
}
