package es.g22.action_decoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * KakfaConsumer
 */
@Service
public class Consumer {

    @Autowired
    KakfaProducer kafkaProducer;

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    // Temp Vars
    //private String[] movementTypes = { "RIGHT", "LEFT", "UP", "DOWN", "SELECT" };

    @KafkaListener(topics = "g22_accelData", groupId = "g221")
    public void consume(String message) throws IOException {
        logger.info("Message received: " + message);
        String movDecoded = decodeMovement(message);
        if (movDecoded != null){
            kafkaProducer.send("g22_movData", movDecoded);
            logger.info("Movement Decoded: " + movDecoded);
        }
    }

    private String decodeMovement(String data) {
        // Parse JSON
        ObjectMapper objectMapper = new ObjectMapper();
        MovementData readings;
        try {
            readings = objectMapper.readValue(data, MovementData.class);
        } catch (IOException e) {
            logger.info("Invalid message received");
            return null;
        }
        // Cut first reading as invalid (improves results)
        if (readings.getData().length < 2) {
            logger.info("Not enough readings received");
            return null;
        }
        List<Reading> readingsList = Arrays.asList(readings.getData()).subList(1, readings.getData().length - 1);

        // Get Type of movement (x axis (left,right), y axis (up down), z axis (select))
        // Get average accelaration for each axis and choose the highest
        double mediaX, mediaY, mediaZ;
        mediaX = readingsList.stream().mapToDouble(Reading::getX).average().getAsDouble();
        mediaY = readingsList.stream().mapToDouble(Reading::getY).average().getAsDouble();
        mediaZ = readingsList.stream().mapToDouble(Reading::getZ).average().getAsDouble();

        //logger.info("X: " + mediaX + " Y: " + mediaY + " Z: " + mediaZ);

        // Prepare Output
        DecodedMovement outputMov = new DecodedMovement();
        outputMov.setDevice_id(readings.getDevice_id());

        if (Math.abs(mediaX) >= Math.abs(mediaY) && Math.abs(mediaX) >= Math.abs(mediaZ)) {
            // mediaX é maior
            if (mediaX > 0)
                outputMov.setData("RIGHT");
            else
                outputMov.setData("LEFT");

        } else if (Math.abs(mediaY) >= Math.abs(mediaX) && Math.abs(mediaY) >= Math.abs(mediaZ)) {
            // mediaY é maior
            if (mediaY > 0)
                outputMov.setData("UP");
            else
                outputMov.setData("DOWN");
        } else {
            // mediaZ é maior
            outputMov.setData("SELECT");
        }
        String output;
        try {
            output = objectMapper.writeValueAsString(outputMov);
        } catch (JsonProcessingException e) {
            logger.info("Not enough readings (accelerometer) received");
            return null;
        }
        return output;
    }
}
