package com.example.orderjobabom.order.domain;

public enum OrderStatus {

    PAYMENT_CONFIRM, // 결제 확인
    PAYMENT_FAILED,
    ORDER_WAITING, // 주문 대기 중
    ORDER_ACCEPT, // 주문 접수

    PREPARING, // 배달 준비 중
    DELIVERING, // 배달 중
    DELIVERY_DONE, // 배달 완료

    ORDER_DONE, // 주문 처리
    ORDER_CANCEL, // 주문 취소
    ORDER_REFUND, // 환불
    EXCHANGE // 교환

}
