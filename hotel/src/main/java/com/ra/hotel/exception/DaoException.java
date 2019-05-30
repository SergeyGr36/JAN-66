package com.ra.hotel.exception;

public class DaoException extends Exception {

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
