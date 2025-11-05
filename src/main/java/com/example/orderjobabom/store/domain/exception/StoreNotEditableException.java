package com.example.orderjobabom.store.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class StoreNotEditableException extends HttpStatusCodeException {
    public StoreNotEditableException() {
        this("상점의 수정/삭제 권한이 없습니다.");
    }

    public StoreNotEditableException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
