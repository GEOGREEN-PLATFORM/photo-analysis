package com.example.photo_analysis.service;

import ai.onnxruntime.OrtException;
import com.example.photo_analysis.model.PhotoDTO;
import com.example.photo_analysis.model.ResponseDTO;

import java.io.IOException;

public interface AnalysisService {
    ResponseDTO analyse(PhotoDTO photo) throws IOException, OrtException;
}
