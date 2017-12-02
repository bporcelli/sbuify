package com.cse308.sbuify.customer.preferences;

public class PreferenceException extends RuntimeException {
    public PreferenceException(String s) {
        super(s);
    }

    public PreferenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
