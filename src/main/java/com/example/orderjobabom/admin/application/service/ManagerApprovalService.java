package com.example.orderjobabom.admin.application.service;

import com.example.orderjobabom.admin.domain.repository.RoleRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManagerApprovalService {

    private final RoleRequestRepository roleRequestRepository;

    public void approve(UUID userId) {
        roleRequestRepository.approveManager(userId);
    }
    public void demote(UUID userId) {
        roleRequestRepository.demoteManagerToUser(userId);
    }
}
