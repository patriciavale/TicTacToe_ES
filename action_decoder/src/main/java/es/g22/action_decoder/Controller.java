package es.g22.action_decoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller
 */

@RestController
public class Controller {

    @Autowired
    KakfaProducer kafkaProducer;

    @GetMapping("/")
    public String sendToKafka(){
        kafkaProducer.send("g22_accelData", "hello world");
        return "OK";
    }

}
