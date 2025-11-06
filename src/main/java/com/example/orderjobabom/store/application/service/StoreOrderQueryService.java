package com.example.orderjobabom.store.application.service;

import com.example.orderjobabom.global.presentation.exception.FailException;
import com.example.orderjobabom.order.domain.Order;
import com.example.orderjobabom.order.domain.OrderRepository;
import com.example.orderjobabom.store.application.service.dto.StoreOrderSummaryDto;
import com.example.orderjobabom.store.domain.StoreId;
import com.example.orderjobabom.store.domain.StoreRepository;
import com.example.orderjobabom.store.domain.exception.StoreErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreOrderQueryService {

    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;

    public Page<StoreOrderSummaryDto> getStoreOrders(UUID storeId, int page, int size) {

        storeRepository.findById(StoreId.of(storeId)).orElseThrow(() -> new FailException(StoreErrorCode.STORE_NOT_FOUND));
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Order> orders = orderRepository.findAllByStoreId(storeId, pageable);

        return orders.map(order -> StoreOrderSummaryDto.from(order));
    }

}
