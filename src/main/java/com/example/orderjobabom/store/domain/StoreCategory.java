package com.example.orderjobabom.store.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StoreCategory {

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Category category;

    private boolean active;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StoreCategory that = (StoreCategory) o;
        return active == that.active && category == that.category;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(category);
    }
}
