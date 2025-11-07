package com.example.orderjobabom.store.domain;

public enum Category {
    KOREAN("한식"),
    CHINESE("중식"),
    JAPANESE("일식"),
    ITALIAN("이태리");

    private final String value;
    Category(String value) {
        this.value = value;
    }
    public String toString() {
        return value;
    }
}
