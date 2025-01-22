package com.example.photo_analysis.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDTO {
    @NotNull
    private Boolean isHogweed;
}
