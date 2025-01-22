package com.example.photo_analysis.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
    @NotNull
    private Boolean isHogweed;

    @DecimalMin("0.00")
    @DecimalMax("1.00")
    @NotNull
    private Float prediction;
}
