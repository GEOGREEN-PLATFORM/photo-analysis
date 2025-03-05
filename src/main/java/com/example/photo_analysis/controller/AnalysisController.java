package com.example.photo_analysis.controller;

import com.example.photo_analysis.model.PhotoDTO;
import com.example.photo_analysis.model.ResponseDTO;
import com.example.photo_analysis.service.AnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analyse")
@RequiredArgsConstructor
@Tag(name = "Анализ фото", description = "Позволяет проанализировать фотографию на наличие борщевика")
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    private static final Logger logger = LoggerFactory.getLogger(AnalysisController.class);

    @PostMapping
    @Operation(
            summary = "Анализ",
            description = "Позволяет проанализировать фотографию и получить вероятность наличия борщевика на фото"
    )
    @ApiResponse(responseCode = "400", description = "Http message not readable exception")
    public ResponseDTO analyse(@RequestBody @Parameter(description = "Айди фотографии", required = true) PhotoDTO photoDTO) {
        logger.debug("Получен запрос /analyse на фото с айди: {}", photoDTO);
        return analysisService.analyse(photoDTO);
    }
}
