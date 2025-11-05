package com.example.orderjobabom.store.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.Objects;

@ToString
@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreCategory {
    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private Category category;

    private boolean active;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StoreCategory that = (StoreCategory) o;
        return category.equals(that.category);
    }

    @Override
    public int hashCode(){ return Objects.hashCode(category); }

}
