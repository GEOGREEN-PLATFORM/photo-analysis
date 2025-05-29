package com.example.photo_analysis.service.impl;

import com.example.photo_analysis.consumer.dto.PhotoAnalyseInDTO;
import com.example.photo_analysis.exception.custom.CustomIOException;
import com.example.photo_analysis.feignClient.FeignClientService;
import com.example.photo_analysis.model.PhotoDTO;
import com.example.photo_analysis.model.ResponseDTO;
import com.example.photo_analysis.producer.dto.PhotoAnalyseOutDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnalysisServiceImplTest {

    private FeignClientService feignClientService;
    private AnalysisServiceImpl service;

    @BeforeEach
    void setUp() {
        feignClientService = mock(FeignClientService.class);
        service = spy(new AnalysisServiceImpl(feignClientService));
        service.setModelPath("src/main/resources/recognitionModel/model.onnx");
    }

    @Test
    void analyse_shouldThrowCustomIOException_whenEmptyImage() {
        UUID photoId = UUID.randomUUID();

        // Подделываем ответ: пустой массив
        when(feignClientService.analyse(photoId)).thenReturn(ResponseEntity.ok(new byte[0]));

        PhotoDTO dto = new PhotoDTO();

        // Ничего не подменяем — дадим упасть predict()
        assertThatThrownBy(() -> service.analyse(dto))
                .isInstanceOf(CustomIOException.class)
                .hasMessageContaining("IOException");
    }


    @Test
    void analysePhotoAnalyseIn_shouldReturnDefaultPrediction_whenSkippedOnnx() {
        // arrange
        UUID markerId = UUID.randomUUID();
        UUID photoId = UUID.randomUUID();

        PhotoAnalyseInDTO inDto = new PhotoAnalyseInDTO();
        inDto.setUserMarkerId(markerId);
        inDto.setPhotoId(photoId);
        inDto.setProtoPosition(5);

        byte[] fakeImageBytes = new byte[]{1, 2, 3}; // что угодно

        when(feignClientService.analyse(photoId)).thenReturn(ResponseEntity.ok(fakeImageBytes));

        // stub predict (вместо реального onnx)
        doReturn(0.7f).when(service).predict(any());

        // act
        PhotoAnalyseOutDTO result = service.analyse(inDto);

        // assert
        assertThat(result.getUserMarkerId()).isEqualTo(markerId);
        assertThat(result.getProtoPosition()).isEqualTo(5);
        assertThat(result.getPrediction()).isEqualTo(30); // 0.7 * 100
    }

    @Test
    void analyse_shouldReturnPrediction_whenStubbedPredict() {
        UUID photoId = UUID.randomUUID();
        byte[] fakeImage = new byte[]{42};

        when(feignClientService.analyse(photoId)).thenReturn(ResponseEntity.ok(fakeImage));
        doReturn(0.3f).when(service).predict(any());

        PhotoDTO dto = new PhotoDTO(photoId);
        ResponseDTO response = service.analyse(dto);

        assertThat(response.getPrediction()).isEqualTo(0.3f);
        assertThat(response.getIsHogweed()).isTrue(); // >= 0.25
    }
}
