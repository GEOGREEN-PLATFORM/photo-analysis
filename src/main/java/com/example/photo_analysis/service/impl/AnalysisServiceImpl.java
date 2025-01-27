package com.example.photo_analysis.service.impl;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import com.example.photo_analysis.model.PhotoDTO;
import com.example.photo_analysis.model.ResponseDTO;
import com.example.photo_analysis.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService {
    private OrtEnvironment env;
    private OrtSession session;

    @Value("${model.path}")
    private String modelPath;

    private static final Logger logger = LoggerFactory.getLogger(AnalysisServiceImpl.class);

    @Override
    @Transactional
    public ResponseDTO analyse(PhotoDTO photo) throws OrtException, IOException {
        logger.debug("Создается окружение и сессия для анализа изображения с айди: {}", photo.getPhotoId());

        env = OrtEnvironment.getEnvironment();
        this.session = env.createSession(modelPath, new OrtSession.SessionOptions());

        logger.debug("Окружение и сессия успешно созданы для изображения с айди: {}", photo.getPhotoId());
        // TODO получение изображения от другого сервиса
        BufferedImage image = ImageIO.read(new File("/app/resources/templates/hog.jpg"));

        logger.debug("Получено изображение с айди: {}", photo.getPhotoId());

        float result = predict(image);

        logger.debug("Получено предсказание для изображения с айди: {} - {}", photo.getPhotoId(), result);

        close();

        return new ResponseDTO(result < 0.5, result);
    }

    private float predict(BufferedImage image) throws OrtException {
        float[] inputData = preprocessImage(image);

        long[] inputShape = {1, 160, 160, 3};
        OnnxTensor inputTensor = OnnxTensor.createTensor(env, FloatBuffer.wrap(inputData), inputShape);

        Map<String, OnnxTensor> inputs = new HashMap<>();
        inputs.put("inputs", inputTensor);

        OrtSession.Result results = session.run(inputs);

        float[][] output = (float[][]) results.get(0).getValue();
        inputTensor.close();

        System.out.println(Arrays.deepToString(output));

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


    private void close() throws OrtException {
        session.close();
        env.close();
    }







}
