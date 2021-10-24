//student #s here

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class BoardEvent extends EventObject {
    private BoardModel.Status type;
    private Player player;
    private int value;
    private boolean result;
    private BoardCell boardCell;
    private List<BoardModel.Command> commands;
    private int[] dice;
    private List<Player> players;


    public BoardEvent(BoardModel model, BoardModel.Status type, Player player, ArrayList<BoardModel.Command> commands){
        this(model, type);
        this.player = player;
        this.commands = commands;
    }

    public BoardEvent(BoardModel model, BoardModel.Status type, Player player, BoardCell boardCell, boolean result){
        this(model, type, player, result);
        this.boardCell = boardCell;
    }

    public BoardEvent(BoardModel model, BoardModel.Status type, Player player, int value , boolean result){
        this(model, type, player, result);
        this.value = value;
    }

    public BoardEvent(BoardModel model, BoardModel.Status type, Player player, boolean result){
        this(model, type, player);
        this.result = result;
    }
    public BoardEvent(BoardModel model, BoardModel.Status type, Player player, int[] dice){
        this(model, type, player);
        this.dice = dice;
    }

    public BoardEvent(BoardModel model, BoardModel.Status type, List<Player> players){
        this(model, type);
        this.players = players;
    }

    public BoardEvent(BoardModel model, BoardModel.Status type, Player player){
        this(model, type);
        this.player = player;
    }

    public BoardEvent(BoardModel model, BoardModel.Status type, BoardCell boardCell){
        this(model, type);
        this.boardCell = boardCell;
    }

    public BoardEvent(BoardModel model, BoardModel.Status type, int value){
        this(model, type);
        this.value = value;
    }

    public BoardEvent(BoardModel model, BoardModel.Status type){
        super(model);
        this.type = type;
        this.player = null;
        this.value = 0;
        this.result = true;
        this.boardCell = null;
        this.commands = new ArrayList<>();
        this.players = new ArrayList<>();
        this.dice = new int[2];
    }

    public BoardModel.Status getType(){
        return type;
    }

    public Player getPlayer() {
        return player;
    }

    public List<BoardModel.Command> getCommands(){
        return (ArrayList<BoardModel.Command>) commands;
    }

    public int getValue(){
        return value;
    }

    public boolean getResult() {
        return result;
    }

    public BoardCell getBoardCell() {
        return boardCell;
    }

    public List<Player> getPlayers(){
        return players;
    }

    public int[] getDice(){
        return dice;
    }
}