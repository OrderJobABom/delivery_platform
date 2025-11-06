package com.example.orderjobabom.review.presentation.controller;

import com.example.orderjobabom.global.presentation.CustomResponse;
import com.example.orderjobabom.order.domain.OrderId;
import com.example.orderjobabom.review.application.service.*;
import com.example.orderjobabom.review.domain.*;
import com.example.orderjobabom.review.presentation.dto.*;
import com.example.orderjobabom.store.domain.Store;
import com.example.orderjobabom.store.domain.StoreId;
import com.example.orderjobabom.store.domain.StoreRepository;
import com.example.orderjobabom.user.domain.UserId;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/reviews")
public class ReviewController {

    private final ReviewCreateService createService;
    private final ReviewUpdateService reviewUpdateService;
    private final ReviewDeleteService reviewDeleteService;
    private final ReviewQueryService reviewQueryService;
    private final ReviewOwnerService reviewOwnerService;
    private final ReviewReportService reviewReportService;
    private final StoreRepository storeRepository;
    private final ReviewAdminQueryService reviewAdminQueryService;
    private final ReviewReportManageService manageService;

    // 리뷰 생성
    @Operation(summary = "리뷰 생성", tags = {"Review - User"})
    @PreAuthorize("@securityRoleUtil.isPureUser(authentication)")
    @PostMapping
    public ResponseEntity<CustomResponse<?>> createReview(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody ReviewRequest request) {

        // JWT → Reviewer 생성
        Reviewer reviewer = Reviewer.of(
                UUID.fromString(jwt.getSubject()),
                jwt.getClaimAsString("preferred_username"),
                jwt.getClaimAsString("email")
        );

        // StoreSummary 생성 (storeId로 조회)
        Store store = storeRepository.findById(new StoreId(request.storeId()))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매장입니다."));
        StoreSummary storeSummary = StoreSummary.of(store.getId().getId(), store.getStoreName());

        // 리뷰 생성 서비스 호출
        ReviewId reviewId = createService.create(
                new OrderId(request.orderId()),
                reviewer.getId(),
                reviewer.getName(),
                reviewer.getEmail(),
                new StoreId(request.storeId()),
                new Rating(request.rating()),
                request.content()
        );

        return ResponseEntity.ok(
                CustomResponse.onSuccess("리뷰가 등록되었습니다.")
        );
    }

    // 리뷰 수정
    @Operation(summary = "리뷰 수정", tags = {"Review - User"})
    @PreAuthorize("@securityRoleUtil.isPureUser(authentication)")
    @PatchMapping("/{reviewId}")
    public ResponseEntity<CustomResponse<?>> updateReview(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID reviewId,
            @RequestBody ReviewUpdateRequest request) {

        UUID userId = UUID.fromString(jwt.getSubject());
        reviewUpdateService.update(reviewId, userId, request);
        return ResponseEntity.ok(CustomResponse.onSuccess("리뷰가 수정되었습니다."));
    }

    // 리뷰 삭제
    @Operation(summary = "리뷰 삭제", tags = {"Review - User"})
    @PreAuthorize("@securityRoleUtil.isPureUser(authentication)")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<CustomResponse<?>> deleteReview(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID reviewId) {

        UUID userId = UUID.fromString(jwt.getSubject());
        reviewDeleteService.delete(reviewId, userId);
        return ResponseEntity.ok(CustomResponse.onSuccess("리뷰가 삭제되었습니다."));
    }

    // 내가 작성한 리뷰 목록 조회
    @Operation(summary = "내가 작성한 리뷰 조회", tags = {"Review - User"})
    @PreAuthorize("@securityRoleUtil.isPureUser(authentication)")
    @GetMapping("/me")
    public ResponseEntity<CustomResponse<List<ReviewResponse>>> getMyReviews(
            @AuthenticationPrincipal Jwt jwt) {

        UUID userId = UUID.fromString(jwt.getSubject());
        List<ReviewResponse> myReviews = reviewQueryService.findMyReviews(userId);

        return ResponseEntity.ok(
                CustomResponse.onSuccess("리뷰를 불러왔습니다.", myReviews)
        );
    }

