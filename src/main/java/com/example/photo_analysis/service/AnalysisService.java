package com.example.photo_analysis.service;

import com.example.photo_analysis.consumer.dto.PhotoAnalyseInDTO;
import com.example.photo_analysis.model.PhotoDTO;
import com.example.photo_analysis.model.ResponseDTO;
import com.example.photo_analysis.producer.dto.PhotoAnalyseOutDTO;

public interface AnalysisService {
    ResponseDTO analyse(PhotoDTO photo);

    PhotoAnalyseOutDTO analyse(PhotoAnalyseInDTO photo);
}
