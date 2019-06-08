package com.ra.course.janus.traintickets.exception;

public enum ErrorMessages {
    SAVE_FAILED("Save failed!"),
    UPDATE_FAILED("Update failed"),
    DELETE_FAILED("Delete failed"),
    FIND_FAILED("Find by id failed"),
    FINDALL_FAILED("Find all failed!"),
    CONFIG_FAILED("Failed to read configuration properties");

    private final String message;

    ErrorMessages(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}