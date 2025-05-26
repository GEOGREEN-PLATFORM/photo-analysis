package com.example.photo_analysis.controller;

import com.example.photo_analysis.model.PhotoDTO;
import com.example.photo_analysis.model.ResponseDTO;
import com.example.photo_analysis.service.AnalysisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.UUID;

@WebMvcTest(AnalysisController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(AnalysisControllerTest.MockConfig.class)
class AnalysisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AnalysisService analysisService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public AnalysisService analysisService() {
            return Mockito.mock(AnalysisService.class);
        }
    }

    @Test
    void analyse_shouldReturnPredictionResult() throws Exception {
        // Arrange
        PhotoDTO photoDTO = new PhotoDTO();
        photoDTO.setPhotoId(UUID.randomUUID());

        ResponseDTO response = new ResponseDTO();
        response.setPrediction(94.0F);

        Mockito.when(analysisService.analyse(any(PhotoDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/analyse/predict")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(photoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prediction").value(94.0F));
    }
}
