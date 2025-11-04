package com.example.orderjobabom.store.repository;

import com.example.orderjobabom.store.domain.Store;
import com.example.orderjobabom.store.domain.StoreId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface StoreRepositoryCustom {
    Optional<Store> findByIdNotDeleted(StoreId id);
    Optional<Store> findByOwnerId(UUID ownerId);
    boolean existsByOwnerId(UUID ownerId);
    Page<Store> findAllNotDeleted(Pageable pageable);
    Page<Store> findAllIncludingDeleted(Pageable pageable);
}