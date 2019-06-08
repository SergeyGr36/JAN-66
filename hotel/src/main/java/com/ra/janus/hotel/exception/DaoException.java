package com.ra.janus.hotel.exception;

public class DaoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DaoException() {
        super();
    }

    public DaoException(final String message) {
        super(message);
    }

    public DaoException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
