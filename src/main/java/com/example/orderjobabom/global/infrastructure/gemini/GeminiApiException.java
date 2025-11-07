package com.example.orderjobabom.global.infrastructure.gemini;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class GeminiApiException extends RuntimeException {

    private final HttpStatusCode httpStatusCode;

    public GeminiApiException(String errorMessage, HttpStatusCode httpStatusCode) {
        super(errorMessage);
        this.httpStatusCode = httpStatusCode;
    }

}


