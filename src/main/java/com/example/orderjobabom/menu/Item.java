package com.example.orderjobabom.menu;

import com.example.orderjobabom.global.infrastructure.converter.PriceConverter;
import com.example.orderjobabom.global.infrastructure.persistence.BaseEntity;
import com.example.orderjobabom.global.infrastructure.persistence.Price;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@Table(name = "p_item")
public class Item extends BaseEntity {

    @EmbeddedId
    @GeneratedValue(strategy = GenerationType.UUID)
    private ItemId id;

    @Convert(converter = PriceConverter.class)
    private Price price;

    @Column(nullable = false)
    private String name;

}
