package com.example.orderjobabom.payment.domain.service;


import com.example.orderjobabom.order.domain.event.OrderAcceptEvent;
import com.example.orderjobabom.payment.infrastructure.redis.PaymentTempService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class OrderAcceptEventHandler {

    private final PaymentTempService paymentTempService;

    @Async
    @TransactionalEventListener(OrderAcceptEvent.class)
    public void handle(OrderAcceptEvent event) {
        paymentTempService.saveAmount(event.orderId().getId(), event.totalAmount());
    }

}