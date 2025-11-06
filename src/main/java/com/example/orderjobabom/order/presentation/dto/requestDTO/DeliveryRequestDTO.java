package com.example.orderjobabom.order.presentation.dto.requestDTO;

import lombok.Getter;

@Getter
public class DeliveryRequestDTO {

    private String address;
    private String memo;
    private double lat;
    private double lon;
}
