package com.example.orderjobabom.user.presentation.controller;

import com.example.orderjobabom.global.presentation.CustomResponse;
import com.example.orderjobabom.global.presentation.success.GeneralSuccessCode;
import com.example.orderjobabom.user.application.dto.TokenInfo;
import com.example.orderjobabom.user.application.dto.UserRegister;
import com.example.orderjobabom.user.application.dto.UserUpdate;
import com.example.orderjobabom.user.application.service.TokenGenerateService;
import com.example.orderjobabom.user.application.service.UserRegisterService;
import com.example.orderjobabom.user.application.service.UserUpdateService;
import com.example.orderjobabom.user.presentation.dto.*;
import com.example.orderjobabom.user.presentation.validator.UserRegisterValidator;
import com.example.orderjobabom.user.presentation.validator.UserUpdateValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

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

    // 로그인한 사용자 정보 조회
    @Operation(summary = "로그인한 사용자 정보 조회", description = "JWT 토큰을 기반으로 사용자 정보를 조회합니다.")
    @GetMapping(value = "/profile", produces = "application/json")
    @Parameter(
            name = "Authorization",
            description = "Bearer 토큰 인증 헤더",
            example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
            in = ParameterIn.HEADER,
            schema = @Schema(format = "string")
    )
    public ResponseEntity<CustomResponse<UserResponse>> getProfile(@AuthenticationPrincipal Jwt jwt) {

        UUID userId = UUID.fromString(jwt.getSubject());
        Map<String, Object> claims = jwt.getClaims();

        String name = (String) claims.getOrDefault("family_name", "")
                + (String) claims.getOrDefault("given_name", "");

        UserResponse userResponse = new UserResponse(
                userId,
                (String) claims.getOrDefault("preferred_username", ""),
                (String) claims.getOrDefault("email", ""),
                name,
                (String) claims.getOrDefault("mobile", "")
        );

        return ResponseEntity.ok(
                CustomResponse.onSuccess(
                        userResponse
                )
        );
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
                .mobile(req.mobile())
                .build();

        updateService.update(userId, dto);

        UserResponse userResponse = new UserResponse(
                userId,
                (String) jwt.getClaims().getOrDefault("preferred_username", ""),
                req.email(),
                req.firstName() + req.lastName(),
                req.mobile()
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

        UserResponse userResponse = new UserResponse(
                userId,
                (String) jwt.getClaims().getOrDefault("preferred_username", ""),
                (String) jwt.getClaims().getOrDefault("email", ""),
                (String) jwt.getClaims().getOrDefault("family_name", "")
                        + (String) jwt.getClaims().getOrDefault("given_name", ""),
                (String) jwt.getClaims().getOrDefault("mobile", "")
        );

        return ResponseEntity.ok(CustomResponse.onSuccess(userResponse));
    }
}


