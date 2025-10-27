package com.olegf.spingapp.thealthbackend.domain.service;

public sealed class OtpException
        extends RuntimeException
        permits OtpException.MissmatchException, OtpException.ExpiredException, OtpException.AttemptsException, OtpException.NotFoundException {

    public OtpException(String message) {
        super(message);
    }

    public static final class MissmatchException extends OtpException {
        public MissmatchException() {
            super("OTP code mismatch");
        }
    }

    public static final class ExpiredException extends OtpException {
        public ExpiredException() {
            super("OTP expired");
        }
    }

    public static final class AttemptsException extends OtpException {
        public AttemptsException() {
            super("OTP attempts exceeded");
        }
    }

    public static final class NotFoundException extends OtpException {
        public NotFoundException() {
            super("OTP not found");
        }
    }
}
