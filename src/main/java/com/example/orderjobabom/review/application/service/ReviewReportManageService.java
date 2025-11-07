package com.example.orderjobabom.review.application.service;

import com.example.orderjobabom.review.domain.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewReportManageService {

    private final ReviewReportRepository reviewReportRepository;
    private final ReviewRepository reviewRepository;

    /** 신고 목록 조회 (대기중 상태만) */
    public List<ReviewReport> getPendingReports() {
        return reviewReportRepository.findByStatus(ReportStatus.PENDING);
    }

    /** 신고 승인 */
    public void approveReport(UUID reportId, String adminName) {
        ReviewReport report = reviewReportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("신고 내역을 찾을 수 없습니다."));

        report.approve(adminName);

        // 리뷰나 답글을 블락 처리
        if (report.getTargetType() == TargetType.REVIEW) {
            Review review = reviewRepository.findById(report.getReviewId())
                    .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));
            review.block();
        } else if (report.getTargetType() == TargetType.REPLY) {
            Review review = reviewRepository.findById(report.getReviewId())
                    .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));
            review.blockReply();
        }
    }

    /** 신고 반려 */
    public void rejectReport(UUID reportId, String adminName) {
        ReviewReport report = reviewReportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("신고 내역을 찾을 수 없습니다."));
        report.reject(adminName);
    }

    /** 신고 해제 (승인된 리뷰 복구) */
    public void unblockReport(UUID reportId, String adminName) {
        ReviewReport report = reviewReportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("신고 내역을 찾을 수 없습니다."));

        if (report.getStatus() != ReportStatus.REVIEWED) {
            throw new IllegalStateException("승인된 신고만 해제할 수 있습니다.");
        }

        // 상태 변경
        report.unblock(adminName);

        // 리뷰 복구
        Review review = reviewRepository.findById(report.getReviewId())
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        if (report.getTargetType() == TargetType.REVIEW) {
            review.unblock(); // 리뷰 복구 (BLOCKED → ACTIVE)
        } else if (report.getTargetType() == TargetType.REPLY) {
            review.unblockReply(); // 답글 복구 (BLOCKED → ACTIVE)
        }
    }
}
