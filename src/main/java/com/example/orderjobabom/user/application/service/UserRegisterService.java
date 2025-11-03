package com.example.orderjobabom.user.application.service;

import com.example.orderjobabom.global.presentation.exception.FailException;
import com.example.orderjobabom.user.application.dto.UserRegister;
import com.example.orderjobabom.user.infrastructure.keycloak.KeycloakProperties;
import com.example.orderjobabom.user.presentation.error.UserErrorCode;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(KeycloakProperties.class)
public class UserRegisterService {

    private final KeycloakProperties properties;
    private final Keycloak keycloak;

    public void register(UserRegister dto) {
        UsersResource usersResource = keycloak.realm(properties.getRealm()).users();

        // 이미 존재하는 username 체크
        List<UserRepresentation> existingUsers = usersResource.search(dto.username());
        if (!existingUsers.isEmpty()) {
            throw new FailException(UserErrorCode.DUPLICATE_USERNAME);
        }

        // 사용자 생성 정보 설정
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());

        // 사용자 속성 추가
        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("phone", List.of(dto.phone()));
        user.setAttributes(attributes);

        // 사용자 등록 요청
        Response response = usersResource.create(user);

        int status = response.getStatus();
        String body = response.readEntity(String.class);

        System.out.println("Keycloak create user response: " + status + " " + body);

        // 응답 상태별 예외 처리
        if (status == 409 && body.contains("User exists with same email")) {
            throw new FailException(UserErrorCode.DUPLICATE_EMAIL);
        } else if (status == 409 && body.contains("User exists with same username")) {
            throw new FailException(UserErrorCode.DUPLICATE_USERNAME);
        } else if (status != 201) {
            throw new FailException(UserErrorCode.KEYCLOAK_REGISTER_FAIL);
        }

        // 생성된 사용자 ID 조회
        String userId = CreatedResponseUtil.getCreatedId(response);

        // 비밀번호 설정
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(dto.password());
        usersResource.get(userId).resetPassword(passwordCred);

        // 기본 ROLE_USER 부여
        RoleRepresentation userRole = keycloak.realm(properties.getRealm())
                .roles()
                .get("ROLE_USER")
                .toRepresentation();

        usersResource.get(userId)
                .roles()
                .realmLevel()
                .add(List.of(userRole));
    }
}