    // 사장님 관련 코드
    /** 내 가게 리뷰 전체 조회 */
    @Operation(summary = "내 가게 리뷰 전체 조회", tags = {"Review - Owner"})
    @PreAuthorize("@securityRoleUtil.isOwner(authentication)")
    @GetMapping("/owner")
    public ResponseEntity<CustomResponse<List<ReviewOwnerResponse>>> getMyStoreReviews(
            @AuthenticationPrincipal Jwt jwt) {

        UUID ownerId = UUID.fromString(jwt.getSubject());
        List<ReviewOwnerResponse> reviews =
                reviewOwnerService.getReviewsByOwner(new UserId(ownerId));

        return ResponseEntity.ok(
                CustomResponse.onSuccess("내 가게 리뷰 목록을 불러왔습니다.", reviews)
        );
    }

    /** 내가 작성한 답글(있는 리뷰만) 조회 */
    @Operation(summary = "답글이 달린 리뷰 조회", tags = {"Review - Owner"})
    @PreAuthorize("@securityRoleUtil.isOwner(authentication)")
    @GetMapping("/owner/replies")
    public ResponseEntity<CustomResponse<List<ReviewOwnerResponse>>> getMyRepliedReviews(
            @AuthenticationPrincipal Jwt jwt) {

        UUID ownerId = UUID.fromString(jwt.getSubject());
        List<ReviewOwnerResponse> replies =
                reviewOwnerService.getRepliedReviewsByOwner(new UserId(ownerId));

        return ResponseEntity.ok(
                CustomResponse.onSuccess("답글이 달린 리뷰만 불러왔습니다.", replies)
        );
    }

    /** 답글 작성 */
    @Operation(summary = "답글 작성", tags = {"Review - Owner"})
    @PreAuthorize("@securityRoleUtil.isOwner(authentication)")
    @PostMapping("/owner/{reviewId}/reply")
    public ResponseEntity<CustomResponse<?>> addReply(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID reviewId,
            @RequestBody ReviewReplyRequest request) {

        UUID ownerUuid = UUID.fromString(jwt.getSubject());
        String ownerName = jwt.getClaimAsString("preferred_username");
        String ownerRole = extractRoleFromJwt(jwt);

        reviewOwnerService.addReply(
                reviewId,
                ownerUuid,
                ownerName,
                ownerRole,
                request.content()
        );

        return ResponseEntity.ok(CustomResponse.onSuccess("답글이 등록되었습니다."));
    }

    /** 답글 수정 */
    @Operation(summary = "답글 수정", tags = {"Review - Owner"})
    @PreAuthorize("@securityRoleUtil.isOwner(authentication)")
    @PatchMapping("/owner/{reviewId}/reply")
    public ResponseEntity<CustomResponse<?>> updateReply(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID reviewId,
            @RequestBody ReviewReplyRequest request) {

        UUID ownerUuid = UUID.fromString(jwt.getSubject());
        String ownerName = jwt.getClaimAsString("preferred_username");
        String ownerRole = extractRoleFromJwt(jwt);

        reviewOwnerService.updateReply(
                reviewId,
                ownerUuid,
                ownerName,
                ownerRole,
                request.content()
        );

        return ResponseEntity.ok(CustomResponse.onSuccess("답글이 수정되었습니다."));
    }

    /** 답글 삭제 */
    @Operation(summary = "답글 삭제", tags = {"Review - Owner"})
    @PreAuthorize("@securityRoleUtil.isOwner(authentication)")
    @DeleteMapping("/owner/{reviewId}/reply")
    public ResponseEntity<CustomResponse<?>> deleteReply(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID reviewId) {

        UUID ownerId = UUID.fromString(jwt.getSubject());
        reviewOwnerService.deleteReply(reviewId, new UserId(ownerId));

        return ResponseEntity.ok(
                CustomResponse.onSuccess("답글이 삭제되었습니다.")
        );
    }

    /** 리뷰 or 답글 신고 */
    @Operation(summary = "리뷰 및 답글 신고", tags = {"Review - Owner, User"})
    @PreAuthorize("@securityRoleUtil.isPureUser(authentication) or @securityRoleUtil.isOwner(authentication)")
    @PostMapping("/{reviewId}/report")
    public ResponseEntity<CustomResponse<?>> reportReview(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID reviewId,
            @RequestBody ReviewReportRequest request
    ) {
        UUID reporterId = UUID.fromString(jwt.getSubject());
        reviewReportService.report(reviewId, reporterId, request.reason(), request.targetType());
        return ResponseEntity.ok(CustomResponse.onSuccess("신고가 접수되었습니다."));
    }


