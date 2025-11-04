package com.example.orderjobabom.store.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.regex.Pattern;

@ToString
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhoneNumber {

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9+\\-() ]{7,40}$"); // 심플 검증 (국제 포맷 포함)

    @Column(name = "phone", length = 40, nullable = false)
    private String value;

    private PhoneNumber(String value) { this.value = value; }

    public static PhoneNumber of(String raw) {
        if (raw == null || raw.isBlank()) throw new IllegalArgumentException("phone required");
        String normalized = raw.trim();
        if (!PHONE_PATTERN.matcher(normalized).matches())
            throw new IllegalArgumentException("invalid phone format");
        return new PhoneNumber(normalized);
    }
}
