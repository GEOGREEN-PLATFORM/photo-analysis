package com.example.photo_analysis.controller;

import ai.onnxruntime.OrtException;
import com.example.photo_analysis.model.PhotoDTO;
import com.example.photo_analysis.model.ResponseDTO;
import com.example.photo_analysis.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/analyse")
@RequiredArgsConstructor
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    private static final Logger logger = LoggerFactory.getLogger(AnalysisController.class);

    @PostMapping
    public ResponseDTO analyse(@RequestBody PhotoDTO photoDTO) throws IOException, OrtException {
        logger.debug("Получен запрос /analyse на фото с айди: {}", photoDTO);
        return analysisService.analyse(photoDTO);
    }
}
