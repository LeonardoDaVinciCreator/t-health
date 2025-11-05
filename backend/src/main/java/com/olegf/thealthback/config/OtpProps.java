package com.olegf.thealthback.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@AllArgsConstructor
@ConfigurationProperties("otp.props")
public class OtpProps {
    private int attemptsThreshold;
    private String interval;
}
