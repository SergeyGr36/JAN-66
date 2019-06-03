package com.ra.course.janus.faculty.exception;

public class DaoException
    extends RuntimeException {

    public static final long serialVersionUID = -4841130589162737690L;

    public DaoException() {
        super();
    }
    public DaoException(final String s) {
        super(s);
    }
    public DaoException(final String errorMessage, final Throwable err){
            super(errorMessage,err);
        }

}
