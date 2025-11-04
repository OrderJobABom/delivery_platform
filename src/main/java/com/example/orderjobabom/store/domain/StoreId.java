package com.example.orderjobabom.store.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;

@ToString
@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreId {
    @Column(name="store_id", length = 60, nullable = false, updatable = false)
    public UUID id;

    public StoreId(UUID id) {
        this.id = id;
    }

    public static StoreId of() { return new StoreId(UUID.randomUUID()); }
    public static StoreId of(UUID id) { return new StoreId(id); }
}
