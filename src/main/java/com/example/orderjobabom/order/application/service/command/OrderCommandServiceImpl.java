package com.example.orderjobabom.order.application.service.command;

import com.example.orderjobabom.global.presentation.exception.FailException;
import com.example.orderjobabom.menu.domain.Item;
import com.example.orderjobabom.menu.domain.ItemId;
import com.example.orderjobabom.menu.domain.ItemRepository;
import com.example.orderjobabom.order.domain.*;
import com.example.orderjobabom.order.domain.exception.OrderErrorCode;
import com.example.orderjobabom.order.presentation.dto.requestDTO.DeliveryRequestDTO;
import com.example.orderjobabom.order.presentation.dto.requestDTO.OrderCreateRequestDTO;
import com.example.orderjobabom.order.presentation.dto.requestDTO.OrderItemRequestDTO;
import com.example.orderjobabom.order.presentation.dto.responseDTO.OrderResponseDTO;
import com.example.orderjobabom.user.domain.UserId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderCommandServiceImpl implements OrderCommandService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    @Override
    public OrderResponseDTO.OrderDetailsDTO createOrder(OrderCreateRequestDTO requestDTO) {

        // 배송 정보 생성
        DeliveryInfo deliveryInfo = new DeliveryInfo(requestDTO.address(), requestDTO.memo());

        // 요청 DTO의 OrderItems 기반으로 OrderItem 리스트 생성
        List<OrderItem> orderItems = requestDTO.orderItemRequestList().stream()
                .map(this::createOrderItem)
                .toList();

        Jwt jwt = (Jwt)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID ordererId = UUID.fromString(jwt.getSubject());
        UserId userId = UserId.of(ordererId);
        String username = requestDTO.username();

        Orderer orderer = new Orderer(userId, username);

        // Order 생성
        Order newOrder = Order.create(orderer, deliveryInfo, orderItems);

        // 주문 저장
        Order saveOrder = orderRepository.save(newOrder);
        return OrderResponseDTO.OrderDetailsDTO.from(saveOrder);
    }

    // Item 엔티티를 조회해서 OrderItem 객체 생성
    private OrderItem createOrderItem(OrderItemRequestDTO orderItemRequest) {

        Item item = itemRepository.findById(ItemId.of(orderItemRequest.itemId()))
                .orElseThrow(() -> new FailException(OrderErrorCode.ORDER_ITEM_NOT_FOUND));


        // 주문 아이템의 개수만큼 stock 감소
//        item.decreaseStock(orderItemRequest.getCount());

        return OrderItem.builder()
                .itemId(item.getId())
                .itemName(item.getName())
                .price(item.getPrice())
                .count(orderItemRequest.count())
                .totalPrice(item.getPrice().multiply(orderItemRequest.count()))
                .build();

    }

    //  배달 정보 업데이트
    @Override
    public void coordinateDelivery(OrderId orderId, DeliveryRequestDTO dto) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new FailException(OrderErrorCode.ORDER_ITEM_NOT_FOUND));
        order.updateDelivery(dto);
    }

    @Override
    public void cancelOrder(UUID orderId) {

        Order order = orderRepository.findById(OrderId.of(orderId)).orElseThrow(() -> new FailException(OrderErrorCode.ORDER_NOT_FOUND));
        order.cancel();
    }
}

