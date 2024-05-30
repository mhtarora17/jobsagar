package com.job.sagar.exception;

public class MicroServiceErrorException extends BaseException {
    private static final long serialVersionUID = 5041578511654328224L;

    public MicroServiceErrorException(String status, int code, String message) {
        super(status, code, message);
    }

    public MicroServiceErrorException(String message, String status, int code, String message1) {
        super(message, status, code, message1);
    }

    public MicroServiceErrorException(String message, Throwable cause, String status, int code,
                                      String message1) {
        super(message, cause, status, code, message1);
    }
}