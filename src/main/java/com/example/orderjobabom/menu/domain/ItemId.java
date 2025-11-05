package com.example.orderjobabom.menu.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;

@ToString
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class ItemId {

    @Column(name = "item_id")
    private UUID id;

    public ItemId(UUID id) {
        this.id = id;
    }

    public static ItemId of() {
        return ItemId.of(UUID.randomUUID());
    }

    public static ItemId of(UUID id) {
        return new ItemId(id);
    }
}
