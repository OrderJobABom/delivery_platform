package com.example.orderjobabom.menu;

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
public class ItemId {

    @Column(name = "item_id")
    private UUID id;

    public ItemId(UUID id) {
        this.id = id;
    }

    public static ItemId of(UUID id) {
        return new ItemId(id);
    }
}
