package com.example.photo_analysis.consumer;


import com.example.photo_analysis.consumer.dto.PhotoAnalyseInDTO;
import com.example.photo_analysis.producer.dto.PhotoAnalyseOutDTO;
import com.example.photo_analysis.producer.KafkaProducerService;
import com.example.photo_analysis.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerService {

    @Autowired
    private AnalysisService analysisService;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @KafkaListener(topics = "photo-analyse-req", groupId = "photo-analyse-group",
            containerFactory = "photoAnalyseKafkaListenerContainerFactory")
    public void listen(PhotoAnalyseInDTO photo) {
        PhotoAnalyseOutDTO photoAnalyseOutDTO = analysisService.analyse(photo);
        kafkaProducerService.sendPhoto(photoAnalyseOutDTO);
    }
}
