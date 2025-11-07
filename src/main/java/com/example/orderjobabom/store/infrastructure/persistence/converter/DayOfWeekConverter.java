package com.example.orderjobabom.store.infrastructure.persistence.converter;

import jakarta.persistence.AttributeConverter;
import org.springframework.util.StringUtils;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DayOfWeekConverter implements AttributeConverter<List<DayOfWeek>, String> {
    @Override
    public String convertToDatabaseColumn(List<DayOfWeek> attribute) {
        return attribute == null ? null : attribute.stream().map(DayOfWeek::name).collect(Collectors.joining(","));
    }

    @Override
    public List<DayOfWeek> convertToEntityAttribute(String dbData) {
        return StringUtils.hasText(dbData) ? Arrays.stream(dbData.split(",")).map(this::toConvert).map(DayOfWeek::valueOf).toList() : null;
    }

    private String toConvert(String dayOfWeek) {
        Map<String, String> data = Map.of(
                "MON", "MONDAY",
                "TUE", "TUESDAY",
                "WED", "WEDNESDAY",
                "THU", "THURSDAY",
                "FRI", "FRIDAY",
                "SAT", "SATURDAY",
                "SUN", "SUNDAY"

        );

        return data.getOrDefault(dayOfWeek, dayOfWeek);
    }
}
