package com.example.orderjobabom.order.domain;

import com.example.orderjobabom.user.domain.UserId;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Orderer {

    @Column(name = "orderer_id")
    private UserId id;

    @Column(name = "orderer_name")
    private String name;

    public Orderer(UserId id, String name) {
        this.id = id;
        this.name = name;
    }


}
