package com.example.photo_analysis.exception;

import ai.onnxruntime.OrtException;
import com.example.photo_analysis.model.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ HttpMessageNotReadableException.class })
    public ErrorDTO catchHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return new ErrorDTO(
                HttpStatus.BAD_REQUEST,
                e.getRootCause().getLocalizedMessage()
        );
    }

    @ExceptionHandler({ OrtException.class })
    public ErrorDTO catchOrtException(OrtException e) {
        return new ErrorDTO(
                HttpStatus.BAD_REQUEST,
                "Unknown error in the image recognition model: " + e.getLocalizedMessage()
        );
    }

    @ExceptionHandler({ IOException.class })
    public ErrorDTO catchIOException(IOException e) {
        return new ErrorDTO(
                HttpStatus.BAD_REQUEST,
                "Unknown error when working with images: " + e.getLocalizedMessage()
        );
    }
}
