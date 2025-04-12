package com.example.photo_analysis.service;

import com.example.photo_analysis.model.PhotoDTO;
import com.example.photo_analysis.model.ResponseDTO;

public interface AnalysisService {
    ResponseDTO analyse(PhotoDTO photo);
}
