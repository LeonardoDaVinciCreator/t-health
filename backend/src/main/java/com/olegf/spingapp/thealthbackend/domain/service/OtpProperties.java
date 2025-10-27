package com.olegf.spingapp.thealthbackend.domain.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@AllArgsConstructor
@ConfigurationProperties("otp")
public class OtpProperties {
    private String interval;
    private int attemptsThreshold;
}
