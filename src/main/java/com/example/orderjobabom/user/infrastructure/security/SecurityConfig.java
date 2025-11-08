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
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        JwtAuthenticationConverter conv = new JwtAuthenticationConverter();
        conv.setJwtGrantedAuthoritiesConverter(new KeycloakClientRoleConverter());

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Swagger는 로그인 안 해도 접근 가능하게 (문서 열람용)
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // 회원가입/토큰 관련은 공개
                        .requestMatchers(
                                "/v1/user/signup",
                                "/v1/user/token",
                                "/v1/user/token/refresh",
                                "/v1/gemini/generate"
                        ).permitAll()

                        // 보호된 API
                        .requestMatchers(
                                "/v1/user/profile/**",
                                "/v1/user/password/**",
                                "/v1/user/owner/**",
                                "/v1/reviews/**"
                        ).authenticated()

                        // 관리자 권한별 접근 제어
                        .requestMatchers("/v1/admin/approvals/pending").hasAnyRole("MASTER", "MANAGER")
                        .requestMatchers(
                                "/v1/admin/approvals/approve/owner/**",
                                "/v1/admin/approvals/demote/owner/**"
                        ).hasAnyRole("MASTER", "MANAGER")
                        .requestMatchers(
                                "/v1/admin/approvals/approve/manager/**",
                                "/v1/admin/approvals/demote/manager/**"
                        ).hasRole("MASTER")
                        .requestMatchers("/v1/admin/users/**").hasAnyRole("MASTER", "MANAGER")

                        // 나머지 임시 허용
                        .anyRequest().permitAll()
                )

                // 로그인 성공 시 Swagger UI로 이동
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/swagger-ui/index.html", true)
                )

                // JWT 리소스 서버 설정 (API용 Bearer 토큰 인증)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(conv))
                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
                );

        return http.build();
    }
}
