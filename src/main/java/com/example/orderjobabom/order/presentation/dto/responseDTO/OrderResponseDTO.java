package com.example.orderjobabom.order.presentation.dto.responseDTO;

import com.example.orderjobabom.order.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderResponseDTO {

    public record OrderPreviewDTO(
            OrderId orderId,
            String name,
            int totalPrice,
            OrderStatus orderStatus,
            String address,
            LocalDateTime createdAt

    ) {
        public static OrderPreviewDTO from(Order order) {
            return new OrderPreviewDTO(
                    order.getId(),
                    order.getOrderer().getName(),
                    order.getOrderTotalPrice().getValue(),
                    order.getOrderStatus(),
                    order.getDeliveryInfo().getAddress(),
                    order.getCreatedAt()
            );
        }
    }

    public record OrderDetailsDTO(
        OrderPreviewDTO previewDTO,
        List<OrderItem> orderItemList
    ) {
        public static OrderDetailsDTO from(Order order) {
            return new OrderDetailsDTO(
                    OrderPreviewDTO.from(order),
                    order.getOrderItemList()
            );
        }

    }

}
