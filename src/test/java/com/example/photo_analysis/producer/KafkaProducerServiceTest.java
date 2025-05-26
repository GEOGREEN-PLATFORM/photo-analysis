package com.example.photo_analysis.producer;

import com.example.photo_analysis.producer.dto.PhotoAnalyseOutDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class KafkaProducerServiceTest {

    @Test
    @DisplayName("sendPhoto должен отправлять сообщение в Kafka с правильной темой и данными")
    void sendPhoto_shouldSendMessageToKafka() {
        // Given
        KafkaTemplate<String, PhotoAnalyseOutDTO> kafkaTemplate = mock(KafkaTemplate.class);
        KafkaProducerService service = new KafkaProducerService(kafkaTemplate);

        UUID id = UUID.randomUUID();

        PhotoAnalyseOutDTO dto = new PhotoAnalyseOutDTO();
        dto.setUserMarkerId(id);
        dto.setPrediction(94);

        // When
        service.sendPhoto(dto);

        // Then
        ArgumentCaptor<PhotoAnalyseOutDTO> captor = ArgumentCaptor.forClass(PhotoAnalyseOutDTO.class);
        verify(kafkaTemplate).send(eq("photo-analyse-resp"), captor.capture());
        PhotoAnalyseOutDTO sent = captor.getValue();

        assertThat(sent).isNotNull();
        assertThat(sent.getUserMarkerId()).isEqualTo(id);
        assertThat(sent.getPrediction()).isEqualTo(94);
    }
}
