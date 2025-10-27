package com.olegf.spingapp.thealthbackend.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;

@Data
@Entity(name = "otps")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ColumnTransformer(
            read = "pgp_sym_decrypt(phone, current_setting('secret'))",
            write = "pgp_sym_encrypt(?::text, current_setting('secret'))"
    )
    private String phone;
    @ColumnTransformer(
            read = "pgp_sym_decrypt(code, current_setting('secret'))",
            write = "pgp_sym_encrypt(?::text, current_setting('secret'))"
    )
    private String code;
    private LocalDateTime createdAt;
    private boolean isExpired;
    private int attempts;

    public Otp(String phone, String code) {
        this.phone = phone;
        this.code = code;
        this.createdAt = LocalDateTime.now();
        this.isExpired = false;
        this.attempts = 0;
    }

    public void incrementAttempts() {
        this.attempts++;
    }

    public boolean checkExpired(TemporalAmount interval) {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(this.createdAt.plus(interval));
    }
}
