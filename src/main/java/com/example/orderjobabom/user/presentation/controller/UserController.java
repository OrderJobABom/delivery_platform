package com.example.orderjobabom.user.presentation.controller;

import com.example.orderjobabom.global.presentation.CustomResponse;
import com.example.orderjobabom.global.presentation.success.GeneralSuccessCode;
import com.example.orderjobabom.user.application.dto.TokenInfo;
import com.example.orderjobabom.user.application.dto.UserRegister;
import com.example.orderjobabom.user.application.dto.UserUpdate;
import com.example.orderjobabom.user.application.service.*;
import com.example.orderjobabom.user.infrastructure.keycloak.KeycloakProperties;
import com.example.orderjobabom.user.presentation.dto.*;
import com.example.orderjobabom.user.presentation.validator.UserRegisterValidator;
import com.example.orderjobabom.user.presentation.validator.UserUpdateValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
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
@RequestMapping("/v1/user")
public class UserController {

    private final UserRegisterService userRegisterService;
    private final UserRegisterValidator userRegisterValidator;
    private final TokenGenerateService tokenGenerateService;
    private final UserUpdateService updateService;
    private final TokenRefreshService tokenRefreshService;
    private final OwnerRoleRequestService ownerRoleRequestService;
    private final ManagerRoleRequestService managerRoleRequestService;
    private final Keycloak keycloak;
    private final KeycloakProperties properties;

    /**
     * 회원가입 API
     */
    @PostMapping("/signup")
    public ResponseEntity<CustomResponse<?>> register(@Valid @RequestBody UserRegisterRequest request) {
        // 1️입력값 검증
        userRegisterValidator.validate(request);

        // 2️서비스용 DTO로 변환
        UserRegister dto = UserRegister.builder()
                .username(request.username())
                .password(request.password())
                .email(request.email())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .phone(request.phone())
                .build();

        // 3️회원가입 처리
        userRegisterService.register(dto);

        // 4️성공 응답
        return ResponseEntity.ok(CustomResponse.onSuccess("회원가입이 완료되었습니다."));
    }

    // 로그인 후 토큰 발급 코드
    @PostMapping("/token")
    public ResponseEntity<CustomResponse<TokenResponse>> generateToken(@Valid @RequestBody TokenRequest request) {
        TokenInfo tokenInfo = tokenGenerateService.generate(request.username(), request.password());

        TokenResponse tokenResponse = new TokenResponse(
                tokenInfo.access_token(),
                tokenInfo.expires_in(),
                tokenInfo.refresh_expires_in(),
                tokenInfo.refresh_token(),
                tokenInfo.token_type()
        );

        return ResponseEntity.ok(CustomResponse.onSuccess(tokenResponse));
    }

    // 리프래쉬 토큰으로 엑세스 토큰 재발급 코드
    @PostMapping("/token/refresh")
    @Operation(summary = "리프레시 토큰으로 액세스 토큰 재발급", description = "유효한 리프레시 토큰을 사용해 새로운 액세스 토큰을 발급합니다.")
    public ResponseEntity<CustomResponse<TokenResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        TokenInfo tokenInfo = tokenRefreshService.refreshAccessToken(request.refreshToken());

        TokenResponse tokenResponse = new TokenResponse(
                tokenInfo.access_token(),
                tokenInfo.expires_in(),
                tokenInfo.refresh_expires_in(),
                tokenInfo.refresh_token(),
                tokenInfo.token_type()
        );

        return ResponseEntity.ok(CustomResponse.onSuccess(tokenResponse));
    }

    // 로그인한 사용자 정보 조회
    @Operation(summary = "로그인한 사용자 정보 조회", description = "Keycloak에서 직접 사용자 정보를 조회합니다.")
    @GetMapping("/profile")
    public ResponseEntity<CustomResponse<UserResponse>> getProfile(@AuthenticationPrincipal Jwt jwt) {

        String userId = jwt.getSubject(); // JWT 안의 sub (Keycloak userId)
        var realm = keycloak.realm(properties.getRealm());
        var user = realm.users().get(userId).toRepresentation(); // 실시간 조회

        String fullName = user.getFirstName() + " " + user.getLastName();
        String phone = user.getAttributes() != null
                ? user.getAttributes().getOrDefault("phone", List.of("")).get(0)
                : "";

        UserResponse userResponse = new UserResponse(
                UUID.fromString(userId),
                user.getUsername(),
                user.getEmail(),
                fullName.trim(),
                phone
        );

        return ResponseEntity.ok(CustomResponse.onSuccess(userResponse));
    }

    // 회원정보 수정
    @PatchMapping("/profile")
    @Operation(summary = "회원 정보 수정", description = "Keycloak 회원 정보를 수정합니다.")
    public ResponseEntity<CustomResponse<?>> updateProfile(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UserUpdateRequest req) {

        new UserUpdateValidator().validateUpdateProfile(req);
        UUID userId = UUID.fromString(jwt.getSubject());

        UserUpdate dto = UserUpdate.builder()
                .email(req.email())
                .firstName(req.firstName())
                .lastName(req.lastName())
                .phone(req.phone())
                .build();

        updateService.update(userId, dto);

        UserResponse userResponse = new UserResponse(
                userId,
                (String) jwt.getClaims().getOrDefault("preferred_username", ""),
                req.email(),
                req.firstName() + req.lastName(),
                req.phone()
        );

        return ResponseEntity.ok(CustomResponse.onSuccess(userResponse));
    }


     // 비밀번호 변경

    @PatchMapping("/password")
    @Operation(summary = "비밀번호 변경", description = "Keycloak 회원의 비밀번호를 변경합니다.")
    public ResponseEntity<CustomResponse<?>> changePassword(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody PasswordChangeRequest req) {

        new UserUpdateValidator().validateChangePassword(req);
        UUID userId = UUID.fromString(jwt.getSubject());

        updateService.updatePassword(userId, req.password());

        return ResponseEntity.ok(
                CustomResponse.onSuccess("비밀번호가 성공적으로 변경되었습니다.")
        );
    }

    // 사장님 권한 요청
    @PatchMapping("/owner/request")
    @PreAuthorize("hasRole('USER') and !hasRole('OWNER') and !hasRole('MANAGER')")
    @Operation(summary = "사장님 권한 요청", description = "현재 로그인한 사용자가 사장님 권한을 요청합니다.")
    public ResponseEntity<CustomResponse<?>> requestOwnerRole(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        ownerRoleRequestService.requestOwnerRole(userId);

        return ResponseEntity.ok(
                CustomResponse.onSuccess(
                        "사장님 권한 요청이 등록되었습니다. 관리자 승인을 기다려주세요."
                )
        );
    }

    @PatchMapping("/manager/request")
    @PreAuthorize("hasRole('USER') and !hasRole('MANAGER') and !hasRole('OWNER')") // 일반 사용자만 요청 가능
    @Operation(summary = "매니저 권한 요청", description = "현재 로그인한 사용자가 매니저 권한을 요청합니다.")
    public ResponseEntity<CustomResponse<?>> requestManagerRole(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        managerRoleRequestService.requestManagerRole(userId);

        return ResponseEntity.ok(
                CustomResponse.onSuccess(
                        "매니저 권한 요청이 등록되었습니다. 관리자 승인을 기다려주세요."
                )
        );
    }


}


