package com.example.orderjobabom.global.presentation.error;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum GeneralErrorCode implements BaseErrorCode {

    BAD_REQUEST_400(HttpStatus.BAD_REQUEST, "400", "잘못된 요청입니다."),
    UNAUTHORIZED_401(HttpStatus.UNAUTHORIZED, "401", "인증이 필요합니다."),
    FORBIDDEN_403(HttpStatus.FORBIDDEN, "403", "권한이 없는 잘못된 접근입니다."),
    NOT_FOUND_404(HttpStatus.BAD_REQUEST, "404", "요청 자원을 찾을 수 없습니다."),
    NOT_FOUND_500(HttpStatus.INTERNAL_SERVER_ERROR, "500", "서버 내부 오류입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

}
