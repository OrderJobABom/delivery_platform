package com.example.orderjobabom.review.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ReviewId implements Serializable {

    @Column(name = "review_id", nullable = false, updatable = false, unique = true)
    private UUID id;

    public static ReviewId of() {
        return new ReviewId(UUID.randomUUID());
    }
}
