package ru.volkov.batch.common.exceptions;

public class CustomRetryableException extends Exception {

    public CustomRetryableException() {
        super();
    }

    public CustomRetryableException(String msg) {
        super(msg);
    }
}
