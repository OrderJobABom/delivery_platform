package com.example.orderjobabom.store.domain.exception;

import com.example.orderjobabom.global.presentation.success.BaseSuccessCode;
import com.example.orderjobabom.global.presentation.success.SuccessReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StoreItemSuccessCode implements BaseSuccessCode {

    ITEM_CREATE_SUCCESS(HttpStatus.CREATED, "200", "메뉴를 생성했습니다."),
    ITEM_UPDATE_SUCCESS(HttpStatus.OK, "201", "메뉴 수정 완료했습니다."),
    ITEM_DELETE_SUCCESS(HttpStatus.OK, "201", "메뉴를 성공적으로 삭제했습니다."),
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
