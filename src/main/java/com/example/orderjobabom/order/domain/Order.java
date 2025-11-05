package com.example.orderjobabom.order.domain;

import com.example.orderjobabom.global.infrastructure.converter.PriceConverter;
import com.example.orderjobabom.global.infrastructure.persistence.BaseEntity;
import com.example.orderjobabom.global.infrastructure.persistence.Price;
import com.example.orderjobabom.global.presentation.exception.FailException;
import com.example.orderjobabom.order.domain.exception.OrderErrorCode;
import com.example.orderjobabom.order.presentation.dto.requestDTO.DeliveryRequestDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "p_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Access(AccessType.FIELD)
public class Order extends BaseEntity {

    @EmbeddedId
    private OrderId id;

    @Embedded
    private Orderer orderer;

    @Convert(converter = PriceConverter.class)
    private Price orderTotalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private DeliveryInfo deliveryInfo;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "p_order_item_list", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "item_idx")
    private List<OrderItem> orderItemList;

    @Builder
    private Order(OrderId id, Orderer orderer, DeliveryInfo deliveryInfo, List<OrderItem> orderItemList, OrderStatus orderStatus) {
        this.id = id;
        this.orderer = orderer;
        this.orderStatus = orderStatus;
        this.deliveryInfo = deliveryInfo;
        this.orderItemList = orderItemList;
        this.orderTotalPrice = calculateTotalPrice();

    }

    private Price calculateTotalPrice() {

        return new Price(this.orderItemList.stream()
                .mapToInt(orderItem -> orderItem.calculateTotalPrice().getValue())
                .sum());
    }

    public static Order create(Orderer orderer, DeliveryInfo deliveryInfo, List<OrderItem> orderItems) {
        if (orderItems.isEmpty() || orderItems == null) {
            throw new FailException(OrderErrorCode.ORDER_ITEM_EMPTY);
        }

        OrderId orderId = OrderId.of();
        return new Order(orderId, orderer, deliveryInfo, orderItems, OrderStatus.ORDER_WAITING);
    }

    public void updateDelivery(DeliveryRequestDTO deliveryRequestDTO) {

        this.deliveryInfo.updateAddress(deliveryRequestDTO);
    }

    public void cancel() {
        if (this.orderStatus != OrderStatus.ORDER_WAITING) {
            throw new FailException(OrderErrorCode.ORDER_CANNOT_BE_CANCELED);
        }

        // 5분 지난 경우
        if (LocalDateTime.now().isAfter(this.getCreatedAt().plusMinutes(5))) {
            throw new FailException(OrderErrorCode.ORDER_CANCEL_TIME_OUT);
        }

        this.orderStatus = OrderStatus.ORDER_CANCEL;
    }
}
