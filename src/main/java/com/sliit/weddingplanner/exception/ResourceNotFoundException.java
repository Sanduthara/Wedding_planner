package com.sliit.weddingplanner.exception;

// OOP: Inheritance
// OOP: Encapsulation
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
