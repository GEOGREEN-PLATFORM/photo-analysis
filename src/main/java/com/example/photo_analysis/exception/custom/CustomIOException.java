package com.example.photo_analysis.exception.custom;

public class CustomIOException extends RuntimeException {
    public CustomIOException(String message) {
        super("IOException with message:" + message);
    }
}
