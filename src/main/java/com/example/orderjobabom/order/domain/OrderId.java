package com.example.orderjobabom.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class OrderId {

    @Column(length = 50, name = "order_id")
    private UUID id;

    public OrderId(UUID id) {
        this.id = id;
    }

    public static OrderId of() {
        return new OrderId(UUID.randomUUID());
    }

}
