package com.example.orderjobabom.admin.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RoleRequest {

    private final String userId;
    private final String username;
    private final String email;
    private final String requestedRole; // OWNER or MANAGER
    private final String status;        // PENDING, APPROVED, REJECTED
}
