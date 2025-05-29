package com.example.photo_analysis.producer;

import com.example.photo_analysis.producer.dto.PhotoAnalyseOutDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, PhotoAnalyseOutDTO> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, PhotoAnalyseOutDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPhoto(PhotoAnalyseOutDTO photo) {
        kafkaTemplate.send("photo-analyse-resp", photo);
    }
}
