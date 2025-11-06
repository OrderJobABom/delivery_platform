package com.example.orderjobabom.store.domain;

import com.example.orderjobabom.user.domain.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, StoreId> {

    Optional<Store> findByOwnerId(UserId ownerId);


    @Modifying
    @Query("UPDATE Store s SET s.averageRating = :avg WHERE s.id.id = :storeId")
    void updateAverageRating(@Param("storeId") UUID storeId, @Param("avg") double avg);
}