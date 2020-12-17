package es.g22.web_server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * KafkaConsumer
 */

@Service
public class KafkaConsumer {

    @Autowired
    private SimpMessagingTemplate template;

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "g22_gameData", groupId = "g22")
    public void consume(String message) throws IOException {

        logger.info("Received message: "  + message);

        // Decode message
        ObjectMapper objectMapper = new ObjectMapper();
        GameAction action;
        try {
            action = objectMapper.readValue(message, GameAction.class);
        } catch (IOException e) {
            logger.error("Invalid message received");
            return;
        }

        logger.info("Sending action to topic: " + "game/" + action.getGame_id().toString());
        //Send through web socket
        template.convertAndSend("/game/" + action.getGame_id().toString(), action);

    }

    
}