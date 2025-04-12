package com.example.photo_analysis.exception.custom;

public class CustomOrtException extends RuntimeException {
    public CustomOrtException(String message) {
        super("OrtException with message:" + message);
    }
}
