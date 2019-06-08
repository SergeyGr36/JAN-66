package com.ra.course.janus.traintickets.exception;

public class ConfigException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ConfigException() {
        super();
    }
    public ConfigException(final String s) {
        super(s);
    }
    public ConfigException(final String s, final Throwable throwable) {
        super(s, throwable);
    }
    public ConfigException(final Throwable throwable) {
        super(throwable);
    }
}
