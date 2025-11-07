package com.example.orderjobabom.review.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Reply {

    @Embedded
    private Replyer replyer;

    @Column(name = "reply_content", length = 500)
    private String content;

    @Column(name = "reply_created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "reply_status", nullable = true)
    private ReplyStatus status = ReplyStatus.ACTIVE;

    public static Reply of(Replyer replyer, String content) {
        return new Reply(replyer, content, LocalDateTime.now(), ReplyStatus.ACTIVE);
    }

    /** 삭제 처리 */
    public void markAsDeleted() {
        this.status = ReplyStatus.DELETED;
    }

    /** 관리자 신고 정지 처리 */
    public void markAsBlocked() {
        this.status = ReplyStatus.BLOCKED;
    }

    /** 상태 체크 메서드 추가 */
    public boolean isDeleted() {
        return this.status == ReplyStatus.DELETED;
    }


    public boolean isActive() {
        return this.status == ReplyStatus.ACTIVE;
    }

    public void block() {
        this.status = ReplyStatus.BLOCKED;
    }

    public void unblock() {
        this.status = ReplyStatus.ACTIVE;
    }

}
