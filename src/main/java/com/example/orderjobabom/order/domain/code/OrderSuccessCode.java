package com.example.orderjobabom.order.domain.code;

import com.example.orderjobabom.global.presentation.success.BaseSuccessCode;
import com.example.orderjobabom.global.presentation.success.SuccessReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum OrderSuccessCode implements BaseSuccessCode {

    ORDER_CANCEL(HttpStatus.NO_CONTENT, "204", "주문이 취소 되었습니다."),
    ORDER_OK(HttpStatus.OK, "201", "주문 내역을 조회했습니다."),
    ORDER_CREATED(HttpStatus.CREATED, "200", "주문을 생성했습니다."),
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
