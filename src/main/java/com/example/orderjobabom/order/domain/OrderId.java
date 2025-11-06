package com.example.orderjobabom.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;

@ToString
@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderId {
    @Column(length=45, name="order_id")
    private UUID id;

    public OrderId(UUID id) {
        this.id = id;
    }

    public static OrderId of() {
        return OrderId.of(UUID.randomUUID());
    }

    public static OrderId of(UUID id) {
        return new OrderId(id);
    }

}
