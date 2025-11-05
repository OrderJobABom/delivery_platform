package com.example.orderjobabom.store.domain;

import com.example.orderjobabom.store.infrastructure.persistence.converter.DayOfWeekConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

@ToString
@Getter
@Embeddable
@Access(AccessType.FIELD)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OperatingInfo {
    private LocalTime startHour;
    private LocalTime endHour;

    @Convert(converter = DayOfWeekConverter.class)
    private List<DayOfWeek> weekdays;
}
