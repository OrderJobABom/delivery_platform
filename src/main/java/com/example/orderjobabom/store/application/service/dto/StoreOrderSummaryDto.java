package com.example.orderjobabom.store.application.service.dto;

import com.example.orderjobabom.order.domain.Order;
import com.example.orderjobabom.order.domain.OrderStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record StoreOrderSummaryDto(
    UUID orderId,
    String ordererName,
    int totalPrice,
    OrderStatus orderStatus,
    String address,
    String orderSummary,
    LocalDateTime orderedAt
) {
    public static StoreOrderSummaryDto from(Order order) {

        String summary = getOrderSummary(order);

        return new StoreOrderSummaryDto(
                order.getId().getId(),
                order.getOrderer().getName(),
                order.getOrderTotalPrice().getValue(),
                order.getOrderStatus(),
                order.getDeliveryInfo().getAddress(),
                summary,
                order.getCreatedAt()
        );
    }

    // 주문 개수에 따라 다른 응답값을 반환
    private static String getOrderSummary(Order order) {
        if (order.getOrderItemList() == null || order.getOrderItemList().isEmpty()) {
            return "주문 항목이 없습니다.";
        }

        String firstItemName = order.getOrderItemList().getFirst().getItemName();
        int totalItemCount = order.getOrderItemList().size();

        if (totalItemCount >= 2) {
            return String.format("%s 외 %d건", firstItemName, totalItemCount - 1);
        }
        return firstItemName;
    }

}
