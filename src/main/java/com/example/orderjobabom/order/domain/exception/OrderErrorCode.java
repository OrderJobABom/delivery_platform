package com.example.orderjobabom.order.domain.exception;

import com.example.orderjobabom.global.presentation.error.BaseErrorCode;
import com.example.orderjobabom.global.presentation.error.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OrderErrorCode implements BaseErrorCode {

    ORDER_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "주문 상품을 찾을 수 없습니다."),
    ORDER_ITEM_EMPTY(HttpStatus.NOT_FOUND, "404", "주문 상품은 비어있을 수 없습니다."),
    ORDER_DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "배달 정보를 찾을 수 없습니다."),
    ORDER_DATE_FORMAT_BAD_REQUEST(HttpStatus.BAD_REQUEST, "400", "종료날짜가 시작날짜보다 이를 수 없습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "관련 주문을 찾을 수 없습니다."),
    ORDER_CANNOT_BE_CANCELED(HttpStatus.BAD_REQUEST, "404", "주문을 취소할 수 없습니다."),
    ORDER_CANCEL_TIME_OUT(HttpStatus.BAD_REQUEST, "404", "주문한지 5분이 넘어 취소할 수 없습니다."),
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
