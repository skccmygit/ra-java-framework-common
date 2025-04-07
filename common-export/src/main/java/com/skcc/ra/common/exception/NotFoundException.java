package com.skcc.ra.common.exception;

import java.util.List;

public class NotFoundException extends RuntimeException {

    private List<String> details;

    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(String message, List<String> details) {
        super(message);
        this.details = details;
    }

    public NotFoundException(String message, List<String> details, Throwable cause) {
        super(message, cause);
        this.details = details;
    }

    public List<String> getDetails() {
        return details;
    }
}
