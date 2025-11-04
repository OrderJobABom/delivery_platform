package com.example.orderjobabom.store.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OperatingHours {

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "p_store_daily_hours", joinColumns = @JoinColumn(name = "store_id"))
    @OrderColumn(name = "day_order")
    private List<DailyHours> dailyHours = new ArrayList<>();

    public OperatingHours(List<DailyHours> dailyHours) {
        // 중복 요일 방지/정렬 보장
        if (dailyHours == null || dailyHours.isEmpty())
            throw new IllegalArgumentException("daily hours required");
        // 요일 유일성 체크
        EnumSet<DayOfWeek> seen = EnumSet.noneOf(DayOfWeek.class);
        for (DailyHours d : dailyHours) {
            if (!seen.add(d.getDay())) {
                throw new IllegalArgumentException("duplicate day: " + d.getDay());
            }
        }
        // 정렬 고정
        dailyHours.sort(Comparator.comparing(DailyHours::getDay));
        this.dailyHours = dailyHours;
    }

    public static OperatingHours of(List<DailyHours> dailyHours) {
        return new OperatingHours(dailyHours);
    }

    @Embeddable
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @EqualsAndHashCode
    public static class DailyHours {

        @Enumerated(EnumType.STRING)
        @Column(name = "day_of_week", nullable = false, length = 10)
        private DayOfWeek day;

        @Column(name = "open_time", nullable = false)
        private LocalTime open;

        @Column(name = "close_time", nullable = false)
        private LocalTime close;

        public DailyHours(DayOfWeek day, LocalTime open, LocalTime close) {
            if (day == null) throw new IllegalArgumentException("day required");
            if (open == null || close == null) throw new IllegalArgumentException("open/close required");
            if (!close.isAfter(open)) throw new IllegalArgumentException("close must be after open");
            this.day = day;
            this.open = open;
            this.close = close;
        }

        public static DailyHours of(DayOfWeek day, LocalTime open, LocalTime close) {
            return new DailyHours(day, open, close);
        }
    }
}
