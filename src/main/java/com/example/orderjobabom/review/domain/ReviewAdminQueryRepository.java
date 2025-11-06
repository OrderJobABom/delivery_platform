package com.example.orderjobabom.review.domain;

import com.example.orderjobabom.review.presentation.dto.AdminReviewResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class ReviewAdminQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<AdminReviewResponse> findAllForAdmin(String reviewerName,
                                                     String storeName,
                                                     ReviewStatus reviewStatus,
                                                     ReportStatus reportStatus,
                                                     Pageable pageable) {

        QReview r = QReview.review;
        QReviewReport rr = QReviewReport.reviewReport;

        BooleanBuilder builder = new BooleanBuilder();

        if (reviewerName != null && !reviewerName.isBlank()) {
            builder.and(r.reviewer.name.containsIgnoreCase(reviewerName));
        }

        if (storeName != null && !storeName.isBlank()) {
            builder.and(r.store.name.containsIgnoreCase(storeName));
        }

        if (reviewStatus != null) {
            builder.and(r.status.eq(reviewStatus));
        }

        // 신고 상태 필터 추가
        if (reportStatus != null) {
            builder.and(rr.status.eq(reportStatus));
        }

        List<AdminReviewResponse> content = queryFactory
                .select(Projections.constructor(AdminReviewResponse.class,
                        r.id.id,
                        r.reviewer.name,
                        r.reviewer.email,
                        r.store.name,
                        r.content,
                        r.reply.content,
                        r.reply.replyer.name,
                        r.reply.replyer.role,
                        r.reply.status,
                        r.status,
                        r.createdAt,
                        r.modifiedAt,
                        // 리뷰 신고 여부
                        new CaseBuilder()
                                .when(rr.targetType.eq(TargetType.REVIEW))
                                .then(true)
                                .otherwise(false),
                        // 리뷰 신고 상태
                        new CaseBuilder()
                                .when(rr.targetType.eq(TargetType.REVIEW))
                                .then(rr.status)
                                .otherwise((ReportStatus) null),
                        // 답글 신고 여부
                        new CaseBuilder()
                                .when(rr.targetType.eq(TargetType.REPLY))
                                .then(true)
                                .otherwise(false),
                        // 답글 신고 상태
                        new CaseBuilder()
                                .when(rr.targetType.eq(TargetType.REPLY))
                                .then(rr.status)
                                .otherwise((ReportStatus) null)
                ))
                .from(r)
                .leftJoin(rr).on(rr.reviewId.id.eq(r.id.id))
                .where(builder)
                .orderBy(r.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(r.countDistinct())
                .from(r)
                .leftJoin(rr).on(rr.reviewId.id.eq(r.id.id))
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
}
