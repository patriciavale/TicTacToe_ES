package es.g22.game_manager;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @Autowired
    KafkaProducer kafkaProducer;

    @Autowired
    Listeners listeners;

    @Autowired
    GameLogic gameLogic;

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "g22_movData", groupId = "g22")
    public void consume(String message) throws IOException {
        logger.info("Message received: " + message);

        // Do stuff with message
        // Decode message
        ObjectMapper objectMapper = new ObjectMapper();
        DecodedMovement movement;
        try {
            movement = objectMapper.readValue(message, DecodedMovement.class);
        } catch (IOException e) {
            logger.error("Invalid message received");
            return;
        }
        //logger.info("H»ASFJSAFKSAOFBAFOASFHASOFSHFBSAOFHBAFOSAFBSOAFHFBOAFHSAOFHSAFBSAHFSAB");
        if(!listeners.getRelations().containsKey(movement.getDevice_id())){
            logger.info("Device in movement isn't in any game");
            return;
        }
        //Get Game ID
        Long id = listeners.getRelations().get(movement.getDevice_id());

        GameAction action = gameLogic.executeMovement(id, movement);

        if(action == null)
            return;
        
        String output;
        try {
            output = objectMapper.writeValueAsString(action);
        } catch (JsonProcessingException e) {
            logger.error("Couldn't transform Game Action into Json to send");
            return;
        }
        logger.info("Sending game action to kafka: " + output);
        kafkaProducer.send("g22_gameData", output);
    }

}