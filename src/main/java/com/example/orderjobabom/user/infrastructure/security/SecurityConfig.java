package com.example.orderjobabom.user.infrastructure.security;

import com.example.orderjobabom.user.infrastructure.keycloak.KeycloakClientRoleConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(prePostEnabled = true) // 메서드 권한(@PreAuthorize) 활성화
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        JwtAuthenticationConverter conv = new JwtAuthenticationConverter();
        conv.setJwtGrantedAuthoritiesConverter(new KeycloakClientRoleConverter());

        http.csrf(c -> c.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/v1/user/profile/**", "/v1/user/password/**", "/v1/user/role/**").hasRole("USER")
                        .requestMatchers("/v1/owner/**").hasRole("OWNER")
                        .anyRequest().permitAll())
                .oauth2Login(c -> c.disable())
                .oauth2ResourceServer(c -> c
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(conv))
                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler()));
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Swagger & Docs 공개 허용
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/api-docs.html"
                        ).permitAll()

                        // 토큰 관련 요청은 인증 불필요 (회원가입, 로그인, 토큰 재발급)
                        .requestMatchers(
                                "/v1/user/signup",
                                "/v1/user/token",
                                "/v1/user/token/refresh"
                        ).permitAll()

                        .requestMatchers("/v1/reviews/**").authenticated()

                        // 사용자 보호 API — 인증(토큰) 필요
                        .requestMatchers(
                                "/v1/user/profile/**",
                                "/v1/user/password/**",
                                "/v1/user/owner/**"
                        ).authenticated()

                        // 관리자 API (승인 관련)
                        .requestMatchers("/v1/admin/approvals/pending").hasRole("MASTER")

                        // 사장님 승인/강등 — MASTER, MANAGER 둘 다 가능
                        .requestMatchers(
                                "/v1/admin/approvals/approve/owner/**",
                                "/v1/admin/approvals/demote/owner/**"
                        ).hasAnyRole("MASTER", "MANAGER")

                        // 매니저 승인/강등 — MASTER만 가능
                        .requestMatchers(
                                "/v1/admin/approvals/approve/manager/**",
                                "/v1/admin/approvals/demote/manager/**"
                        ).hasRole("MASTER")

                        // 유저 조회 — MASTER, MANAGER 둘 다 가능
                        .requestMatchers("/v1/admin/users/**").hasAnyRole("MASTER", "MANAGER")

                        // 나머지는 임시로 허용
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth2 -> oauth2.disable())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(conv))
                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler()));

        return http.build();
    }
}