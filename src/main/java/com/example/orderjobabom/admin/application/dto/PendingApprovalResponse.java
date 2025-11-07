package com.example.orderjobabom.admin.application.dto;

import java.util.UUID;

public record PendingApprovalResponse(
        UUID userId,
        String username,
        String email,
        String requestedRole,
        String status
) {}
