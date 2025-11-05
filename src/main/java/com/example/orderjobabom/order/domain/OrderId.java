package com.example.orderjobabom.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class OrderId {

    @Column(length = 50, name = "order_id")
    private UUID id;

    public OrderId(UUID id) {
        this.id = id;
    }

    public static OrderId of() {
        return new OrderId(UUID.randomUUID());
    }

    public static OrderId of(UUID id) {
        return new OrderId(id);
    }

}
