package com.ra.course.janus.traintickets.exception;

public enum ErrorMessages {
    SAVE_FAILED("Save failed!"),
    UPDATE_FAILED("Update failed"),
    DELETE_FAILED("Delete failed"),
    FIND_FAILED("Find by id failed"),
    FINDALL_FAILED("Find all failed!");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }
}
