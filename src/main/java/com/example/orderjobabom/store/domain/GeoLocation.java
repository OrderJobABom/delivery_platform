package com.example.orderjobabom.store.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@ToString
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GeoLocation {

    @Column(name = "lat", nullable = false)
    private Double lat;

    @Column(name = "lng", nullable = false)
    private Double lng;

    public static GeoLocation of(Double lat, Double lng) {
        if (lat == null || lat < -90 || lat > 90) throw new IllegalArgumentException("lat -90..90");
        if (lng == null || lng < -180 || lng > 180) throw new IllegalArgumentException("lng -180..180");
        return new GeoLocation(lat, lng);
    }
}