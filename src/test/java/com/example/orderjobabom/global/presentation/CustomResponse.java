package com.example.orderjobabom.global.presentation;

import com.example.orderjobabom.global.presentation.success.BaseSuccessCode;
import org.springframework.http.HttpStatus;

public class CustomResponse<T> {

    private Boolean isSuccess;

    private HttpStatus status;

    private String message;

    private String code;

    private T result;

    public static CustomResponse<?> onSuccess(BaseSuccessCode baseSuccessCode) {


    }
}
