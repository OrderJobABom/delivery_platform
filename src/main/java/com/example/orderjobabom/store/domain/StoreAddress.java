package com.example.orderjobabom.store.domain;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@ToString
@Getter
@Embeddable
@Access(AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreAddress {

    @Column(length = 100, nullable = false)
    private String address;
    private double lat;
    private double lon;

    @Builder
    public StoreAddress(String address, double lat, double lon){
        this.address = address;
        this.lat = lat;
        this.lon = lon;
    }

    protected static StoreAddress of(String address) {
        return StoreAddress.builder()
                .address(address)
                .build();
    }
}
