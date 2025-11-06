package com.example.orderjobabom.menu.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock {

    private int value;

    public Stock(int value) {
        this.value = Math.max(0, value);
    }

    public Stock add(int num) {
        return new Stock(value + num);
    }

    public Stock minus(int num) {
        return new Stock(value - num);
    }
}
