package es.g22.data_proxy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Controller
 */

@RestController
public class Controller {
    
    @Autowired
    KakfaProducer kafkaProducer;

    @Autowired
    DeviceManager deviceManager;

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @PostMapping("/send")
    public Map<String, String> sendToKafka(@RequestBody String data){
        HashMap<String, String> map = new HashMap<>();
        logger.info("Received/Sending message: " + data);
        kafkaProducer.send("g22_accelData", data);
        map.put("status", "OK");
        return map;
    }
    
    @GetMapping(value="/getid")
    public String getNewID() {
        //Generate new id
        String id = deviceManager.newId();
        deviceManager.keepAlive(id);
        logger.info("New id generated: " + id);
        return id;
    }

    @PostMapping(value="/alive")
    public String keepAlive(@RequestParam(value="id") String id){
        if (deviceManager.keepAlive(id)){
            logger.info("Keep alive: " + id);
            return "OK";
        }
        return "ERROR"; 
    }

    @GetMapping(value="/devices")
    public List<String> getDevices(){
        return deviceManager.getList().stream()
            .map(DeviceEntry::getId)
            .collect(Collectors.toList());
    }


    
}