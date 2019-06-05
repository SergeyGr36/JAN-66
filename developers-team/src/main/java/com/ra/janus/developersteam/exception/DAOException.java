package com.ra.janus.developersteam.exception;

public class DAOException extends RuntimeException  {
    private static final long serialVersionUID = 1L;

    public DAOException() {
        super();
    }
    public DAOException(final String s) {
        super(s);
    }
    public DAOException(final String s, final Throwable throwable) {
        super(s, throwable);
    }
    public DAOException(final Throwable throwable) {
        super(throwable);
    }
}
