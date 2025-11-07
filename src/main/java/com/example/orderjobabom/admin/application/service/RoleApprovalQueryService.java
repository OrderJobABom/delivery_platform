package com.example.orderjobabom.admin.application.service;

import com.example.orderjobabom.admin.application.dto.PendingApprovalResponse;
import com.example.orderjobabom.admin.domain.repository.RoleRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleApprovalQueryService {

    private final RoleRequestRepository roleRequestRepository;

    public List<PendingApprovalResponse> getPendingRequests() {
        return roleRequestRepository.findPendingRequests().stream()
                .map(req -> new PendingApprovalResponse(
                        UUID.fromString(req.getUserId()),
                        req.getUsername(),
                        req.getEmail(),
                        req.getRequestedRole(),
                        req.getStatus()
                ))
                .collect(Collectors.toList());
    }
}
