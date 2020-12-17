package es.g22.game_manager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller
 */
@RestController
public class Controller {

    @Autowired
    private GamesRepository repository;

    @Autowired
    Listeners listeners;

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @GetMapping("/api/game/{id}")
    public Game getGame(@PathVariable("id") long id){
        return repository.findById(id).get();
    }

    @GetMapping("/api/game")
    public List<Game> getGames(){
        return repository.findAll();
    }

    @GetMapping("/api/game/finished")
    public List<Game> finishedGames(){
        return repository.getFinished();
    }

    @GetMapping("/api/game/notfinished")
    public List<Game> notfinishedGames(){
        return repository.getNotFinished();
    }


    @PostMapping("api/newgame")
    public String newGame(@RequestParam("device1") String device1, @RequestParam("device2") String device2){
        Game game = new Game(device1, device2);
        Game newgame = repository.save(game);
        listeners.addDevice(device1, newgame.getId());
        listeners.addDevice(device2, newgame.getId());
        return Long.toString(newgame.getId());
    }


    
}