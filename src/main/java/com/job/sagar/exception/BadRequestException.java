package com.job.sagar.exception;

public class BadRequestException extends BaseException {
    private static final long serialVersionUID = 5041578511654328224L;

    public BadRequestException(String status, int code, String message) {
        super(status, code, message);
    }

    public BadRequestException(String message, String status, int code, String message1) {
        super(message, status, code, message1);
    }

    public BadRequestException(String message, Throwable cause, String status, int code,
                               String message1) {
        super(message, cause, status, code, message1);
    }
}