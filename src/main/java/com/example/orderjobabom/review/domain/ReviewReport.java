package com.example.orderjobabom.review.domain;

import com.example.orderjobabom.user.domain.UserId;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_review_report")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewReport {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TargetType targetType;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "review_id"))
    private ReviewId reviewId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "reporter_id"))
    private UserId reporterId;

    @Column(nullable = false, length = 500)
    private String reason;

    @Column(nullable = false, length = 500)
    private String targetContent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status;

    private LocalDateTime createdAt;

    /** 관리자 처리자 이름 */
    @Column(name = "processed_by")
    private String processedBy;

    /** 관리자 처리 시각 */
    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Builder
    public ReviewReport(TargetType targetType, ReviewId reviewId, UserId reporterId, String reason, String targetContent) {
        this.targetType = targetType;
        this.reviewId = reviewId;
        this.reporterId = reporterId;
        this.reason = reason;
        this.targetContent = targetContent;
        this.status = ReportStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    /** 신고 승인 */
    public void approve(String adminName) {
        this.status = ReportStatus.REVIEWED;
        this.processedBy = adminName;
        this.processedAt = LocalDateTime.now();
    }

    /** 신고 반려 */
    public void reject(String adminName) {
        this.status = ReportStatus.REJECTED;
        this.processedBy = adminName;
        this.processedAt = LocalDateTime.now();
    }

    /** 신고 해제 (승인된 리뷰 복구) */
    public void unblock(String adminName) {
        this.status = ReportStatus.UNBLOCKED;
        this.processedBy = adminName;
        this.processedAt = LocalDateTime.now();
    }
}
