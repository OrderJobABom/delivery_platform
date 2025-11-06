package com.example.orderjobabom.admin.infrastructure.keycloak.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Map;

/**
 * Keycloak Admin REST API의 유저 정보 응답 DTO
 */
@Getter
@Setter
public class KeycloakUserResponse {

    private String id;
    private String username;
    private String email;
    private boolean enabled;
    private Long createdTimestamp;
    private String formattedCreatedAt;
    private List<KeycloakUserRole> realmRoles;
    private Map<String, List<String>> attributes;
    private String phone;

    @Getter
    @Setter
    public static class KeycloakUserRole {
        private String name;
    }
}
