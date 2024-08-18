package com.desafioagi.desafioagi.exception;

public class DatabaseConnectionException extends RuntimeException {
    public DatabaseConnectionException(String message, Throwable ex) {
        super(message);
    }
}
