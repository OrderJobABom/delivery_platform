package com.example.orderjobabom.menu.infrastructure.persistence.converter;

import com.example.orderjobabom.menu.domain.Stock;
import jakarta.persistence.AttributeConverter;

public class StockConverter implements AttributeConverter<Stock, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Stock attribute) {
        return attribute.getValue();
    }

    @Override
    public Stock convertToEntityAttribute(Integer num) {
        return new Stock(num == null ? 0 : num);
    }
}
