package com.example.orderjobabom.review.domain;

import com.example.orderjobabom.user.domain.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewReportRepository extends JpaRepository<ReviewReport, UUID> {

    /** 특정 유저가 동일 리뷰(또는 답글)를 신고했는지 중복 확인 */
    @Query("""
        SELECT r FROM ReviewReport r
        WHERE r.reviewId = :reviewId
        AND r.reporterId = :reporterId
        AND r.targetType = :targetType
        AND r.status = 'PENDING'
        """)
    Optional<ReviewReport> findExistingReport(ReviewId reviewId, UserId reporterId, TargetType targetType);

    List<ReviewReport> findByStatus(ReportStatus status);
}
