package com.example.orderjobabom.global.infrastructure.converter;

import com.example.orderjobabom.global.infrastructure.persistence.Price;
import jakarta.persistence.AttributeConverter;

import java.util.Objects;

public class PriceConverter implements AttributeConverter<Price, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Price price) {
        return price == null ? 0 : price.getValue();
    }

    @Override
    public Price convertToEntityAttribute(Integer value) {
        return new Price(Objects.requireNonNullElse(value, 0));
    }
}
