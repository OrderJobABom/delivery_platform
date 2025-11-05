package com.example.orderjobabom.order.presentation.controller;

import com.example.orderjobabom.global.presentation.CustomResponse;
import com.example.orderjobabom.order.domain.OrderId;
import com.example.orderjobabom.order.domain.code.OrderSuccessCode;
import com.example.orderjobabom.order.application.service.command.OrderCommandService;
import com.example.orderjobabom.order.application.service.query.OrderQueryService;
import com.example.orderjobabom.order.presentation.dto.requestDTO.OrderCancelRequestDTO;
import com.example.orderjobabom.order.presentation.dto.requestDTO.OrderCreateRequestDTO;
import com.example.orderjobabom.order.presentation.dto.responseDTO.OrderResponseDTO;
import com.example.orderjobabom.user.domain.UserId;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
@Tag(name = "주문 API", description = "주문 생성, 조회, 취소 관련 API")
public class OrderController {

    private final OrderQueryService orderQueryService;
    private final OrderCommandService orderCommandService;

    @PostMapping("")
    public CustomResponse<?> createOrder(@RequestBody OrderCreateRequestDTO requestDTO) {

        OrderResponseDTO.OrderDetailsDTO orderResponse = orderCommandService.createOrder(requestDTO);
        return CustomResponse.of(OrderSuccessCode.ORDER_CREATED, orderResponse);
    }

    @GetMapping("/{userId}") // userId 임시
    public CustomResponse<?> getAllOrders(
            @PathVariable("userId") UUID userId,
            @RequestParam(name = "startDate") LocalDate startDate,
            @RequestParam(name = "endDate") LocalDate endDate,
            @RequestParam(name = "pageSize") int pageSize,
            @RequestParam(name = "offset") int offset) {

        Page<OrderResponseDTO.OrderPreviewDTO> orderHistory = orderQueryService.getOrderHistoryByDate(userId, startDate, endDate, pageSize, offset);

        return CustomResponse.of(OrderSuccessCode.ORDER_OK, orderHistory);
    }

    @GetMapping("/{userId}/{orderId}") // userId 임시
    public CustomResponse<?> getOrderDetail(
            @PathVariable("userId") UserId userId,
            @PathVariable("orderId") OrderId orderId
    ) {

        OrderResponseDTO.OrderDetailsDTO orderDetail = orderQueryService.getOrderDetail(orderId);
        return CustomResponse.onSuccess(orderDetail);
    }

    @PutMapping("{orderId}/cancel")
    public CustomResponse<?> cancelOrder(
            @PathVariable("orderId") OrderId orderId,
            @RequestBody OrderCancelRequestDTO orderCancelDTO
            ) {

        orderCommandService.cancelOrder(orderId);
        return CustomResponse.onSuccess(orderCancelDTO);

    }

}
