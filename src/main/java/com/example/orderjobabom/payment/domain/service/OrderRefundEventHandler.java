//package com.example.orderjobabom.payment.domain.service;
//
//
//import com.example.orderjobabom.order.domain.event.OrderRefundEvent;
//import com.example.orderjobabom.payment.application.service.PaymentCancelService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.event.TransactionalEventListener;
//
//@Service
//@RequiredArgsConstructor
//public class OrderRefundEventHandler {
//
//    private final PaymentCancelService paymentCancelService;
//
//    @Async
//    @TransactionalEventListener(OrderRefundEvent.class)
//    public void handler(OrderRefundEvent event) {
//        paymentCancelService.cancel(event.orderId(), "주문 환불 처리");
//    }
//
//}