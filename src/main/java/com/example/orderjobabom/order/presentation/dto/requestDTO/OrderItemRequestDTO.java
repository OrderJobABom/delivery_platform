package com.example.orderjobabom.order.presentation.dto.requestDTO;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class OrderItemRequestDTO {

    private UUID itemId;
    private int price;
    private int count;
}
