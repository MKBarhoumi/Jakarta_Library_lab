package edu.campus.library.service;

public class ConflictBusinessException extends RuntimeException {
    public ConflictBusinessException(String message) { super(message); }
}
