package com.example.orderjobabom.store.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class CategoryNotFoundException extends HttpStatusCodeException {
    public CategoryNotFoundException() {
        super(HttpStatus.NOT_FOUND, "분류를 찾을 수 없습니다.");
    }
}

