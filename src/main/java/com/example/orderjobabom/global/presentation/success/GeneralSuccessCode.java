package com.example.orderjobabom.global.presentation.success;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum GeneralSuccessCode implements BaseSuccessCode {

    OK_200(HttpStatus.ACCEPTED, "200", "요청이 성공적으로 처리되었습니다."),
    CREATED_201(HttpStatus.CREATED, "201", "새로운 리소스가 생성되었습니다."),
    DELETED_204(HttpStatus.NO_CONTENT, "204", "기존 리소스가 삭제되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

}
