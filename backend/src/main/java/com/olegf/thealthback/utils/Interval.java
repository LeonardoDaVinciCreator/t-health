package com.olegf.thealthback.utils;

import lombok.Data;

@Data
public class Interval {
    private long value;
    private Unit unit;

    public String valueString() {
        return switch (this.unit) {

            case WEEK -> this.value + " week";
            case DAY -> this.value + " days";// 24 * 60 * 60 * 1000 (ms)
            case MONTH -> this.value + " month";
        };
    }

    public enum Unit {
        WEEK,
        DAY,
        MONTH
    }
}