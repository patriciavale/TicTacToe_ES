package es.g22.web_server;

import java.util.ArrayList;
import java.util.List;
;


/**
 * Game
 */

public class Game {

 
    private long id;

    private String device1;
    private String device2;

    private boolean finished;

    private String[][] board;
    private int[] currentPos;

    private List<GameAction> actions;

    private boolean player1Turn;
    private boolean player2Turn;

    private boolean player1Winner;
    private boolean player2Winner;



    public Game(String device1, String device2) {
        this.device1 = device1;
        this.device2 = device2;
        this.finished = false;
        this.board = new String[3][3];
        for(int i = 0; i<3; i++){
            for (int j = 0; j<3; j++){
                this.board[i][j] = "";
            }
        }
        this.currentPos = new int[]{1,1};
        this.actions = new ArrayList<GameAction>();
        this.player1Turn = true;
        this.player2Turn = false;
        this.player1Winner = false;
        this.player2Winner = false;
    }

    public String getDevice1() {
        return this.device1;
    }

    public void setDevice1(String device1) {
        this.device1 = device1;
    }

    public String getDevice2() {
        return this.device2;
    }

    public void setDevice2(String device2) {
        this.device2 = device2;
    }

    public boolean isFinished() {
        return this.finished;
    }

    public boolean getFinished() {
        return this.finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String[][] getBoard() {
        return this.board;
    }

    public void setBoard(String[][] board) {
        this.board = board;
    }

    public List<GameAction> getActions() {
        return this.actions;
    }

    public void setActions(List<GameAction> actions) {
        this.actions = actions;
    }

    public boolean isPlayer1Turn() {
        return this.player1Turn;
    }

    public boolean getPlayer1Turn() {
        return this.player1Turn;
    }

    public void setPlayer1Turn(boolean player1Turn) {
        this.player1Turn = player1Turn;
    }

    public boolean isPlayer2Turn() {
        return this.player2Turn;
    }

    public boolean getPlayer2Turn() {
        return this.player2Turn;
    }

    public void setPlayer2Turn(boolean player2Turn) {
        this.player2Turn = player2Turn;
    }

    public boolean isPlayer1Winner() {
        return this.player1Winner;
    }

    public boolean getPlayer1Winner() {
        return this.player1Winner;
    }

    public void setPlayer1Winner(boolean player1Winner) {
        this.player1Winner = player1Winner;
    }

    public boolean isPlayer2Winner() {
        return this.player2Winner;
    }

    public boolean getPlayer2Winner() {
        return this.player2Winner;
    }

    public void setPlayer2Winner(boolean player2Winner) {
        this.player2Winner = player2Winner;
    }

    public Game device1(String device1) {
        this.device1 = device1;
        return this;
    }

    public Game device2(String device2) {
        this.device2 = device2;
        return this;
    }

    public Game finished(boolean finished) {
        this.finished = finished;
        return this;
    }

    public Game board(String[][] board) {
        this.board = board;
        return this;
    }

    public Game actions(List<GameAction> actions) {
        this.actions = actions;
        return this;
    }

    public Game player1Turn(boolean player1Turn) {
        this.player1Turn = player1Turn;
        return this;
    }

    public Game player2Turn(boolean player2Turn) {
        this.player2Turn = player2Turn;
        return this;
    }

    public Game player1Winner(boolean player1Winner) {
        this.player1Winner = player1Winner;
        return this;
    }

    public Game player2Winner(boolean player2Winner) {
        this.player2Winner = player2Winner;
        return this;
    }

    @Override
    public String toString() {
        return "{" +
            " device1='" + getDevice1() + "'" +
            ", device2='" + getDevice2() + "'" +
            ", finished='" + isFinished() + "'" +
            ", board='" + getBoard() + "'" +
            ", actions='" + getActions() + "'" +
            ", player1Turn='" + isPlayer1Turn() + "'" +
            ", player2Turn='" + isPlayer2Turn() + "'" +
            ", player1Winner='" + isPlayer1Winner() + "'" +
            ", player2Winner='" + isPlayer2Winner() + "'" +
            "}";
    }


    public Game() {
    }

    public Game(long id, String device1, String device2, boolean finished, String[][] board, int[] currentPos, List<GameAction> actions, boolean player1Turn, boolean player2Turn, boolean player1Winner, boolean player2Winner) {
        this.id = id;
        this.device1 = device1;
        this.device2 = device2;
        this.finished = finished;
        this.board = board;
        this.currentPos = currentPos;
        this.actions = actions;
        this.player1Turn = player1Turn;
        this.player2Turn = player2Turn;
        this.player1Winner = player1Winner;
        this.player2Winner = player2Winner;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int[] getCurrentPos() {
        return this.currentPos;
    }

    public void setCurrentPos(int[] currentPos) {
        this.currentPos = currentPos;
    }

    public Game id(long id) {
        this.id = id;
        return this;
    }

    public Game currentPos(int[] currentPos) {
        this.currentPos = currentPos;
        return this;
    }

    public void addGameAction(GameAction action){
        this.actions.add(action);
    }

    public boolean checkWinner(){
        for (int r = 0; r < 3; r++) {
            if (this.board[r][0] == this.board[r][1] && this.board[r][1] == this.board[r][2]){
                this.finished = true;
                if(this.board[r][0] == "1"){
                    this.player1Winner = true;
                }
                else{
                    this.player2Winner = true;
                }
                return true;
            }
        }
        //loops through columns checking if win-condition exists
        for (int c = 0; c < 3; c++) {
            if (this.board[0][c].equals(this.board[1][c]) && this.board[1][c].equals(this.board[2][c])) {
                this.finished = true;
                if(this.board[0][c].equals("1")){
                    this.player1Winner = true;
                }
                else{
                    this.player2Winner = true;
                }
                return true;
            }
        }
        //checks diagonals for win-condition
        if (this.board[0][0].equals(this.board[1][1]) && this.board[1][1].equals(this.board[2][2])){
            this.finished = true;
            if(this.board[1][1].equals("1")){
                this.player1Winner = true;
            }
            else{
                this.player2Winner = true;
            }
            return true;
        }
    
        if (this.board[0][2].equals(this.board[1][1]) && this.board[1][1].equals(this.board[2][0])){
            this.finished = true;
            if(this.board[1][1].equals("1")){
                this.player1Winner = true;
            }
            else{
                this.player2Winner = true;
            }
            return true;
        }
    
        return false;
    }
    
}