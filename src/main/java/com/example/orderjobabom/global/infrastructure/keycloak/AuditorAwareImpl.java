package com.example.orderjobabom.global.infrastructure.keycloak;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component("auditorAware")
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 로그인 안 되어 있으면 "SYSTEM" 반환
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of("SYSTEM");
        }

        Object principal = authentication.getPrincipal();

        // JWT 기반 인증일 때 (Keycloak + Spring Security 구조)
        if (principal instanceof Jwt jwt) {
            String userId = jwt.getSubject();
            return Optional.ofNullable(userId);
        }

        // 일반적인 username 기반 (혹시 다른 인증 방식일 경우)
        return Optional.ofNullable(authentication.getName());
    }
}
