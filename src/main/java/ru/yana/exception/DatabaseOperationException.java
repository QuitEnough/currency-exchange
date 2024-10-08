package ru.yana.exception;

public class DatabaseOperationException extends RuntimeException{
    public DatabaseOperationException(String message) {
        super(message);
    }
}
