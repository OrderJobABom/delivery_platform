package com.example.orderjobabom.store.application.service;

import com.example.orderjobabom.global.presentation.exception.FailException;
import com.example.orderjobabom.order.domain.Order;
import com.example.orderjobabom.order.domain.OrderId;
import com.example.orderjobabom.order.domain.OrderRepository;
import com.example.orderjobabom.order.domain.exception.OrderErrorCode;
import com.example.orderjobabom.store.domain.StoreId;
import com.example.orderjobabom.store.domain.StoreRepository;
import com.example.orderjobabom.store.domain.exception.StoreErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@PreAuthorize("hasAnyRole('OWNER', 'MASTER', 'MANAGER')")
public class StoreOrderCommandService {

    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;

    public void startCooking(UUID storeId, UUID orderId) {

        Order order = orderRepository.findById(OrderId.of(orderId)).orElseThrow(() -> new FailException(OrderErrorCode.ORDER_NOT_FOUND));

        if (!storeRepository.existsById(StoreId.of(storeId))) {
            throw new FailException(StoreErrorCode.STORE_NOT_FOUND);
        }

        if (!order.getStoreId().equals(storeId)) {
            throw new FailException(StoreErrorCode.CAN_NOT_ACCESS);
        }

        order.completeCooking();
    }

    public void completeCooking(UUID storeId, UUID orderId) {
        if (!storeRepository.existsById(StoreId.of(storeId))) {
            throw new FailException(StoreErrorCode.STORE_NOT_FOUND);
        }

        Order order = orderRepository.findById(OrderId.of(orderId)).orElseThrow(() -> new FailException(OrderErrorCode.ORDER_NOT_FOUND));

        order.completeCooking();
    }


}
