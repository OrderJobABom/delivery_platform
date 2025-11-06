package com.example.orderjobabom.order.application.service.command;

import com.example.orderjobabom.order.domain.OrderId;
import com.example.orderjobabom.order.presentation.dto.requestDTO.DeliveryRequestDTO;
import com.example.orderjobabom.order.presentation.dto.requestDTO.OrderCreateRequestDTO;
import com.example.orderjobabom.order.presentation.dto.responseDTO.OrderResponseDTO;

import java.util.UUID;

public interface OrderCommandService {

    // 주문 생성
    OrderResponseDTO.OrderDetailsDTO createOrder(OrderCreateRequestDTO dto);

    // 배달 주소 지정
    void coordinateDelivery(OrderId orderId, DeliveryRequestDTO deliveryRequestDTO);

    // 주문 취소
    void cancelOrder(UUID orderId);
}
