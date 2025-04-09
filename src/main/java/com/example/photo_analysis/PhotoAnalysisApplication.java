package com.example.photo_analysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PhotoAnalysisApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotoAnalysisApplication.class, args);
	}

}
