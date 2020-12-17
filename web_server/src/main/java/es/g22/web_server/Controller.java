package es.g22.web_server;

import java.util.HashMap;
import java.util.List;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Controller
 */

@RestController
public class Controller {
    
    @Autowired
    @Qualifier("eurekaTemplate")
    RestTemplate restTemplate;

    @Autowired
    private EurekaClient discoveryClient;

        //InstanceInfo instance = discoveryClient.getNextServerFromEureka("game-manager", false);
        //System.out.println(instance.getHomePageUrl());

    @CrossOrigin
    @GetMapping("/api/game/{id}")
    public Game getGame(@PathVariable("id") long id){
        return restTemplate.getForObject("http://game-manager" + "/api/game/" + Long.toString(id), Game.class);
    }

    @CrossOrigin
    @GetMapping("/api/game")
    public List<Game> getGames(){
        return restTemplate.getForObject("http://game-manager" + "/api/game", List.class);
    }

    @CrossOrigin
    @GetMapping("/api/game/finished")
    public List<Game> finishedGames(){
        return restTemplate.getForObject("http://game-manager" + "/api/game/finished", List.class);
    }

    @CrossOrigin
    @GetMapping("/api/game/notfinished")
    public List<Game> notfinishedGames(){
        return restTemplate.getForObject("http://game-manager" + "/api/game/notfinished", List.class);
    }

    @CrossOrigin
    @PostMapping("api/newgame")
    public String newGame(@RequestParam("device1") String device1, @RequestParam("device2") String device2){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("device1", device1);
        map.add("device2", device2);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        
        return restTemplate.postForEntity("http://game-manager" + "/api/newgame", request, String.class).getBody();
    }

    @CrossOrigin
    @GetMapping("/api/devices")
    public List<String> getDevices(){
        List<String> data;
        data = restTemplate.getForObject("http://data-proxy" + "/devices", List.class);
        return data;
    }
    
    
}