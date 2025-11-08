package com.example.orderjobabom.order.domain.event;


import com.example.orderjobabom.order.domain.OrderId;

// 주문 환불 단계로 변경시 발생 이벤트
public record OrderRefundEvent(
        OrderId orderId
) {}