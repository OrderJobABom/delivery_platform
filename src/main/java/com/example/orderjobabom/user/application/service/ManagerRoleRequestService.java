package com.example.orderjobabom.user.application.service;

import com.example.orderjobabom.global.presentation.exception.FailException;
import com.example.orderjobabom.global.presentation.error.GeneralErrorCode;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManagerRoleRequestService {

    private final Keycloak keycloak;

    public void requestManagerRole(UUID userId) {
        try {
            // Keycloak Realm 조회
            var realmResource = keycloak.realm("delivery");
            var userResource = realmResource.users().get(userId.toString());

            // 사용자 정보 확인
            UserRepresentation user = userResource.toRepresentation();
            if (user == null) {
                throw new FailException(GeneralErrorCode.NOT_FOUND_404);
            }

            // 관리자 승인 전까지는 “pending-manager” 상태로 표시
            user.singleAttribute("manager_request_status", "PENDING");
            userResource.update(user);

        } catch (Exception e) {
            throw new FailException(GeneralErrorCode.INTERNAL_SERVER_500);
        }
    }
}
