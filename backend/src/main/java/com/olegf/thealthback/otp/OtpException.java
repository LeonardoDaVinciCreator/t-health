package com.olegf.thealthback.otp;

public sealed class OtpException
        extends RuntimeException
        permits OtpException.OtpNotExistsException, OtpException.OtpAttemptsExceeded, OtpException.OtpMismatchException, OtpException.OtpExpiredException {

    public OtpException(String message) {
        super(message);
    }

    public static final class OtpNotExistsException extends OtpException {
        public OtpNotExistsException() {
            super("Otp not exists");
        }
    }

    public static final class OtpAttemptsExceeded extends OtpException {
        public OtpAttemptsExceeded() {
            super("Attempt count exceeded");
        }
    }

    public static final class OtpMismatchException extends OtpException {
        public OtpMismatchException() {
            super("Otp mismatch");
        }
    }

    public static final class OtpExpiredException extends OtpException {
        public OtpExpiredException() {
            super("Otp expired");
        }
    }
}
