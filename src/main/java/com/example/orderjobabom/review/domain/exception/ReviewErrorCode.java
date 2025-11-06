package com.example.orderjobabom.review.domain.exception;

import com.example.orderjobabom.global.presentation.error.BaseErrorCode;
import com.example.orderjobabom.global.presentation.error.ErrorReasonDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ReviewErrorCode implements BaseErrorCode {

    REVIEW_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "R001", "이미 리뷰가 존재합니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "R002", "리뷰를 찾을 수 없습니다."),
    REVIEW_NOT_EDITABLE(HttpStatus.FORBIDDEN, "R003", "리뷰를 수정할 권한이 없습니다."),
    REVIEW_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "R004", "이미 삭제된 리뷰입니다."),
    REVIEW_ACCESS_DENIED(HttpStatus.FORBIDDEN, "R005", "본인 가게의 리뷰만 관리할 수 있습니다."),
    REVIEW_REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "R006", "신고할 답글이 존재하지 않습니다."),
    REVIEW_REPORT_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "R007", "이미 신고한 리뷰입니다."),
    REVIEW_SELF_REPORT_FORBIDDEN(HttpStatus.FORBIDDEN, "R008", "자신의 리뷰는 신고할 수 없습니다."),
    REPLY_SELF_REPORT_FORBIDDEN(HttpStatus.FORBIDDEN, "R009", "자신의 답글은 신고할 수 없습니다."),
    OWNER_REPLY_REPORT_FORBIDDEN(HttpStatus.FORBIDDEN, "R010", "사장님은 답글을 신고할 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .httpStatus(this.httpStatus)
                .isSuccess(false)
                .code(this.code)
                .message(this.message)
                .build();
    }
}
