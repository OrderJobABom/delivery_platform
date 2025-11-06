package com.example.orderjobabom.order.application.service.command;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class OrderCommandServiceImplTest {

//    @Autowired
//    private OrderCommandService orderCommandService;
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private ItemRepository itemRepository;
//
//    OrderCreateRequestDTO request;
//
//    @BeforeEach
//    void init() {
//
//        Item testItem = new Item(
//                ItemId.of(UUID.fromString("eebad6f5-7ea5-4091-9272-b1bacec129b6")),
//                new Price(15000),
//                "테스트 상품"
//        );
//
//        UserId id = UserId.of(UUID.fromString("eebad6f5-7ea5-4091-9272-b1bacec129b1"));
//        itemRepository.save(testItem);
//
//        request = OrderCreateRequestDTO.builder()
//                .orderItemRequestList(List.of(OrderItemRequestDTO.builder()
//                        .itemId(ItemId.of(UUID.fromString("eebad6f5-7ea5-4091-9272-b1bacec129b6")).getId())
//                        .price(15000)
//                        .count(1)
//                        .build()))
//                .address("서울시")
//                .memo("메모입니다")
//                .payPrice(15000)
//                .build();
//    }
//
//    @Test
//    @DisplayName("주문 등록 테스트")
//    @Transactional
//    void createOrder() {
//        assertDoesNotThrow(() -> {
//
//            OrderResponseDTO.OrderDetailsDTO orderResponse = orderCommandService.createOrder(request);
//            Order order = orderRepository.findById(orderResponse.previewDTO().orderId()).orElseThrow();
//            System.out.println(order);
//        });
//    }
//



}