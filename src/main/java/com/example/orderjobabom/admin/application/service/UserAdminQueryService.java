package com.example.orderjobabom.admin.application.service;

import com.example.orderjobabom.admin.infrastructure.keycloak.KeycloakUserAdminClient;
import com.example.orderjobabom.admin.infrastructure.keycloak.dto.KeycloakUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAdminQueryService {

    private final KeycloakUserAdminClient keycloakUserAdminClient;

    public List<KeycloakUserResponse> getUsers(
            int page,
            int size,
            String search,
            String sort,
            String accessToken,
            Set<String> currentUserRoles
    ) {
        int first = page * size;
        List<KeycloakUserResponse> users =
                keycloakUserAdminClient.getUsers(first, size, search, accessToken);

        List<String> allowedRoles = List.of("ROLE_MASTER", "ROLE_MANAGER", "ROLE_OWNER", "ROLE_USER");

        users.forEach(u -> {
            List<String> roles = keycloakUserAdminClient.getUserRoles(u.getId(), accessToken);
            if (roles != null && !roles.isEmpty()) {
                u.setRealmRoles(
                        roles.stream()
                                .filter(allowedRoles::contains)
                                .map(r -> {
                                    KeycloakUserResponse.KeycloakUserRole rr = new KeycloakUserResponse.KeycloakUserRole();
                                    rr.setName(r);
                                    return rr;
                                })
                                .toList()
                );
            }
        });

        if (currentUserRoles.contains("ROLE_MANAGER")) {
            users = users.stream()
                    .filter(u -> {
                        if (u.getRealmRoles() == null) return false;
                        List<String> roleNames = u.getRealmRoles().stream()
                                .map(KeycloakUserResponse.KeycloakUserRole::getName)
                                .toList();
                        return roleNames.contains("ROLE_USER") || roleNames.contains("ROLE_OWNER");
                    })
                    .collect(Collectors.toList());
        } else if (currentUserRoles.contains("ROLE_MASTER")) {
            // MASTER는 모든 유저 조회 가능
        } else {
            users = List.of();
        }

        Comparator<KeycloakUserResponse> comparator;
        if ("name".equalsIgnoreCase(sort)) {
            comparator = Comparator.comparing(KeycloakUserResponse::getUsername);
        } else if ("email".equalsIgnoreCase(sort)) {
            comparator = Comparator.comparing(KeycloakUserResponse::getEmail);
        } else {
            comparator = Comparator.comparing(KeycloakUserResponse::getCreatedTimestamp).reversed();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

        return users.stream()
                .sorted(comparator)
                .peek(u -> {
                    if (u.getAttributes() != null && u.getAttributes().containsKey("phone")) {
                        List<String> phones = u.getAttributes().get("phone");
                        if (phones != null && !phones.isEmpty()) {
                            u.setPhone(phones.get(0));
                        }
                    }

                    if (u.getCreatedTimestamp() != null) {
                        LocalDateTime time = LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(u.getCreatedTimestamp()),
                                ZoneId.systemDefault()
                        );
                        u.setFormattedCreatedAt(time.format(formatter));
                    }
                })
                .collect(Collectors.toList());
    }
}
