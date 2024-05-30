package com.job.sagar.exception;

public class ExternalServiceErrorException extends BaseException {

    private static final long serialVersionUID = -8319788570051642500L;

    public ExternalServiceErrorException(String status, int code, String message) {
        super(status, code, message);
    }

    public ExternalServiceErrorException(String message, String status, int code, String message1) {
        super(message, status, code, message1);
    }

    public ExternalServiceErrorException(String message, Throwable cause, String status, int code,
                                         String message1) {

        super(message, cause, status, code, message1);
    }
}
