package com.example.orderjobabom.admin.application.service;

import com.example.orderjobabom.admin.domain.repository.RoleRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OwnerApprovalService {

    private final RoleRequestRepository roleRequestRepository;

    public void approve(UUID userId) {
        roleRequestRepository.approveOwner(userId);
    }
    public void demote(UUID userId) {
        roleRequestRepository.demoteOwnerToUser(userId);
    }
}
