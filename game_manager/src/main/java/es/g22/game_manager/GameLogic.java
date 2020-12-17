package es.g22.game_manager;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * GameLogic
 */
@Service
@Transactional
public class GameLogic {

    private static final Logger logger = LoggerFactory.getLogger(GameLogic.class);

    @Autowired
    private Listeners listeners;

    @Autowired
    private GamesRepository repository;

    public GameAction executeMovement(Long gameid, DecodedMovement mov){
        Game game = repository.findById(gameid).get();


        if (!mov.getDevice_id().equals(game.getDevice1()) && !mov.getDevice_id().equals(game.getDevice2())){
            logger.error("Movement from device " + mov.getDevice_id() + " wrongfully sent to game " + game.getId());
            return null;
        }
        if (game.isFinished()){
            listeners.removeDevice(mov.getDevice_id());
            logger.error("Movement send to ended game.");
            return null;
        }
        if (game.isPlayer1Turn() && !mov.getDevice_id().equals(game.getDevice1())){
            logger.error("Not player 2 turn");
            return null;
        }
        if (game.isPlayer2Turn() && !mov.getDevice_id().equals(game.getDevice2())){
            logger.error("Not player 1 turn");
            return null;
        }
        if(mov.getData().equals("LEFT")){
            if(game.getCurrentPos()[0] == 0)
                return null;
            int[] temp = game.getCurrentPos();
            temp[0] = temp[0]-1;
            //logger.info(Arrays.toString(temp));
            game.setCurrentPos(temp);
            GameAction action = new GameAction(game.getId(), mov.getDevice_id(), "LEFT");
            List<GameAction> array = game.getActions();
            array.add(action);
            game.setActions(array);
            repository.save(game);
            return action;
        }
        else if(mov.getData().equals("RIGHT")){
            if(game.getCurrentPos()[0] == 2)
                return null;
            int[] temp = game.getCurrentPos();
            temp[0] = temp[0]+1;
            game.setCurrentPos(temp);
            GameAction action = new GameAction(game.getId(), mov.getDevice_id(), "RIGHT");
            List<GameAction> array = game.getActions();
            array.add(action);
            game.setActions(array);
            repository.save(game);
            return action;
        }
        else if(mov.getData().equals("DOWN")){
            if(game.getCurrentPos()[1] == 0)
                return null;
            int[] temp = game.getCurrentPos();
            temp[1] = temp[1]-1;
            game.setCurrentPos(temp);
            GameAction action = new GameAction(game.getId(), mov.getDevice_id(), "DOWN");
            List<GameAction> array = game.getActions();
            array.add(action);
            game.setActions(array);
            repository.save(game);
            return action;            
        }
        else if(mov.getData().equals("UP")){
            if(game.getCurrentPos()[1] == 2)
                return null;
            int[] temp = game.getCurrentPos();
            temp[1] = temp[1]+1;
            game.setCurrentPos(temp);
            GameAction action = new GameAction(game.getId(), mov.getDevice_id(), "UP");
            List<GameAction> array = game.getActions();
            array.add(action);
            game.setActions(array);
            repository.save(game);
            return action;
            
        }
        else if(mov.getData().equals("SELECT")){
            String[][] board;
            board = game.getBoard();
            if (board[game.getCurrentPos()[0]][game.getCurrentPos()[1]].equals("")){
                if(game.isPlayer1Turn()){
                    board[game.getCurrentPos()[0]][game.getCurrentPos()[1]] = "1";
                }
                else{
                    board[game.getCurrentPos()[0]][game.getCurrentPos()[1]] = "2";
                }
            }
            else{
                logger.error("Can't play in that position. Occupied");
                return null;
            }
            game.setBoard(board);
            if (game.checkWinner()){
                GameAction action = new GameAction(game.getId(), mov.getDevice_id(), "WINNER");
                List<GameAction> array = game.getActions();
                array.add(action);
                game.setActions(array);
                listeners.removeDevice(game.getDevice1());
                listeners.removeDevice(game.getDevice2());
                return action;
            }
            game.setPlayer1Turn(!game.getPlayer1Turn());
            game.setPlayer2Turn(!game.getPlayer2Turn());

            GameAction action = new GameAction(game.getId(), mov.getDevice_id(), "SELECT");
            List<GameAction> array = game.getActions();
            array.add(action);
            game.setActions(array);
            repository.save(game);
            return action;
        }
        else{
            logger.error("Invalid movement direction");
            return null;
        }
    } 
}