package com.olegf.thealthback.otp;

import com.olegf.thealthback.config.OtpProps;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class OtpService {
    private final OtpProps otpProps;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Map<String, Otp> phoneToOtp = new HashMap<>();

    public Otp generateOtp(String phone) {
        var otp = new Otp(Math.abs(new Random().nextInt()) << 5);
        phoneToOtp.put(phone, otp);

        return otp;
    }

    public void checkOtp(Otp.Check check) {
        if (!phoneToOtp.containsKey(check.getPhone())) { throw new OtpException.OtpNotExistsException(); }
        var existingOtp = phoneToOtp.get(check.getPhone());
        
        if (existingOtp.isExpired(otpProps.getInterval())) { throw new OtpException.OtpExpiredException(); }
        if (existingOtp.getAttempts() > otpProps.getAttemptsThreshold()) { throw new OtpException.OtpAttemptsExceeded(); }

        if (existingOtp.getValue() != check.getValue()) {
            existingOtp.incrementAttempts();
            throw new OtpException.OtpMismatchException();
        }
        
        phoneToOtp.remove(check.getPhone());
    }

    @Scheduled(fixedRate = 100)
    void checkExpired() {
        var isRemoved = phoneToOtp.values().removeIf(otp -> otp.isExpired(otpProps.getInterval()));

        if (isRemoved) {
            log.info("OTP removed");
        }
    }
}
