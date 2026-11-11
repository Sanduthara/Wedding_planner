package com.sliit.weddingplanner.exception;

// OOP: Inheritance
// OOP: Encapsulation
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
