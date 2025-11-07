package com.example.orderjobabom.admin.infrastructure.keycloak;

import com.example.orderjobabom.admin.domain.model.RoleRequest;
import com.example.orderjobabom.admin.domain.repository.RoleRequestRepository;
import com.example.orderjobabom.user.infrastructure.keycloak.KeycloakProperties;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class KeycloakAdminApprovalRepository implements RoleRequestRepository {

    private final Keycloak keycloak;
    private final KeycloakProperties properties;

    private static final String OWNER_ROLE = "ROLE_OWNER";
    private static final String MANAGER_ROLE = "ROLE_MANAGER";

    @Override
    public List<RoleRequest> findPendingRequests() {
        var realm = keycloak.realm(properties.getRealm());
        List<UserRepresentation> users = realm.users().list();

        // 🧠 각 유저의 attributes를 다시 fetch해서 최신 상태로 확인
        return users.stream()
                .map(user -> {
                    var userDetail = realm.users().get(user.getId()).toRepresentation();
                    Map<String, List<String>> attrs = userDetail.getAttributes();
                    if (attrs == null) return null;

                    String ownerStatus = attrs.getOrDefault("owner_request_status", List.of()).stream().findFirst().orElse(null);
                    String managerStatus = attrs.getOrDefault("manager_request_status", List.of()).stream().findFirst().orElse(null);

                    if (!"PENDING".equals(ownerStatus) && !"PENDING".equals(managerStatus)) {
                        return null;
                    }

                    return new RoleRequest(
                            user.getId(),
                            user.getUsername(),
                            user.getEmail(),
                            ownerStatus != null ? "OWNER" : "MANAGER",
                            ownerStatus != null ? ownerStatus : managerStatus
                    );
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public void approveOwner(UUID userId) {
        var realm = keycloak.realm(properties.getRealm());
        var userResource = realm.users().get(userId.toString());
        UserRepresentation user = userResource.toRepresentation();

        // OWNER 권한 부여
        RoleRepresentation ownerRole = realm.roles().get(OWNER_ROLE).toRepresentation();
        userResource.roles().realmLevel().add(List.of(ownerRole));

        // 요청 상태 업데이트
        user.singleAttribute("owner_request_status", "APPROVED");
        userResource.update(user);

        invalidateUserSessions(userId);
    }

    @Override
    public void approveManager(UUID userId) {
        var realm = keycloak.realm(properties.getRealm());
        var userResource = realm.users().get(userId.toString());
        UserRepresentation user = userResource.toRepresentation();

        // ROLE_MANAGER 부여
        RoleRepresentation managerRole = realm.roles().get(MANAGER_ROLE).toRepresentation();
        userResource.roles().realmLevel().add(List.of(managerRole));

        // realm-management 클라이언트의 관리 권한 3개 추가
        var clientRolesResource = realm.clients()
                .findByClientId("realm-management")
                .stream()
                .findFirst()
                .map(client -> realm.clients().get(client.getId()))
                .orElseThrow(() -> new RuntimeException("realm-management client not found"));

        // 필요한 client roles 조회
        RoleRepresentation queryUsers = clientRolesResource.roles().get("query-users").toRepresentation();
        RoleRepresentation viewUsers = clientRolesResource.roles().get("view-users").toRepresentation();
        RoleRepresentation manageUsers = clientRolesResource.roles().get("manage-users").toRepresentation();

        // 유저에게 클라이언트 롤 부여
        userResource.roles()
                .clientLevel(clientRolesResource.toRepresentation().getId())
                .add(List.of(queryUsers, viewUsers, manageUsers));

        // 요청 상태 업데이트
        user.singleAttribute("manager_request_status", "APPROVED");
        userResource.update(user);

        // 세션 무효화 (토큰 만료)
        invalidateUserSessions(userId);
    }

    @Override
    public void demoteOwnerToUser(UUID userId) {
        var realm = keycloak.realm(properties.getRealm());
        var userResource = realm.users().get(userId.toString());

        // 현재 유저 Role 목록 조회
        var currentRoles = userResource.roles().realmLevel().listAll();

        // ROLE_OWNER 제거
        List<RoleRepresentation> removeRoles = currentRoles.stream()
                .filter(role -> role.getName().equals("ROLE_OWNER"))
                .toList();

        if (!removeRoles.isEmpty()) {
            userResource.roles().realmLevel().remove(removeRoles);
        }

        // ROLE_USER 없으면 다시 부여
        boolean hasUserRole = currentRoles.stream()
                .anyMatch(role -> role.getName().equals("ROLE_USER"));
        if (!hasUserRole) {
            RoleRepresentation userRole = realm.roles().get("ROLE_USER").toRepresentation();
            userResource.roles().realmLevel().add(List.of(userRole));
        }

        // attributes 정리 (owner_request_status 삭제)
        var user = userResource.toRepresentation();
        Map<String, List<String>> attrs = user.getAttributes();
        if (attrs != null) {
            attrs.remove("owner_request_status");
            user.setAttributes(attrs);
            userResource.update(user);
        }

        // 세션 무효화 (토큰 만료)
        invalidateUserSessions(userId);
    }

    @Override
    public void demoteManagerToUser(UUID userId) {
        var realm = keycloak.realm(properties.getRealm());
        var userResource = realm.users().get(userId.toString());

        // 현재 유저 Role 목록 조회
        var currentRoles = userResource.roles().realmLevel().listAll();

        // ROLE_MANAGER 제거
        List<RoleRepresentation> removeRoles = currentRoles.stream()
                .filter(role -> role.getName().equals("ROLE_MANAGER"))
                .toList();

        if (!removeRoles.isEmpty()) {
            userResource.roles().realmLevel().remove(removeRoles);
        }

        // ROLE_USER 없으면 다시 부여
        boolean hasUserRole = currentRoles.stream()
                .anyMatch(role -> role.getName().equals("ROLE_USER"));
        if (!hasUserRole) {
            RoleRepresentation userRole = realm.roles().get("ROLE_USER").toRepresentation();
            userResource.roles().realmLevel().add(List.of(userRole));
        }

        // realm-management 클라이언트 roles 제거 (query-users, view-users, manage-users)
        var clientRolesResource = realm.clients()
                .findByClientId("realm-management")
                .stream()
                .findFirst()
                .map(client -> realm.clients().get(client.getId()))
                .orElse(null);

        if (clientRolesResource != null) {
            var clientId = clientRolesResource.toRepresentation().getId();

            List<RoleRepresentation> managementRoles = List.of(
                    clientRolesResource.roles().get("query-users").toRepresentation(),
                    clientRolesResource.roles().get("view-users").toRepresentation(),
                    clientRolesResource.roles().get("manage-users").toRepresentation()
            );

            userResource.roles().clientLevel(clientId).remove(managementRoles);
        }

        // attributes 정리 (manager_request_status 삭제)
        var user = userResource.toRepresentation();
        Map<String, List<String>> attrs = user.getAttributes();
        if (attrs != null) {
            attrs.remove("manager_request_status");
            user.setAttributes(attrs);
            userResource.update(user);
        }
        // 세션 무효화 (토큰 만료)
        invalidateUserSessions(userId);
    }

    private void invalidateUserSessions(UUID userId) {
        var realm = keycloak.realm(properties.getRealm());
        realm.users().get(userId.toString()).logout();
    }

}