    @Operation(summary = "관리자 - 전체 리뷰 조회", description = "유저명, 가게명, 상태별, 신고 상태별, 최신순, 페이지네이션 조회", tags = {"Review - Manager, Master"})
    @PreAuthorize("@securityRoleUtil.isMaster(authentication) or @securityRoleUtil.isManager(authentication)")
    @GetMapping("/admin/view")
    public ResponseEntity<CustomResponse<Page<AdminReviewResponse>>> getReviews(
            @RequestParam(required = false) String reviewerName,
            @RequestParam(required = false) String storeName,
            @RequestParam(required = false) ReviewStatus status,
            @RequestParam(required = false) ReportStatus reportStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminReviewResponse> reviews =
                reviewAdminQueryService.getReviews(reviewerName, storeName, status, reportStatus, pageable);

        return ResponseEntity.ok(CustomResponse.onSuccess("리뷰 목록을 불러왔습니다.", reviews));
    }

    @Operation(summary = "신고 목록 조회 (대기중)", tags = {"Review - Manager, Master"})
    @PreAuthorize("@securityRoleUtil.isManager(authentication) or @securityRoleUtil.isMaster(authentication)")
    @GetMapping("/admin/reports")
    public ResponseEntity<CustomResponse<List<ReviewReport>>> getPendingReports() {
        return ResponseEntity.ok(CustomResponse.onSuccess("신고 목록을 불러왔습니다.", manageService.getPendingReports()));
    }

    @Operation(summary = "신고 승인", tags = {"Review - Manager, Master"})
    @PreAuthorize("@securityRoleUtil.isManager(authentication) or @securityRoleUtil.isMaster(authentication)")
    @PatchMapping("/admin/reports/{reportId}/approve")
    public ResponseEntity<CustomResponse<?>> approveReport(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID reportId) {

        String adminName = jwt.getClaimAsString("preferred_username");
        manageService.approveReport(reportId, adminName);
        return ResponseEntity.ok(CustomResponse.onSuccess("신고가 승인되었습니다."));
    }

    @Operation(summary = "신고 반려", tags = {"Review - Manager, Master"})
    @PreAuthorize("@securityRoleUtil.isManager(authentication) or @securityRoleUtil.isMaster(authentication)")
    @PatchMapping("/admin/reports/{reportId}/reject")
    public ResponseEntity<CustomResponse<?>> rejectReport(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID reportId) {

        String adminName = jwt.getClaimAsString("preferred_username");
        manageService.rejectReport(reportId, adminName);
        return ResponseEntity.ok(CustomResponse.onSuccess("신고가 반려되었습니다."));
    }

    @Operation(summary = "신고 해제 (복구)", tags = {"Review - Manager, Master"})
    @PreAuthorize("@securityRoleUtil.isManager(authentication) or @securityRoleUtil.isMaster(authentication)")
    @PatchMapping("/admin/reports/{reportId}/unblock")
    public ResponseEntity<CustomResponse<?>> unblockReport(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID reportId) {

        String adminName = jwt.getClaimAsString("preferred_username");
        manageService.unblockReport(reportId, adminName);
        return ResponseEntity.ok(CustomResponse.onSuccess("신고가 해제되었습니다."));
    }

    // 관리자 리뷰/답글 삭제

    @Operation(summary = "관리자 리뷰/답글 삭제", description = "관리자 또는 매니저가 특정 리뷰 또는 답글을 삭제합니다.", tags = {"Review - Manager, Master"})
    @PreAuthorize("@securityRoleUtil.isMaster(authentication) or @securityRoleUtil.isManager(authentication)")
    @DeleteMapping("/admin/{reviewId}/delete")
    public ResponseEntity<CustomResponse<?>> deleteReviewOrReply(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID reviewId,
            @RequestParam TargetType targetType
    ) {
        String adminName = jwt.getClaimAsString("preferred_username");

        reviewAdminQueryService.deleteReviewOrReply(reviewId, targetType, adminName);

        return ResponseEntity.ok(CustomResponse.onSuccess("삭제가 완료되었습니다."));
    }

    /** keycloak JWT에서 역할 추출 */
    private String extractRoleFromJwt(Jwt jwt) {
        Object realmAccess = jwt.getClaims().get("realm_access");
        if (realmAccess instanceof Map<?, ?> realmMap) {
            Object roles = realmMap.get("roles");
            if (roles instanceof List<?> roleList) {
                for (Object role : roleList) {
                    String roleStr = role.toString();
                    if (roleStr.startsWith("ROLE_")) {
                        return roleStr.replace("ROLE_", ""); // ROLE_OWNER → OWNER
                    }
                }
            }
        }
        return "UNKNOWN";
    }

}
