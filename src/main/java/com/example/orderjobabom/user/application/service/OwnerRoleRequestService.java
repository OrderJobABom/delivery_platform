package com.example.orderjobabom.user.application.service;

import com.example.orderjobabom.user.infrastructure.keycloak.KeycloakProperties;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OwnerRoleRequestService {

    private final Keycloak keycloak;
    private final KeycloakProperties properties;

    // 사장님 권한 신청
    public void requestOwnerRole(UUID userId) {
        var realm = keycloak.realm(properties.getRealm());
        var userResource = realm.users().get(userId.toString());

        UserRepresentation user = userResource.toRepresentation();

        // 기존 Attribute 가져오고 없으면 새로 생성
        Map<String, List<String>> attributes =
                user.getAttributes() != null ? user.getAttributes() : new HashMap<>();

        // 사장 요청 상태 등록
        attributes.put("owner_request_status", List.of("PENDING"));

        user.setAttributes(attributes);
        userResource.update(user);
    }
}
