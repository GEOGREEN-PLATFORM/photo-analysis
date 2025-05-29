package com.example.photo_analysis.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name="photo-analyse", url="${file.server.host}")
public interface FeignClientService {
    @GetMapping("/file/image/download/{imageId}")
    ResponseEntity<byte[]> analyse(@PathVariable UUID imageId);
}
