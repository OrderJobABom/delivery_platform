package com.example.orderjobabom.review.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Rating {

    @Column(name = "rating_value", nullable = false)
    private int value;

    public static Rating of(int value) {
        if (value < 1 || value > 5) {
            throw new IllegalArgumentException("별점은 1~5점이어야 합니다.");
        }
        return new Rating(value);
    }
}
