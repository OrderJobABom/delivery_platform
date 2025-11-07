package com.example.orderjobabom.store.domain.exception;

import com.example.orderjobabom.global.presentation.success.BaseSuccessCode;
import com.example.orderjobabom.global.presentation.success.SuccessReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StoreSuccessCode implements BaseSuccessCode {
    STORE_CREATED(HttpStatus.CREATED,"201", "가게가 등록되었습니다."),
    STORE_UPDATED(HttpStatus.OK,"200", "가게가 수정되었습니다."),
    STORE_DELETED(HttpStatus.NO_CONTENT,"204", "가게가 삭제되었습니다."),;

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