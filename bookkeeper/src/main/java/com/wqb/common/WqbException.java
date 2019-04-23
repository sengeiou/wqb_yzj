package com.wqb.common;

/**
 * @author Shoven
 * @since 2019-04-08 20:04
 */
public class WqbException extends RuntimeException {

    public WqbException() {
        super();
    }

    public WqbException(String message) {
        super(message);
    }

    public WqbException(String message, Throwable cause) {
        super(message, cause);
    }
}
