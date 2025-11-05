package com.olegf.thealthback.otp;

import com.olegf.thealthback.config.OtpProps;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class OtpServiceTest {

    private final OtpProps otpProps = new OtpProps(10, "PT1M");
    private OtpService otpService = new OtpService(otpProps);

    @Test
    public void should_generate_otp() {
        // given
        String phone = "+79994436598";

        // when
        Otp result = otpService.generateOtp(phone);

        // then
        Map<String, Otp> innerMap = (Map<String, Otp>) ReflectionTestUtils.getField(otpService, "phoneToOtp");
        assertThat(innerMap).hasSize(1);
        assertThat(innerMap).containsEntry(phone, result);

        Otp innerOtp = innerMap.get(phone);

        assertThat(result.getValue()).isEqualTo(innerOtp.getValue());
        assertThat(result.getCreatedAt().isBefore(LocalDateTime.now())).isTrue();
        assertThat(result.getAttempts()).isEqualTo(0);
    }
}
