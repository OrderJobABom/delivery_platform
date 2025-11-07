package com.example.orderjobabom.user.application.service;

import com.example.orderjobabom.user.application.dto.UserUpdate;
import com.example.orderjobabom.user.infrastructure.keycloak.KeycloakProperties;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 회원정보 수정 / 비밀번호 변경 / Role 변경
 * → Keycloak Admin API를 활용
 */
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(KeycloakProperties.class)
public class UserUpdateService {

    private final KeycloakProperties properties;
    private final Keycloak keycloak;

    /**
     * 회원 정보 변경 (이메일, 이름, 전화번호 등)
     */
    public void update(UUID userId, UserUpdate dto) {
        // 현재 사용자 정보 조회
        UserRepresentation user = getUserProfile(userId);

        // 수정 가능한 필드들 갱신
        if (StringUtils.hasText(dto.firstName())) {
            user.setFirstName(dto.firstName());
        }
        if (StringUtils.hasText(dto.lastName())) {
            user.setLastName(dto.lastName());
        }
        if (StringUtils.hasText(dto.email())) {
            user.setEmail(dto.email());
        }

        // attributes (ex. mobile)
        Map<String, List<String>> attributes =
                Objects.requireNonNullElseGet(user.getAttributes(), HashMap::new);

        if (StringUtils.hasText(dto.phone())) {
            attributes.put("phone", List.of(dto.phone()));
        }

        user.setAttributes(attributes);

        // Keycloak에 업데이트 반영
        keycloak.realm(properties.getRealm())
                .users()
                .get(userId.toString())
                .update(user);
    }

    /**
     * 비밀번호 변경
     */
    public void updatePassword(UUID userId, String newPassword) {
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(newPassword);

        keycloak.realm(properties.getRealm())
                .users()
                .get(userId.toString())
                .resetPassword(passwordCred);
    }

    /**
     * Role 변경
     */
    public void updateUserRole(UUID userId, List<String> roleNames) {
        String realm = properties.getRealm();
        RoleScopeResource roleScope = keycloak.realm(realm)
                .users()
                .get(userId.toString())
                .roles()
                .realmLevel();

        // 1️⃣ 기존 Role 제거
        List<RoleRepresentation> existingRoles = roleScope.listAll();
        if (!existingRoles.isEmpty()) {
            roleScope.remove(existingRoles);
        }

        // 2️⃣ 새 Role 등록
        List<RoleRepresentation> newRoles = roleNames.stream()
                .map(roleName -> keycloak.realm(realm).roles().get(roleName).toRepresentation())
                .toList();

        roleScope.add(newRoles);
    }

    /**
     * UUID로 사용자 프로필 조회
     */
    private UserRepresentation getUserProfile(UUID userId) {
        return keycloak.realm(properties.getRealm())
                .users()
                .get(userId.toString())
                .toRepresentation();
    }
}
