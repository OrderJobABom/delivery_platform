package com.example.orderjobabom.global.infrastructure.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("securityRoleUtil")
public class SecurityRoleUtil {

    /**
     * 오직 ROLE_USER만 가지고 있고
     * ROLE_MASTER, ROLE_MANAGER, ROLE_OWNER 중 어떤 것도 가지지 않은 경우만 true
     */
    public boolean isPureUser(Authentication auth) {
        if (auth == null || auth.getAuthorities() == null) return false;

        boolean hasUser = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"));

        boolean hasOther = auth.getAuthorities().stream()
                .anyMatch(a -> List.of("ROLE_MASTER", "ROLE_MANAGER", "ROLE_OWNER")
                        .contains(a.getAuthority()));

        return hasUser && !hasOther;
    }

    /**
     * ROLE_OWNER이면서 동시에 ROLE_USER도 가지고 있어야 true
     */
    public boolean isOwner(Authentication auth) {
        if (auth == null || auth.getAuthorities() == null) return false;

        boolean hasUser = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"));

        boolean hasOwner = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_OWNER"));

        return hasUser && hasOwner;
    }


    /**
     * ROLE_MANAGER 권한이 있어야 true
     */
    public boolean isManager(Authentication auth) {
        if (auth == null || auth.getAuthorities() == null) return false;

        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"));
    }

    /**
     * ROLE_MASTER 권한이 있어야 true
     */
    public boolean isMaster(Authentication auth) {
        if (auth == null || auth.getAuthorities() == null) return false;

        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MASTER"));
    }
}
