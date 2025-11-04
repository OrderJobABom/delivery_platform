package com.example.orderjobabom.store.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@ToString
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreAddress {

    @Column(nullable = false, length = 100)
    private String line1;

    @Column(length = 100)
    private String line2;

    @Column(nullable = false, length = 30)
    private String city;

    @Column(nullable = false, length = 20)
    private String zip;

    public static StoreAddress of(String line1, String line2, String city, String zip){
        if(line1 == null || line1.isBlank()) throw new IllegalArgumentException("line1 required");
        if(city == null || city.isBlank()) throw new IllegalArgumentException("city required");
        if(zip == null || zip.isBlank()) throw new IllegalArgumentException("zip required");
        return new StoreAddress(line1, line2, city, zip);
    }
}
