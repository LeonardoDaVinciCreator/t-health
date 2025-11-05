package com.olegf.thealthback.otp;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
public class Otp {
    private int value;
    private int attempts;
    private LocalDateTime createdAt;

    public Otp(int value) {
        this.value = value;
        this.attempts = 0;
        this.createdAt = LocalDateTime.now();
    }

    public void incrementAttempts() { attempts++; }

    public boolean isExpired(String interval) {
        var now = LocalDateTime.now();
        var threshold = this.createdAt.plus(Duration.parse(interval));

        return now.isAfter(threshold);
    }

    @Data
    @AllArgsConstructor
    public static class Check {
        private int value;
        private String phone;
    }
}
