package com.example.orderjobabom.global.presentation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        // 프론트 React 실행 주소
                        .allowedOrigins("http://localhost:3000")
                        // 허용할 메서드
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        // 헤더 및 인증 허용
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        // Preflight 결과 캐싱 시간 (초)
                        .maxAge(3600);
            }
        };
    }
}