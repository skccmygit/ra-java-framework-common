package com.skcc.ra.common.exception;

import java.util.List;

public class BadRequestException extends RuntimeException {

    private List<String> details;

    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(String message, List<String> details) {
        super(message);
        this.details = details;
    }

    public BadRequestException(String message, List<String> details, Throwable cause) {
        super(message, cause);
        this.details = details;
    }

    public List<String> getDetails() {
        return details;
    }
}
