package edu.campus.library.service;

public class NotFoundBusinessException extends RuntimeException {
    public NotFoundBusinessException(String message) { super(message); }
}
