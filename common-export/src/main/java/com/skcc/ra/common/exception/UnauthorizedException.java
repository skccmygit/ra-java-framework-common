package com.skcc.ra.common.exception;

/**
 * UnauthorizedException.java
 * : 작성필요
 *
 * @author Lee Ki Jung(jellyfishlove@sk.com)
 * @version 1.0.0
 * @since 2022-03-11, 최초 작성
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(String error) {
        super(error);
    }

    public UnauthorizedException(String error, Throwable cause) {
        super(error, cause);
    }
}
