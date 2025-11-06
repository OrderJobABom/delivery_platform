package com.example.orderjobabom.order.domain;

import com.example.orderjobabom.order.presentation.dto.requestDTO.DeliveryRequestDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryInfo {

    @Column(name = "delivery_address")
    private String address;

    @Lob
    private String memo;

    @Column(name = "latitude")
    private double lat;

    @Column(name = "longtitude")
    private double lon;

    public DeliveryInfo(String address, String memo) {
        this.address = address;
        this.memo = memo;
    }

    public void updateAddress(DeliveryRequestDTO dto) {
        this.address = dto.getAddress();
        this.memo = dto.getMemo();
        this.lat = dto.getLat();
        this.lon = dto.getLon();
    }


}
