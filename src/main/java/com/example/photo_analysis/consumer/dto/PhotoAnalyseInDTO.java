package com.example.photo_analysis.consumer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoAnalyseInDTO {

    @NotNull
    private UUID userMarkerId;

    @NotNull
    private Integer protoPosition;

    @NotNull
    private UUID photoId;

}