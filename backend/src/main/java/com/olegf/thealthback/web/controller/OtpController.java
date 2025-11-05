package com.olegf.thealthback.web.controller;

import com.olegf.thealthback.otp.Otp;
import com.olegf.thealthback.otp.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/otp")
public class OtpController {

    private final OtpService otpService;

    @PostMapping
    public Otp generateOtpCode(@RequestBody String phone) {
        return otpService.generateOtp(phone);
    }

    @PostMapping("/verify")
    public void checkOtp(@RequestBody Otp.Check check) {
        otpService.checkOtp(check);
    }
}
