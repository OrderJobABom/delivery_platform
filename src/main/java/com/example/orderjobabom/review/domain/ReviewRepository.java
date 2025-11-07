package com.example.orderjobabom.review.domain;

import com.example.orderjobabom.order.domain.OrderId;
import com.example.orderjobabom.store.domain.StoreId;
import com.example.orderjobabom.user.domain.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, ReviewId> {

    /** 주문 ID로 리뷰 전체 조회 (상태 확인용) */
    Optional<Review> findByOrderId(OrderId orderId);

    /** 특정 유저가 작성한 리뷰 전체 조회 */
    @Query("""
        SELECT r
        FROM Review r
        WHERE r.reviewer.id = :userId
          AND r.status = com.example.orderjobabom.review.domain.ReviewStatus.ACTIVE
    """)
    List<Review> findActiveByUserId(UUID userId);

    /** 특정 가게의 모든 리뷰 조회 */
    @Query("""
        SELECT r
        FROM Review r
        WHERE r.store.id = :storeId
    """)
    List<Review> findByStoreId(UUID storeId);

    /** 특정 가게의 답글이 달린 리뷰 조회 */
    @Query("""
        SELECT r
        FROM Review r
        WHERE r.store.id = :storeId
          AND r.reply.content IS NOT NULL
          AND r.reply.status = com.example.orderjobabom.review.domain.ReplyStatus.ACTIVE
    """)

    List<Review> findRepliedReviewsByStoreId(UUID storeId);

    // 리뷰 평균 계산
    @Query("SELECT COALESCE(AVG(r.rating.value), 0) " +
            "FROM Review r WHERE r.store.id = :storeId AND r.status = 'ACTIVE'")
    double calculateAverageByStoreId(@Param("storeId") UUID storeId);

}
