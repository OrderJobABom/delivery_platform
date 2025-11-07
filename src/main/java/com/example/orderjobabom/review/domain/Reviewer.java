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
public class Reviewer {

    @Column(name = "reviewer_id", nullable = false)
    private UUID id; // UserId 대신 UUID 직렬화

    @Column(name = "reviewer_name", length = 50, nullable = false)
    private String name; // 닉네임 or username

    @Column(name = "reviewer_email", length = 100)
    private String email;

    public static Reviewer of(UUID id, String name, String email) {
        return new Reviewer(id, name, email);
    }
}
