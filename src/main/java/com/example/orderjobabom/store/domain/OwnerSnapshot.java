package com.example.orderjobabom.store.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@ToString
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OwnerSnapshot {

    @AttributeOverrides(
            @AttributeOverride(name = "id", column = @Column(name = "owner_id", nullable = false))
    )
    private UserId id;

    @Column(name = "owner_name", nullable = false, length = 60)
    private String name;

    public static OwnerSnapshot of(UserId id, String name){
        if(id == null) throw new IllegalArgumentException("owner id required");
        if(name == null || name.isBlank()) throw new IllegalArgumentException("owner name required");
        return new OwnerSnapshot(id, name);
    }
}
