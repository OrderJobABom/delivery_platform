package com.example.orderjobabom.order.application.service.query;


import com.example.orderjobabom.order.domain.OrderId;
import com.example.orderjobabom.order.presentation.dto.responseDTO.OrderResponseDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.UUID;

public interface OrderQueryService {

    // 주문 내역 조회
    Page<OrderResponseDTO.OrderPreviewDTO> getOrdersHistory(UUID userId, int page, int size);

    // 주문 내역 기간별 조회
    Page<OrderResponseDTO.OrderPreviewDTO> getOrderHistoryByDate(UUID userId, LocalDate start, LocalDate end, int page, int size);

    //주문 상세 조회
    OrderResponseDTO.OrderDetailsDTO getOrderDetail(UUID orderId);


}
