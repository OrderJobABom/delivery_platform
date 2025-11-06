package com.example.orderjobabom.global.infrastructure.persistence;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Price {

    private int value;

    public Price(int value) {
        this.value = value;
    }

    public Price add(Price price) {
        return new Price(this.value + price.value);
    }

    public Price multiply(int count) {
        return new Price(this.value * count);
    }
}
