package com.olegf.spingapp.thealthbackend.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class PhoneValidator {

    public static void validate(String phone) {
        if (!phone.startsWith("+")) throw new IllegalArgumentException("Invalid phone number");
        if (phone.substring(1).matches("\\d")) throw new IllegalArgumentException("Invalid phone number");
        if (phone.length() > 12) throw new IllegalArgumentException("Invalid phone number");
    }
}
