package com.example.orderjobabom.order.presentation.dto.requestDTO;

import com.example.orderjobabom.global.infrastructure.persistence.Price;
import com.example.orderjobabom.menu.ItemId;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class OrderItemRequestDTO {

    private ItemId itemId;
    private int price;
    private int count;
}
