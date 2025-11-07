package com.example.orderjobabom.user.application.service;

import com.example.orderjobabom.global.presentation.exception.FailException;
import com.example.orderjobabom.user.application.dto.TokenInfo;
import com.example.orderjobabom.user.infrastructure.keycloak.KeycloakProperties;
import com.example.orderjobabom.user.presentation.error.AuthErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(KeycloakProperties.class)
public class TokenRefreshService {

    private final KeycloakProperties properties;

    public TokenInfo refreshAccessToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new FailException(AuthErrorCode.REFRESH_TOKEN_INVALID);
        }

        RestTemplate restTemplate = new RestTemplate();
        String url = properties.getServerUrl() + "/realms/" + properties.getRealm() + "/protocol/openid-connect/token";

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", refreshToken);
        body.add("client_id", properties.getClientId());
        body.add("client_secret", properties.getClientSecret());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<TokenInfo> response = restTemplate.exchange(url, HttpMethod.POST, entity, TokenInfo.class);
            return response.getBody();

        } catch (HttpClientErrorException e) {
            String responseBody = e.getResponseBodyAsString();

            if (responseBody.contains("invalid_grant")) {
                if (responseBody.contains("expired")) {
                    throw new FailException(AuthErrorCode.REFRESH_TOKEN_EXPIRED);
                } else {
                    throw new FailException(AuthErrorCode.REFRESH_TOKEN_INVALID);
                }
            }

            throw new FailException(AuthErrorCode.LOGIN_REQUIRED);
        }
    }
}
