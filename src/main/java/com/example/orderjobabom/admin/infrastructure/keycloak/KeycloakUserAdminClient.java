package com.example.orderjobabom.admin.infrastructure.keycloak;

import com.example.orderjobabom.admin.infrastructure.keycloak.dto.KeycloakUserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Keycloak Admin REST API를 호출하여 유저 목록과 권한 정보를 가져오는 클라이언트
 */

@Component
public class KeycloakUserAdminClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${keycloak.server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    /**
     * Keycloak에서 전체 유저 목록을 조회 (페이징 및 검색 지원)
     * @param first 시작 인덱스
     * @param max 가져올 개수
     * @param search 검색어 (username, email 등)
     * @param accessToken 현재 로그인한 관리자 Access Token
     */
    public List<KeycloakUserResponse> getUsers(int first, int max, String search, String accessToken) {
        String url = keycloakServerUrl + "/admin/realms/" + realm + "/users?first=" + first + "&max=" + max;
        if (search != null && !search.isBlank()) {
            url += "&search=" + search;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken); // 토큰을 파라미터로 받아 헤더에 설정
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<KeycloakUserResponse[]> response =
                restTemplate.exchange(url, HttpMethod.GET, entity, KeycloakUserResponse[].class);

        return Arrays.asList(response.getBody());
    }

    /**
     * 특정 유저의 Realm Role 목록 조회
     * @param userId Keycloak User ID
     * @param accessToken 현재 로그인한 관리자 Access Token
     */
    public List<String> getUserRoles(String userId, String accessToken) {
        String url = keycloakServerUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/realm";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);

        List<?> body = response.getBody();
        if (body == null) return List.of();

        // LinkedHashMap 구조에서 "name" 필드만 추출
        return body.stream()
                .filter(item -> item instanceof Map)
                .map(item -> ((Map<?, ?>) item).get("name"))
                .filter(Objects::nonNull)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

}
