package com.example.photo_analysis.service;

import ai.onnxruntime.OrtException;
import com.example.photo_analysis.model.PhotoDTO;

import java.io.IOException;

public interface AnalysisService {
    Boolean analyse(PhotoDTO photo) throws IOException, OrtException;
}
