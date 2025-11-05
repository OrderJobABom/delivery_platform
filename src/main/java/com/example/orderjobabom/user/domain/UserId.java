package com.example.orderjobabom.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserId {

    @Column(length = 50, name = "user_id")
    private UUID id;

    public UserId(UUID id) {
        this.id = id;
    }

    public static UserId of(UUID id) {
        return new UserId(id);
    }


}
