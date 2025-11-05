package com.olegf.thealthback.web.controller;

import com.olegf.thealthback.otp.OtpException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OtpControllerAdvice {

    @ExceptionHandler(OtpException.class)
    public ResponseEntity<String> handleOtpException(OtpException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
