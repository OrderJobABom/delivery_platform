package com.example.orderjobabom.global.presentation;

import com.example.orderjobabom.global.presentation.success.BaseSuccessCode;
import com.example.orderjobabom.global.presentation.success.GeneralSuccessCode;
import com.example.orderjobabom.global.presentation.success.SuccessReasonDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "message", "code", "result"})
public class CustomResponse<T> {

    @JsonProperty("isSuccess")
    private Boolean isSuccess;

    private String message;

    private String code;

    private T result;

    public static <T> CustomResponse<T> onSuccess(T result) {

        SuccessReasonDTO dto = GeneralSuccessCode._OK.getReasonHttpStatus();
        return new CustomResponse<>(true, dto.getMessage(), dto.getCode(), result);
    }

    public static <T> CustomResponse<T> of(BaseSuccessCode successCode, T result) {

        SuccessReasonDTO dto = successCode.getReasonHttpStatus();
        return new CustomResponse<>(true, dto.getMessage(), dto.getCode(), result);
    }

    public static <T> CustomResponse<T> onFailure(String message, String code, T data) {

        return new CustomResponse<>(false, message, code, data);
    }

    public static <T> CustomResponse<T> onFailure(String message, String code) {

        return CustomResponse.<T>builder()
                .message(message)
                .code(code)
                .build();
    }
}
