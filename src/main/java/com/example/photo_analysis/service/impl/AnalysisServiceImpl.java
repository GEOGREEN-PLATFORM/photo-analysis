package com.example.photo_analysis.service.impl;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import com.example.photo_analysis.exception.custom.CustomIOException;
import com.example.photo_analysis.exception.custom.CustomOrtException;
import com.example.photo_analysis.feignClient.FeignClientService;
import com.example.photo_analysis.model.PhotoDTO;
import com.example.photo_analysis.model.ResponseDTO;
import com.example.photo_analysis.service.AnalysisService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService {
    private OrtEnvironment env;
    private OrtSession session;

    @Value("${model.path}")
    private String modelPath;

    @Autowired
    private final FeignClientService feignClientService;

    private static final Logger logger = LoggerFactory.getLogger(AnalysisServiceImpl.class);

    @Override
    @Transactional
    public ResponseDTO analyse(PhotoDTO photo) {
        logger.debug("Создается окружение и сессия для анализа изображения с айди: {}", photo.getPhotoId());

        try {
            env = OrtEnvironment.getEnvironment();
            this.session = env.createSession(modelPath, new OrtSession.SessionOptions());
        }
        catch (OrtException e) {
            throw new CustomOrtException(e.getMessage());
        }

        logger.debug("Окружение и сессия успешно созданы для изображения с айди: {}", photo.getPhotoId());

        BufferedImage image;

        try {
            // TODO получение изображения от другого сервиса
//            image = ImageIO.read(new File("/app/resources/templates/hog.jpg"));
            image = getImageAsBufferedImage(photo.getPhotoId());
        }
        catch (IOException e) {
            throw new CustomIOException(e.getMessage());
        }

        logger.debug("Получено изображение с айди: {}", photo.getPhotoId());

        float result = predict(image);

        logger.info("Получено предсказание для изображения с айди: {} - {}", photo.getPhotoId(), result);

        close();

        return new ResponseDTO(result < 0.5, result);
    }

    private BufferedImage getImageAsBufferedImage(UUID imageId) throws IOException {
        ResponseEntity<byte[]> response = feignClientService.analyse(imageId);

        byte[] imageBytes = response.getBody();
        if (imageBytes == null) {
            throw new IOException("Failed to download image");
        }

        try (ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes)) {
            return ImageIO.read(bis);
        }
    }

    private float predict(BufferedImage image) {
        float[] inputData = preprocessImage(image);
        float[][] output;

        try {

            long[] inputShape = {1, 160, 160, 3};
            OnnxTensor inputTensor = OnnxTensor.createTensor(env, FloatBuffer.wrap(inputData), inputShape);

            Map<String, OnnxTensor> inputs = new HashMap<>();
            inputs.put("inputs", inputTensor);

            OrtSession.Result results = session.run(inputs);

            output = (float[][]) results.get(0).getValue();
            inputTensor.close();
        }
        catch (OrtException e) {
            throw new CustomOrtException(e.getMessage());
        }

        return output[0][0];
    }

    private float[] preprocessImage(BufferedImage image) {
        BufferedImage resizedImage = new BufferedImage(160, 160, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, 160, 160, null);
        g.dispose();

        // Нормализация пикселей в диапазон [0, 1]
        float[] inputData = new float[160 * 160 * 3];
        int index = 0;

        for (int y = 0; y < 160; y++) {
            for (int x = 0; x < 160; x++) {
                int rgb = resizedImage.getRGB(x, y);
                inputData[index++] = ((rgb >> 16) & 0xFF) / 255.0f; // Красный
                inputData[index++] = ((rgb >> 8) & 0xFF) / 255.0f;  // Зеленый
                inputData[index++] = (rgb & 0xFF) / 255.0f;         // Синий
            }
        }
        return inputData;
    }


    private void close() {
        try {
            session.close();
            env.close();
        }
        catch (OrtException e) {
            throw new CustomOrtException(e.getMessage());
        }
    }







}
