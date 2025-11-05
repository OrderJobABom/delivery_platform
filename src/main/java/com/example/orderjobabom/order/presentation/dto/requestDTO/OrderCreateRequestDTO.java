package com.example.orderjobabom.order.presentation.dto.requestDTO;

import com.example.orderjobabom.global.infrastructure.persistence.Price;
import com.example.orderjobabom.order.domain.DeliveryInfo;
import com.example.orderjobabom.order.domain.Orderer;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrderCreateRequestDTO {

    List<OrderItemRequestDTO> orderItemRequestList;
    DeliveryInfo deliveryInfo;
    Price payPrice;
    Orderer orderer;
}
