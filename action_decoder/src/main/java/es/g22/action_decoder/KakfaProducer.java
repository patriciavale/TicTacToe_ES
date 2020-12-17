package es.g22.action_decoder;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * KakfaProducer
 */
@Service
public class KakfaProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void send(String topic, String message) {
        kafkaTemplate.send(topic, message);
   }
}
