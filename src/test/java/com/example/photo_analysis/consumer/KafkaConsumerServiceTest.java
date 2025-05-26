package com.example.photo_analysis.consumer;

import com.example.photo_analysis.consumer.dto.PhotoAnalyseInDTO;
import com.example.photo_analysis.producer.KafkaProducerService;
import com.example.photo_analysis.producer.dto.PhotoAnalyseOutDTO;
import com.example.photo_analysis.service.AnalysisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerServiceTest {

    @Mock
    private AnalysisService analysisService;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;

    private PhotoAnalyseInDTO inputDto;
    private PhotoAnalyseOutDTO outputDto;

    @BeforeEach
    void setUp() {
        inputDto = new PhotoAnalyseInDTO();
        // Можно задать поля, если нужно

        outputDto = new PhotoAnalyseOutDTO();
        // Можно задать поля, если нужно
    }

    @Test
    void listen_shouldCallAnalyseAndSendPhoto() {
        // Arrange
        when(analysisService.analyse(inputDto)).thenReturn(outputDto);

        // Act
        kafkaConsumerService.listen(inputDto);

        // Assert
        verify(analysisService).analyse(inputDto);
        verify(kafkaProducerService).sendPhoto(outputDto);
    }

    @Test
    void listen_shouldSendExactlyOneMessage() {
        when(analysisService.analyse(inputDto)).thenReturn(outputDto);

        kafkaConsumerService.listen(inputDto);

        ArgumentCaptor<PhotoAnalyseOutDTO> captor = ArgumentCaptor.forClass(PhotoAnalyseOutDTO.class);
        verify(kafkaProducerService, times(1)).sendPhoto(captor.capture());

        assertThat(captor.getValue()).isEqualTo(outputDto);
    }
}
