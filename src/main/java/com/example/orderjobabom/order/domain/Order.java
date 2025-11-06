package com.example.orderjobabom.order.domain;

import com.example.orderjobabom.user.domain.UserId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_order")  // 실제 DB 테이블 이름
public class Order {

    @EmbeddedId
    private OrderId id;  // 기본키

    @Embedded
    private UserId userId;  // 유저 ID (작성자 등)

    // 나중에 주문 상태, 매장, 결제 금액 등 추가 가능
    // @Enumerated(EnumType.STRING)
    // private OrderStatus status;

    public Order(OrderId id, UserId userId) {
        this.id = id;
        this.userId = userId;
    }
}
