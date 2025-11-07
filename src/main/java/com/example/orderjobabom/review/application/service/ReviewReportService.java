package com.example.orderjobabom.review.application.service;

import com.example.orderjobabom.review.domain.*;
import com.example.orderjobabom.review.domain.exception.*;
import com.example.orderjobabom.store.domain.Store;
import com.example.orderjobabom.store.domain.StoreId;
import com.example.orderjobabom.store.domain.StoreRepository;
import com.example.orderjobabom.user.domain.UserId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewReportService {

    private final ReviewRepository reviewRepository;
    private final ReviewReportRepository reportRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public void report(UUID reviewUuid, UUID reporterUuid, String reason, TargetType targetType) {
        // 리뷰 존재 확인
        Review review = reviewRepository.findById(new ReviewId(reviewUuid))
                .orElseThrow(ReviewNotFoundException::new);

        UserId reporterId = new UserId(reporterUuid);
        boolean isOwner = storeRepository.findByOwnerId(reporterId).isPresent();

        // 중복 신고 방지
        if (reportRepository.findExistingReport(review.getId(), reporterId, targetType).isPresent()) {
            throw new ReportAlreadyExistsException();
        }

        // 신고 대상 검증
        if (targetType == TargetType.REVIEW) {
            // 자신이 쓴 리뷰 신고 금지
            if (review.getReviewer().getId().equals(reporterId)) {
                throw new ReviewSelfReportForbiddenException();
            }
        }

        if (targetType == TargetType.REPLY) {
            if (review.getReply() == null || review.getReply().isDeleted()) {
                throw new ReplyNotFoundException();
            }

            // 사장님은 답글 신고 불가
            if (isOwner) {
                throw new OwnerReplyReportForbiddenException();
            }

            // 사장님이 자기 답글 신고 불가
            Store store = storeRepository.findById(StoreId.of(review.getStore().getId()))
                    .orElseThrow(() -> new IllegalStateException("가게 정보를 찾을 수 없습니다."));

            if (store.getOwner().getId().getId().equals(reporterId.getId())) {
                throw new ReplySelfReportForbiddenException();
            }
        }

        // 신고 내용 저장
        String targetContent = (targetType == TargetType.REPLY)
                ? review.getReply().getContent()
                : review.getContent();

        ReviewReport report = ReviewReport.builder()
                .targetType(targetType)
                .reviewId(review.getId())
                .reporterId(reporterId)
                .reason(reason)
                .targetContent(targetContent)
                .build();

        reportRepository.save(report);
    }
}
