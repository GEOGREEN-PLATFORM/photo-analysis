package com.example.photo_analysis.exception;

import com.example.photo_analysis.exception.custom.CustomIOException;
import com.example.photo_analysis.exception.custom.CustomOrtException;
import com.example.photo_analysis.model.ErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({ HttpMessageNotReadableException.class })
    public ErrorDTO catchHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.error("Произошла ошибка: {}", e.getMessage(), e);
        return new ErrorDTO(
                HttpStatus.BAD_REQUEST,
                e.getRootCause().getLocalizedMessage()
        );
    }

    @ExceptionHandler({ CustomOrtException.class })
    public ErrorDTO catchOrtException(CustomOrtException e) {
        logger.error("Произошла ошибка: {}", e.getMessage(), e);
        return new ErrorDTO(
                HttpStatus.BAD_REQUEST,
                "Unknown error in the image recognition model: " + e.getMessage()
        );
    }

    @ExceptionHandler({ CustomIOException.class })
    public ErrorDTO catchIOException(CustomIOException e) {
        logger.error("Произошла ошибка: {}", e.getMessage(), e);
        return new ErrorDTO(
                HttpStatus.BAD_REQUEST,
                "Unknown error when working with images: " + e.getMessage()
        );
    }
}
