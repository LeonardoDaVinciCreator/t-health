package com.olegf.thealthback.utils;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Interval {
    private long value;
    private Unit unit;

    public LocalDateTime dateFrom() {
        return switch (this.unit) {
            case WEEK -> LocalDateTime.now().minusWeeks(this.value);
            case DAY -> LocalDateTime.now().minusDays(this.value);
            case MONTH -> LocalDateTime.now().minusMonths(this.value);
        };
    }

    public enum Unit {
        WEEK,
        DAY,
        MONTH
    }
}