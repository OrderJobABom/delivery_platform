package com.example.orderjobabom.store.domain;

import com.example.orderjobabom.user.domain.UserId;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Entity
@Table(name = "p_store")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Store {

    /** 매장 ID */
    @EmbeddedId
    private StoreId id;

    /** 사장님 ID */
    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "owner_id", nullable = false))
    private UserId ownerId;

    /** 매장 이름 */
    @Column(nullable = false, length = 100)
    private String storeName;

    /** 매장 전화번호 */
    @Column(length = 45)
    private String storeTel;

    /** 매장 주소 */
    @Column(length = 200)
    private String address;

    /** 카테고리 (한식, 중식 등) */
    @Column(length = 50)
    private String category;

    /** 매장 활성화 여부 (삭제 대신 soft delete용) */
    @Column(nullable = false)
    private boolean active = true;

    /** 매장 평균 평점 */
    @Column(name = "average_rating", nullable = true)
    private double averageRating = 0.0;


    // ====== 빌더 생성자 ======
    @Builder
    public Store(StoreId id, UserId ownerId, String storeName, String storeTel, String address, String category) {
        this.id = Objects.requireNonNullElse(id, StoreId.of());
        this.ownerId = ownerId;
        this.storeName = storeName;
        this.storeTel = storeTel;
        this.address = address;
        this.category = category;
        this.active = true;
    }
}
