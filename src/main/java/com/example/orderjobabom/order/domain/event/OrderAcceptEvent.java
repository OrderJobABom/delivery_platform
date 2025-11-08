package com.example.orderjobabom.order.domain.event;

import com.example.orderjobabom.order.domain.OrderId;
import com.example.orderjobabom.user.domain.UserId;

public record OrderAcceptEvent(
        OrderId orderId,
        UserId userId,
        int totalAmount
) {}
