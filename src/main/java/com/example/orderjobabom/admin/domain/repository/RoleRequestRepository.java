package com.example.orderjobabom.admin.domain.repository;

import com.example.orderjobabom.admin.domain.model.RoleRequest;
import java.util.List;
import java.util.UUID;

public interface RoleRequestRepository {

    List<RoleRequest> findPendingRequests();

    void approveOwner(UUID userId);

    void approveManager(UUID userId);

    void demoteOwnerToUser(UUID userId);

    void demoteManagerToUser(UUID userId);
}
