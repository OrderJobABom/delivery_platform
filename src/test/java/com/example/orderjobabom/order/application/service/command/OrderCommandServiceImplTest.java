package com.example.orderjobabom.order.application.service.command;

import com.example.orderjobabom.global.infrastructure.persistence.Price;
import com.example.orderjobabom.menu.Item;
import com.example.orderjobabom.menu.ItemId;
import com.example.orderjobabom.menu.ItemRepository;
import com.example.orderjobabom.order.domain.DeliveryInfo;
import com.example.orderjobabom.order.domain.Order;
import com.example.orderjobabom.order.domain.OrderRepository;
import com.example.orderjobabom.order.domain.Orderer;
import com.example.orderjobabom.order.presentation.dto.requestDTO.OrderCreateRequestDTO;
import com.example.orderjobabom.order.presentation.dto.requestDTO.OrderItemRequestDTO;
import com.example.orderjobabom.order.presentation.dto.responseDTO.OrderResponseDTO;
import com.example.orderjobabom.user.domain.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class OrderCommandServiceImplTest {

    @Autowired
    private OrderCommandService orderCommandService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    OrderCreateRequestDTO request;

    @BeforeEach
    void init() {

        Item testItem = new Item(
                ItemId.of(UUID.fromString("eebad6f5-7ea5-4091-9272-b1bacec129b6")),
                new Price(15000),
                "테스트 상품"
        );

        UserId id = UserId.of(UUID.fromString("eebad6f5-7ea5-4091-9272-b1bacec129b1"));
        itemRepository.save(testItem);

        request = OrderCreateRequestDTO.builder()
                .orderItemRequestList(List.of(OrderItemRequestDTO.builder()
                        .itemId(ItemId.of(UUID.fromString("eebad6f5-7ea5-4091-9272-b1bacec129b6")))
                        .price(15000)
                        .count(1)
                        .build()))
                .deliveryInfo(new DeliveryInfo("서울시", "메모입니다."))
                .payPrice(new Price(15000))
                .orderer(new Orderer(id,"testName" ))
                .build();
    }

    @Test
    @DisplayName("주문 등록 테스트")
    @Transactional
    void createOrder() {
        assertDoesNotThrow(() -> {

            OrderResponseDTO.OrderDetailsDTO orderResponse = orderCommandService.createOrder(request);
            Order order = orderRepository.findById(orderResponse.previewDTO().orderId()).orElseThrow();
            System.out.println(order);
        });
    }




}