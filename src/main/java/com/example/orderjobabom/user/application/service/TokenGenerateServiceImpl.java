package com.example.orderjobabom.user.application.service;

import com.example.orderjobabom.user.application.dto.TokenInfo;
import com.example.orderjobabom.user.infrastructure.keycloak.KeycloakProperties;
import com.example.orderjobabom.user.presentation.error.UserErrorCode;
import com.example.orderjobabom.global.presentation.exception.FailException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(KeycloakProperties.class)
public class TokenGenerateServiceImpl implements TokenGenerateService {

    private final KeycloakProperties properties;

    @Override
    public TokenInfo generate(String username, String password) {
        String tokenUrl = properties.getServerUrl()
                + "/realms/" + properties.getRealm()
                + "/protocol/openid-connect/token";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", properties.getClientId());
        params.add("client_secret", properties.getClientSecret());
        params.add("username", username);
        params.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<TokenInfo> response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                request,
                TokenInfo.class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new FailException(UserErrorCode.KEYCLOAK_REGISTER_FAIL);
        }

        return response.getBody();
    }
}
