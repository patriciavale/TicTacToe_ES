package es.g22.game_manager;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * KakfaProducer
 */
@Service
public class KafkaProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void send(String topic, String message) {
        kafkaTemplate.send(topic, message);
   }
}
