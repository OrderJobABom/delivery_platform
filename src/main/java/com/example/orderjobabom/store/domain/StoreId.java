package com.example.orderjobabom.store.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;

@Embeddable
@ToString
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreId {

    @Column(length = 50, name = "store_id")
    private UUID id;

    public StoreId(UUID id) {
        this.id = id;
    }

    public static StoreId of() {
        return StoreId.of(UUID.randomUUID());
    }

    public static StoreId of(UUID id) {
        return new StoreId(id);
    }
}


