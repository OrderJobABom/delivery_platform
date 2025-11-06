package com.example.orderjobabom.global.infrastructure.keycloak;

import com.example.orderjobabom.user.infrastructure.keycloak.KeycloakProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.ClientScopeRepresentation;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Keycloak 초기 설정 자동화
 * spring-app 클라이언트에 realm-management 클라이언트 스코프를 자동으로 연결한다.
 * Keycloak 서버가 실행 중이며 Admin Client로 접근 가능한 상태여야 한다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(KeycloakProperties.class)
public class KeycloakClientScopeInitializer {

    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;

    private static final String TARGET_CLIENT_ID = "spring-app";
    private static final String TARGET_SCOPE = "realm-management";

    @PostConstruct
    public void initializeClientScope() {
        try {
            var realm = keycloak.realm(keycloakProperties.getRealm());

            // 1. spring-app 클라이언트 조회
            Optional<ClientRepresentation> targetClientOpt = realm.clients()
                    .findByClientId(TARGET_CLIENT_ID)
                    .stream()
                    .findFirst();

            if (targetClientOpt.isEmpty()) {
                log.error("spring-app client not found in realm '{}'", keycloakProperties.getRealm());
                return;
            }

            var targetClient = realm.clients().get(targetClientOpt.get().getId());

            // 2. realm-management 스코프 조회
            Optional<ClientScopeRepresentation> managementScopeOpt = realm.clientScopes()
                    .findAll()
                    .stream()
                    .filter(scope -> TARGET_SCOPE.equals(scope.getName()))
                    .findFirst();

            if (managementScopeOpt.isEmpty()) {
                log.error("realm-management scope not found in realm '{}'", keycloakProperties.getRealm());
                return;
            }

            String scopeId = managementScopeOpt.get().getId();

            // 3. 이미 연결되어 있는지 확인
            boolean alreadyLinked = targetClient.getDefaultClientScopes()
                    .stream()
                    .anyMatch(scope -> TARGET_SCOPE.equals(scope.getName()));

            if (alreadyLinked) {
                log.info("'{}' already has '{}' scope. Skipping setup.", TARGET_CLIENT_ID, TARGET_SCOPE);
                return;
            }

            // 4. realm-management scope 연결
            targetClient.addDefaultClientScope(scopeId);
            log.info("Added '{}' scope to '{}' client successfully.", TARGET_SCOPE, TARGET_CLIENT_ID);

        } catch (Exception e) {
            log.error("Failed to initialize Keycloak client scope: {}", e.getMessage(), e);
        }
    }
}
