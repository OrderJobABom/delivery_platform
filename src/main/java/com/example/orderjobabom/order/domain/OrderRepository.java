package com.example.orderjobabom.order.domain;

import com.example.orderjobabom.user.domain.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, OrderId> {


    // 특정 사용자 전체 내역 조회
    @Query("SELECT o FROM Order o WHERE o.orderer.id = :userId AND o.deletedAt is NULL ")
    Page<Order> findByOrdererId(@Param("userId") UUID userId, Pageable pageable);

    // 기간별 주문 조회
    @Query("SELECT o FROM Order o WHERE o.orderer.id = :userId AND o.deletedAt IS NULL AND o.createdAt BETWEEN :start AND :end")
    Page<Order> findByOrdererIdAndCreatedAtBetween(
                    @Param("userId") UUID userId,
                    @Param("start") LocalDate start,
                    @Param("end") LocalDate end,
                    Pageable pageable
            );
}
