package com.sliit.weddingplanner.exception;

// OOP: Inheritance
// OOP: Encapsulation
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
