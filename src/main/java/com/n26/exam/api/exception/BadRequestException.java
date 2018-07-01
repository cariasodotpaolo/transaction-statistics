package com.n26.exam.api.exception;

public class BadRequestException extends Exception {

    public BadRequestException(String message) {
        super(message);
    }
}
