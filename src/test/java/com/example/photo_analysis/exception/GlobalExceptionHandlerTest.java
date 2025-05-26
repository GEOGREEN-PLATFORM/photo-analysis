package com.example.photo_analysis.exception;

import com.example.photo_analysis.exception.custom.CustomIOException;
import com.example.photo_analysis.exception.custom.CustomOrtException;
import com.example.photo_analysis.model.ErrorDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void catchHttpMessageNotReadableException_shouldReturnBadRequest() {
        Throwable cause = new RuntimeException("Malformed JSON");
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Error", cause, null);

        ResponseEntity<ErrorDTO> response = handler.catchHttpMessageNotReadableException(ex);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("Malformed JSON");
    }

    @Test
    void catchOrtException_shouldReturnBadRequest() {
        CustomOrtException ex = new CustomOrtException("ORT model failed");

        ResponseEntity<ErrorDTO> response = handler.catchOrtException(ex);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("Unknown error in the image recognition model: " + "OrtException with message:" + "ORT model failed");
    }

    @Test
    void catchIOException_shouldReturnBadRequest() {
        CustomIOException ex = new CustomIOException("File not found");

        ResponseEntity<ErrorDTO> response = handler.catchIOException(ex);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("Unknown error when working with images: " + "IOException with message:" + "File not found");
    }
}
