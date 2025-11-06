package com.example.orderjobabom.menu.domain;

import com.example.orderjobabom.global.infrastructure.converter.PriceConverter;
import com.example.orderjobabom.global.infrastructure.persistence.Price;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemOption {

    @Column(nullable = false, length = 50)
    private String optionName;

    @Convert(converter = PriceConverter.class)
    private Price addPrice;

    @Builder
    public ItemOption(String optionName, Price addPrice) {
        this.optionName = optionName;
        this.addPrice = addPrice;
    }


}
