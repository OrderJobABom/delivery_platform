package com.example.orderjobabom.admin.presentation.controller;

import com.example.orderjobabom.admin.application.dto.PendingApprovalResponse;
import com.example.orderjobabom.admin.application.service.ManagerApprovalService;
import com.example.orderjobabom.admin.application.service.OwnerApprovalService;
import com.example.orderjobabom.admin.application.service.RoleApprovalQueryService;
import com.example.orderjobabom.admin.application.service.UserAdminQueryService;
import com.example.orderjobabom.admin.infrastructure.keycloak.dto.KeycloakUserResponse;
import com.example.orderjobabom.global.presentation.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin")
@Tag(name = "관리자(Admin)", description = "관리자 권한 API (승인 / 유저 관리)")
public class AdminController {

    private final RoleApprovalQueryService roleApprovalQueryService;
    private final OwnerApprovalService ownerApprovalService;
    private final ManagerApprovalService managerApprovalService;
    private final UserAdminQueryService userAdminQueryService;

    // 승인 대기중 사용자 목록 조회 코드
    @GetMapping("/approvals/pending")
    @PreAuthorize("hasAnyRole('MASTER', 'MANAGER')")
    @Operation(summary = "승인 대기중 사용자 목록 조회", description = "사장님, 매니저 승인 요청 상태를 조회합니다.")
    public ResponseEntity<CustomResponse<List<PendingApprovalResponse>>> getPendingApprovals() {
        return ResponseEntity.ok(CustomResponse.onSuccess(roleApprovalQueryService.getPendingRequests()));
    }

    // 사장님 권한 승인 코드
    @PatchMapping("/approvals/approve/owner/{userId}")
    @PreAuthorize("hasAnyRole('MASTER', 'MANAGER')")
    @Operation(
            summary = "사장님 권한 승인",
            description = "승인 대기중인 사용자를 사장님으로 등록합니다.\n\n**접근 권한:** MASTER, MANAGER 가능"
    )
    public ResponseEntity<CustomResponse<?>> approveOwner(@PathVariable UUID userId) {
        ownerApprovalService.approve(userId);
        return ResponseEntity.ok(CustomResponse.onSuccess("사장님 권한이 승인되었습니다."));
    }

    // 매니저 권한 승인 코드
    @PatchMapping("/approvals/approve/manager/{userId}")
    @PreAuthorize("hasRole('MASTER')")
    @Operation(
            summary = "매니저 권한 승인",
            description = "승인 대기중인 사용자를 매니저로 등록합니다.\n\n**접근 권한:** MASTER"
    )
    public ResponseEntity<CustomResponse<?>> approveManager(@PathVariable UUID userId) {
        managerApprovalService.approve(userId);
        return ResponseEntity.ok(CustomResponse.onSuccess("매니저 권한이 승인되었습니다."));
    }

    // 매니저 강등 (MASTER 전용)
    @PatchMapping("/approvals/demote/manager/{userId}")
    @PreAuthorize("hasRole('MASTER')")
    @Operation(summary = "매니저 강등", description = "매니저 권한을 회수하고 일반 회원으로 변경합니다.")
    public ResponseEntity<CustomResponse<?>> demoteManager(@PathVariable UUID userId) {
        managerApprovalService.demote(userId);
        return ResponseEntity.ok(CustomResponse.onSuccess("매니저 권한이 회수되어 일반 회원으로 변경되었습니다."));
    }

    // 사장님 강등 (MASTER, MANAGER 전용)
    @PatchMapping("/approvals/demote/owner/{userId}")
    @PreAuthorize("hasAnyRole('MASTER', 'MANAGER')")
    @Operation(summary = "사장님 강등", description = "사장님 권한을 회수하고 일반 회원으로 변경합니다.")
    public ResponseEntity<CustomResponse<?>> demoteOwner(@PathVariable UUID userId) {
        ownerApprovalService.demote(userId);
        return ResponseEntity.ok(CustomResponse.onSuccess("사장님 권한이 회수되어 일반 회원으로 변경되었습니다."));
    }

    // 유저 정보 조회 및 검색
    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('MASTER', 'MANAGER')")
    @Operation(
            summary = "모든 유저 조회 / 검색 / 정렬",
            description = """
                    Keycloak 서버에서 모든 유저 정보를 조회합니다.
                    - **검색(search)**: username, email 기준
                    - **정렬(sort)**: name, email, createdAt (기본: createdAt DESC)
                    - **페이징(page, size)**: page(0부터 시작), size(기본 10)
                    - **role 필터**: ROLE_USER / ROLE_MANAGER / ROLE_MASTER
                    """
    )
    public ResponseEntity<CustomResponse<List<KeycloakUserResponse>>> getAllUsers(
            @AuthenticationPrincipal Jwt jwt, // 현재 로그인한 사용자 토큰
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "createdAt") String sort
    ) {
        String accessToken = jwt.getTokenValue();

        //  현재 로그인한 사용자의 realm role 추출
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        Set<String> roles = Set.of();

        if (realmAccess != null && realmAccess.get("roles") instanceof List<?>) {
            roles = ((List<?>) realmAccess.get("roles")).stream()
                    .filter(o -> o instanceof String)
                    .map(Object::toString)
                    .collect(Collectors.toSet());
        }
        List<KeycloakUserResponse> result =
                userAdminQueryService.getUsers(page, size, search, sort, accessToken, roles);

        return ResponseEntity.ok(CustomResponse.onSuccess(result));
    }
}
