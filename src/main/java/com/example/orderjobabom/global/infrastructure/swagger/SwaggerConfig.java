package com.example.orderjobabom.global.infrastructure.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    // 회원 API
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("user-api")
                .displayName("회원 API")
                .pathsToMatch("/v1/user/**")
                .pathsToExclude("/v1/orders/**", "/v1/admin/**", "/v1/reviews/**",
                        "/v1/payments/**", "/v1/storeItem/**")
                .build();
    }

    // 관리자 API
    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("admin-api")
                .displayName("관리자 API")
                .pathsToMatch("/v1/admin/**")
                .pathsToExclude("/v1/user/**", "/v1/orders/**", "/v1/reviews/**",
                        "/v1/payments/**", "/v1/storeItem/**")
                .build();
    }

    // 주문 API
    @Bean
    public GroupedOpenApi orderApi() {
        return GroupedOpenApi.builder()
                .group("order-api")
                .displayName("주문 API")
                .pathsToMatch("/v1/orders/**")
                .pathsToExclude("/v1/user/**", "/v1/admin/**", "/v1/reviews/**",
                        "/v1/payments/**", "/v1/storeItem/**")
                .build();
    }

    // 리뷰 API
    @Bean
    public GroupedOpenApi reviewApi() {
        return GroupedOpenApi.builder()
                .group("review-api")
                .displayName("리뷰 API")
                .pathsToMatch("/v1/reviews/**")
                .pathsToExclude("/v1/user/**", "/v1/orders/**", "/v1/admin/**",
                        "/v1/payments/**", "/v1/storeItem/**")
                .build();
    }

    // 결제 API
    @Bean
    public GroupedOpenApi paymentApi() {
        return GroupedOpenApi.builder()
                .group("payment-api")
                .displayName("결제 API")
                .pathsToMatch("/v1/payments/**")
                .pathsToExclude("/v1/user/**", "/v1/orders/**", "/v1/admin/**",
                        "/v1/reviews/**", "/v1/storeItem/**")
                .build();
    }

    // 매장 API (새로 추가)
    @Bean
    public GroupedOpenApi storeApi() {
        return GroupedOpenApi.builder()
                .group("store-api")
                .displayName("매장 API")
                .pathsToMatch("/v1/storeItem/**")
                .pathsToExclude("/v1/user/**", "/v1/orders/**", "/v1/admin/**",
                        "/v1/reviews/**", "/v1/payments/**")
                .build();
    }

    // 공통 OpenAPI 설정 (JWT 보안 등)
    @Bean
    public OpenAPI openAPI() {
        SecurityScheme bearerAuth = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("BearerAuth", bearerAuth))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .info(new Info()
                        .title("배달 서비스 REST API")
                        .description("JWT 기반 인증 API 문서")
                        .version("1.0"));
    }
}
