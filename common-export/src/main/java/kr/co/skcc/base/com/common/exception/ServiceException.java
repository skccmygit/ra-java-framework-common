package kr.co.skcc.base.com.common.exception;

import java.util.List;

public class ServiceException extends RuntimeException {

    private List<String> details;

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(String message, List<String> details) {
        super(message);
        this.details = details;
    }

    public ServiceException(String message, List<String> details, Throwable cause) {
        super(message, cause);
        this.details = details;
    }

    public List<String> getDetails() {
        return details;
    }
}
