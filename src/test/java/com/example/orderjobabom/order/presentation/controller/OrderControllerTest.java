package com.example.orderjobabom.order.presentation.controller;

import com.example.orderjobabom.global.infrastructure.persistence.Price;
import com.example.orderjobabom.menu.domain.ItemId;
import com.example.orderjobabom.order.application.service.command.OrderCommandService;
import com.example.orderjobabom.order.application.service.query.OrderQueryService;
import com.example.orderjobabom.order.domain.DeliveryInfo;
import com.example.orderjobabom.order.domain.Order;
import com.example.orderjobabom.order.domain.OrderItem;
import com.example.orderjobabom.order.domain.Orderer;
import com.example.orderjobabom.order.presentation.dto.requestDTO.OrderCreateRequestDTO;
import com.example.orderjobabom.order.presentation.dto.requestDTO.OrderItemRequestDTO;
import com.example.orderjobabom.order.presentation.dto.responseDTO.OrderResponseDTO;
import com.example.orderjobabom.user.domain.UserId;
import com.example.orderjobabom.user.test.MockUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderQueryService orderQueryService;

    @MockitoBean
    private OrderCommandService orderCommandService;

    @Test
    @DisplayName("주문 생성 테스트")
    @MockUser(roles = "USER")
    void createOrderTest() throws Exception {

        List<OrderItemRequestDTO> itemRequestList = List.of(
                new OrderItemRequestDTO(ItemId.of(UUID.fromString("f1b8f2b9-4a19-4dcb-8f1c-3202d4a5a5a9")).getId(), 1000, 1),
                new OrderItemRequestDTO(ItemId.of(UUID.fromString("f1b8f2b9-4a19-4dcb-8f1c-3202d4a5a5a8")).getId(), 2000, 2)
        );

        OrderCreateRequestDTO orderCreateRequest = OrderCreateRequestDTO.builder()
                .orderItemRequestList(itemRequestList)
                .address("서울시 테스트 주소")
                .memo("테스트 메모입니다.")
                .payPrice(5000) // (1000 * 1) + (2000 * 2) = 5000
                .username("testuser") // DTO에 username 필드가 있으므로 설정
                .build();


        Orderer orderer = new Orderer(UserId.of(UUID.fromString("f31d6e32-68f4-4d84-9c3c-cb2d7905f47e")), "testuser");

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(OrderItem.builder()
                .itemId(ItemId.of(UUID.fromString("f1b8f2b9-4a19-4dcb-8f1c-3202d4a5a5a9")))
                .itemName("Item 1")
                .price(new Price(1000))
                .count(1)
                .build());

        DeliveryInfo deliveryInfo = new DeliveryInfo("서울시", "메모" );

        Order order = Order.create(orderer, deliveryInfo, orderItems );

        Mockito.when(orderCommandService.createOrder(Mockito.any(OrderCreateRequestDTO.class)))
                .thenReturn(OrderResponseDTO.OrderDetailsDTO.from(order));

        mockMvc.perform(post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderCreateRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}