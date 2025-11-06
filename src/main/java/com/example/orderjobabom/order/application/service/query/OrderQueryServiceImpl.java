package com.example.orderjobabom.order.application.service.query;

import com.example.orderjobabom.global.presentation.exception.FailException;
import com.example.orderjobabom.order.domain.Order;
import com.example.orderjobabom.order.domain.OrderId;
import com.example.orderjobabom.order.domain.OrderRepository;
import com.example.orderjobabom.order.domain.code.OrderErrorCode;
import com.example.orderjobabom.order.presentation.dto.responseDTO.OrderResponseDTO;
import com.example.orderjobabom.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderQueryServiceImpl implements OrderQueryService {

    private OrderRepository orderRepository;

    @Override
    public Page<OrderResponseDTO.OrderPreviewDTO> getOrdersHistory(UUID userId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Order> orderPage = orderRepository.findByOrdererId(userId, pageable);
        return orderPage.map(OrderResponseDTO.OrderPreviewDTO::from);
    }

    @Override
    public Page<OrderResponseDTO.OrderPreviewDTO> getOrderHistoryByDate(UUID userId, LocalDate start, LocalDate end, int page, int size ) {

        if (start.isAfter(end)) {
            throw new FailException(OrderErrorCode.ORDER_DATE_FORMAT_BAD_REQUEST);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Order> orderPage = orderRepository.findByOrdererIdAndCreatedAtBetween(userId, start, end, pageable);
        return orderPage.map(OrderResponseDTO.OrderPreviewDTO::from);
    }

    @Override
    public OrderResponseDTO.OrderDetailsDTO getOrderDetail(UUID detailOrderId) {

        OrderId orderId = OrderId.of(detailOrderId);

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new FailException(OrderErrorCode.ORDER_ITEM_NOT_FOUND));
        return OrderResponseDTO.OrderDetailsDTO.from(order);
    }
}
