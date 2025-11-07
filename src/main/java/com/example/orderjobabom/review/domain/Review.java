package com.example.orderjobabom.review.domain;

import com.example.orderjobabom.global.presentation.BaseUserEntity;
import com.example.orderjobabom.order.domain.OrderId;
import com.example.orderjobabom.review.domain.exception.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Table(
        name = "p_review",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"order_id"})
        }
)
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseUserEntity {

    @EmbeddedId
    @AttributeOverride(name = "id", column = @Column(name = "id"))
    private ReviewId id;

    /** 주문 ID (필수) */
    @AttributeOverride(name = "id", column = @Column(name = "order_id", nullable = false))
    @Embedded
    private OrderId orderId;

    /** 리뷰 작성자 스냅샷 */
    @Embedded
    private Reviewer reviewer;

    /** 가게 스냅샷 */
    @Embedded
    private StoreSummary store;

    /** 별점 (1~5, 필수) */
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "value", nullable = false))
    private Rating rating;

    /** 리뷰 본문 (선택) */
    @Column(nullable = true, length = 500)
    private String content;

    /** 리뷰 상태 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewStatus status;

    /** 사장님 답글 (선택) */
    @Embedded
    private Reply reply;

    @Builder
    public Review(OrderId orderId, Reviewer reviewer, StoreSummary store,
                  Rating rating, String content, ReviewId id) {
        this.id = Objects.requireNonNullElse(id, ReviewId.of());
        this.orderId = Objects.requireNonNull(orderId, "orderId는 필수입니다.");
        this.reviewer = Objects.requireNonNull(reviewer, "reviewer는 필수입니다.");
        this.store = Objects.requireNonNull(store, "store는 필수입니다.");
        this.rating = Objects.requireNonNull(rating, "rating은 필수입니다.");
        this.content = content;
        this.status = ReviewStatus.ACTIVE;
    }
    // ====== 도메인 행동(Behavior) ======

    /** 사장님 답글 작성 */
    public void addReply(Reply reply) {
        if (this.reply != null && this.reply.getContent() != null) {

            // 상태 기반 예외 처리
            switch (this.reply.getStatus()) {
                case ACTIVE -> throw new ReviewAlreadyExistsException();   // 이미 존재하는 답글
                case DELETED -> throw new ReviewAlreadyDeletedException(); // 삭제된 답글
                case BLOCKED -> throw new ReviewAccessDeniedException();   // 관리자 차단
            }
        }
        this.reply = reply;
    }

    /** 사장님 답글 수정 */
    public void updateReply(Reply reply) {
        if (this.reply == null) {
            throw new ReplyNotFoundException(); // 수정할 답글 없음
        }

        // 차단된 답글은 수정 불가
        if (this.reply.getStatus() == ReplyStatus.BLOCKED) {
            throw new ReviewAccessDeniedException();
        }

        this.reply = reply;
    }

    /** 사장님 답글 삭제 */
    public void removeReply() {
        if (this.reply == null) {
            throw new ReplyNotFoundException();
        }

        if (this.reply.getStatus() == ReplyStatus.DELETED) {
            throw new ReviewAlreadyDeletedException();
        }

        this.reply.markAsDeleted();
    }

    /** 리뷰 내용 수정 */
    public void updateContent(String content, Rating rating) {
        if (this.status == ReviewStatus.DELETED) {
            throw new ReviewAlreadyDeletedException();
        }

        this.content = content;
        this.rating = rating;
    }

    /** 리뷰 삭제 (Soft Delete) */
    public void delete() {
        if (this.status == ReviewStatus.DELETED) {
            throw new ReviewAlreadyDeletedException();
        }

        this.status = ReviewStatus.DELETED;
        this.deletedAt = LocalDateTime.now();
    }

    public void block() {
        this.status = ReviewStatus.BLOCKED;
    }

    public void unblock() {
        this.status = ReviewStatus.ACTIVE;
    }

    public void blockReply() {
        if (this.reply != null) {
            this.reply.block();
        }
    }

    public void unblockReply() {
        if (this.reply != null) {
            this.reply.unblock();
        }
    }

    public void deleteByAdmin(String adminName) {
        if (this.status == ReviewStatus.DELETED) {
            throw new IllegalStateException("이미 삭제된 리뷰입니다.");
        }
        this.status = ReviewStatus.HIDDEN; // 관리자에 의해 숨김 처리
        this.modifiedBy = adminName;
        this.modifiedAt = LocalDateTime.now();
    }

    public void deleteReplyByAdmin(String adminName) {
        if (this.reply != null && this.reply.getStatus() != ReplyStatus.DELETED) {
            this.reply.markAsDeleted();
            this.modifiedBy = adminName;
            this.modifiedAt = LocalDateTime.now();
        }
    }

}
