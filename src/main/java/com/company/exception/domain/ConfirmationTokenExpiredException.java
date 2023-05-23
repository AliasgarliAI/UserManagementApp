package com.company.exception.domain;

public class ConfirmationTokenExpiredException extends Exception {
    public ConfirmationTokenExpiredException(String message) {
        super(message);
    }
}
