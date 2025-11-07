package com.example.orderjobabom.review.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Replyer {

    @Column(name = "replyer_id", nullable = true)
    private UUID id;

    @Column(name = "replyer_name", nullable = true, length = 50)
    private String name;

    @Column(name = "replyer_role", length = 20)
    private String role; // OWNER or MANAGER

    public static Replyer of(UUID id, String name, String role) {
        return new Replyer(id, name, role);
    }
}
