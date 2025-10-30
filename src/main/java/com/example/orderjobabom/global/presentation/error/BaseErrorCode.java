package com.example.orderjobabom.global.presentation.error;

import org.springframework.http.HttpStatus;

public interface BaseErrorCode {

    ErrorReasonDTO getReasonHttpStatus();
}
