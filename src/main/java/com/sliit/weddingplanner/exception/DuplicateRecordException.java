package com.sliit.weddingplanner.exception;

// OOP: Inheritance
// OOP: Encapsulation
public class DuplicateRecordException extends RuntimeException {
    public DuplicateRecordException(String message) {
        super(message);
    }
}
