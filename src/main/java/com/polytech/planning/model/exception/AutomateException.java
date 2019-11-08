package com.polytech.planning.model.exception;

public class AutomateException extends Exception {

    public AutomateException(String message) {
        super(message);
    }

    public AutomateException(String message, Throwable cause) {
        super(message, cause);
    }
}
