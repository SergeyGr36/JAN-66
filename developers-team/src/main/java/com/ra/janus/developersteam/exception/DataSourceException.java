package com.ra.janus.developersteam.exception;

public class DataSourceException extends RuntimeException  {
    private static final long serialVersionUID = 1L;

    public DataSourceException() {
        super();
    }
    public DataSourceException(final String s) {
        super(s);
    }
    public DataSourceException(final String s, final Throwable throwable) {
        super(s, throwable);
    }
    public DataSourceException(final Throwable throwable) {
        super(throwable);
    }
}