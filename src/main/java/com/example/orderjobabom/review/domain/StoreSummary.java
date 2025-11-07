package com.example.orderjobabom.review.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StoreSummary {

    @Column(name = "store_id", nullable = false)
    private UUID id;

    @Column(name = "store_name", nullable = false, length = 100)
    private String name;

    public static StoreSummary of(UUID id, String name) {
        return new StoreSummary(id, name);
    }
}
