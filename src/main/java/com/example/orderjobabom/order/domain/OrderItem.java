package com.example.orderjobabom.order.domain;

import com.example.orderjobabom.global.infrastructure.converter.PriceConverter;
import com.example.orderjobabom.global.infrastructure.persistence.Price;
import com.example.orderjobabom.menu.ItemId;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.*;


@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class OrderItem {

    @Embedded
    private ItemId itemId;

    @Column(nullable = false, length = 30)
    private String itemName;

    @Convert(converter = PriceConverter.class)
    private Price price;

    private int count;

    @Convert(converter = PriceConverter.class)
    private Price totalPrice;

    public Price calculateTotalPrice() {
        return price.multiply(count);
    }


}
