package com.ra.course.janus.faculty.exceptions;

public class DAOExceptions extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DAOExceptions(final Throwable throwable) {
        super(throwable);
    }
}
