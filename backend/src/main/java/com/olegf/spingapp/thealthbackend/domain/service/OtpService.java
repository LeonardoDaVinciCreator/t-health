package com.olegf.spingapp.thealthbackend.domain.service;

import com.olegf.spingapp.thealthbackend.domain.entity.Otp;
import com.olegf.spingapp.thealthbackend.domain.repository.OtpRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

import static com.olegf.spingapp.thealthbackend.validator.PhoneValidator.validate;

@Service
@Slf4j
@AllArgsConstructor
public class OtpService {
    private final OtpRepository otpRepository;
    private final OtpProperties otpProperties;
    private final PhoneService phoneService;

    public Otp handleOtp(String phone) {
        validate(phone);
        Otp otp = otpRepository.existsByPhone(phone)
                ? otpRepository.findByPhone(phone).get()
                : new Otp(phone, UUID.randomUUID().toString()); //else

        log.debug("OTP for {}", otp);
        phoneService.sendOtp(otp);

        return otpRepository.save(otp);
    }

    public void verify(String otpCode, String phone) {
        otpRepository
                .findByPhone(phone)
                .ifPresentOrElse(
                        otp -> doVerify(otp, otpCode),
                        () -> { throw new OtpException.NotFoundException(); }
                );
    }

    private void doVerify(Otp otp, String otpCode) {
        if (!otp.getCode().equals(otpCode)) {
            otp.incrementAttempts();
            throw new OtpException.MissmatchException();
        }

        if (otp.checkExpired(Duration.parse(otpProperties.getInterval()))) {
            throw new OtpException.ExpiredException();
        }

        if (otp.getAttempts() > otpProperties.getAttemptsThreshold()) {
            throw new OtpException.AttemptsException();
        }
    }
}
