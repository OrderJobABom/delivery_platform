package com.example.orderjobabom.store.infrastructure.persistence.converter;

import com.example.orderjobabom.store.domain.Staff;
import com.example.orderjobabom.user.domain.UserId;
import jakarta.persistence.AttributeConverter;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class StaffConverter implements AttributeConverter<Set<Staff>, String> {
    @Override
    public String convertToDatabaseColumn(Set<Staff> attribute) {
        return attribute == null
                ? null
                : attribute.stream()
                .map(s-> s.getId().getId().toString())
                .collect(Collectors.joining(","));
    }

    @Override
    public Set<Staff> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return new HashSet<>(); // null-safe 반환
        }

        return Arrays.stream(dbData.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .map(s -> {
                    try {
                        // UUID 형식인 경우에만 Staff 생성
                        return new Staff(UserId.of(UUID.fromString(s)));
                    } catch (IllegalArgumentException e) {
                        // 이름("김철수") 같은 UUID 아님 → 무시
                        return null;
                    }
                })
                .filter(s -> s != null)
                .collect(Collectors.toSet());
    }

}
