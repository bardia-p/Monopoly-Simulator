import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

/**
 * Group 3
 * SYSC 3110 - Milestone 2 BoardEvent Class
 *
 * This document is the BoardEvent. This class has a BoardModel.Status type, a player, a value, a list of commands, a
 * result, a board cell, the recent roll, and a list of players.
 *
 * The class is the main point of contact between the BoardModel and the BoardListener. The current status of the board
 * is packaged inside of an event and is passed to the listener which is then either handled internally by the listener
 * or passed on to the controller.
 *
 * Events can come in different forms containing different kinds of information which is the reason why this class has a
 * lot of constructors.
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 1.0
 */
public class BoardEvent extends EventObject {
    /**
     * Keeps track of the event type that shall occur.
     */
    private final BoardModel.Status type;
    /**
     * Keeps track of the player the event refers to.
     */
    private Player player;
    /**
     * Keeps track of an integer value.
     */
    private int value;

    /**
     * Keeps track of an integer value.
     */
    private int value2;
    /**
     * Keeps track of the result of the given event.
     */
    private boolean result;
    /**
     * Keeps track of the relevant board cell in the event.
     */
    private BoardCell boardCell;
    /**
     * Keeps track of a list of commands.
     */
    private List<BoardModel.Command> commands;
    /**
     * Keeps track of the recent dice roll in the event.
     */
    private int[] dice;

    private List<Property.NeighborhoodEnum> neighborhoods;

    /**
     * Keeps track of the relevant players to the event.
     */
    private List<Player> players;
    /**
     * Keeps track of the relevant cells to the event.
     */
    private List<BoardCell> cells;

    private Property property;

    /**
     * Constructor for the Board event, instantiates the board model, the type of event, the relevant player, and the
     * command chosen by the player. Mainly used for getting the player's command
     * @author Kyra Lothrop 101145872
     * @param model the board model, BoardModel
     * @param type the status enum, BoardModel.Status
     * @param commands the possible commands, ArrayList<BoardModel.Command>
     */
    public BoardEvent(BoardModel model, BoardModel.Status type, ArrayList<BoardModel.Command> commands, Player player){
        this(model, type, player);
        this.commands = commands;
    }

    /**
     * Constructor for the Board event, instantiates the board model, the type of event, the relevant player, and the
     * command chosen by the player. Mainly used for buying/selling properties.
     * @author Kyra Lothrop 101145872
     * @param model the board model, BoardModel
     * @param type the status enum, BoardModel.Status
     * @param player the relevant player, Player
     * @param boardCell the relevant board cell, BoardCell
     * @param result the result of the operation, boolean
     */
    public BoardEvent(BoardModel model, BoardModel.Status type, Player player, BoardCell boardCell, boolean result){
        this(model, type, player);
        this.result = result;
        this.boardCell = boardCell;
    }

    /**
     * Constructor for the Board event, instantiates the board model, the type of event, the relevant player, and the
     * command chosen by the player. Mainly used for financial transactions such as paying fees.
     * @author Kyra Lothrop 101145872
     * @param model the board model, BoardModel
     * @param type the status enum, BoardModel.Status
     * @param player the relevant player, Player
     * @param value the value of the event, int
     * @param result the result of the operation, boolean
     */
    public BoardEvent(BoardModel model, BoardModel.Status type, Player player, int value , boolean result){
        this(model, type, player);
        this.result = result;
        this.value = value;
    }

    /**
     * Constructor for the Board event, instantiates the board model, the type of event, the relevant player, and the
     * command chosen by the player. Mainly used for getting the current roll.
     * @author Kyra Lothrop 101145872
     * @param model the board model, BoardModel
     * @param type the status enum, BoardModel.Status
     * @param player the relevant player, Player
     * @param dice the recent dice roll, int[]
     */
    public BoardEvent(BoardModel model, BoardModel.Status type, Player player, int[] dice){
        this(model, type, player);
        this.dice = dice;
    }

    public BoardEvent(BoardModel model, BoardModel.Status type, Player player, List<Property.NeighborhoodEnum> neighborhoods){
        this(model,type, player);
        this.neighborhoods = neighborhoods;
    }

    /**
     * Constructor for the Board event, instantiates the board model, the type of event, the relevant player, and the
     * command chosen by the player. Mainly used for handling events involving more than multiple players such as the
     * list of the winners.
     * @author Kyra Lothrop 101145872
     * @param model the board model, BoardModel
     * @param type the status enum, BoardModel.Status
     * @param players the list of the players, List<Player>
     */
    public BoardEvent(BoardModel model, List<Player> players, BoardModel.Status type){
        this(model, type);
        this.players = players;
    }

    /**
     * Constructor for the Board event, instantiates the board model, the type of event, the relevant player, and the
     * command chosen by the player. Mainly used for getting the status of the player.
     * @author Kyra Lothrop 101145872
     * @param model the board model, BoardModel
     * @param type the status enum, BoardModel.Status
     * @param player the relevant player, Player
     */
    public BoardEvent(BoardModel model, BoardModel.Status type, Player player){
        this(model, type);
        this.player = player;
    }

    /**
     * Constructor for the BoardEvent,
     * @author Kyra Lothrop 101145872
     * @param model the board model, BoardModel
     * @param type the status enum, BoardModel.Status
     * @param player the relevant player, Player
     * @param value the value1 to be used, int
     * @param value2 the value2 to be used, int
     */
    public BoardEvent(BoardModel model, BoardModel.Status type, Player player, int value, int value2){
        this(model, type);
        this.player = player;
        this.value = value;
        this.value2 = value2;
    }


    public BoardEvent(BoardModel model, BoardModel.Status type, Property property){
        this(model, type);
        this.property = property;
    }

    /**
     * Constructor for the Board event, instantiates the board model, the type of event and a value. Mainly used for
     * events such as getting the number of players.
     * @author Kyra Lothrop 101145872
     * @param model the board model, BoardModel
     * @param type the status enum, BoardModel.Status
     * @param value the value to be used, int
     */
    public BoardEvent(BoardModel model, BoardModel.Status type, int value){
        this(model, type);
        this.value = value;
    }

    /**
     * Constructor for the Board event, instantiates the board model, the type of event and a value. Mainly used for
     * events such as construcing the board
     * @author Bardia Parmoun 101143006
     * @param model the board model, BoardModel
     * @param type the status enum, BoardModel.Status
     * @param cells keep track of the cells, List<BoardCell>
     */
    public BoardEvent(BoardModel model, BoardModel.Status type, List<BoardCell> cells){
        this(model, type);
        this.cells = cells;
    }

    /**
     * Constructor for the Board event, instantiates the board model and the type of event. Mainly used for events
     * that require no inputs such as a board status
     * @author Bardia Parmoun 101143006
     * @param model the board model, BoardModel
     * @param type the status enum, BoardModel.Status
     */
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
        this.cells = new ArrayList<>();
        this.neighborhoods = new ArrayList<>();
    }

    /**
     * Accessor to get the enum status.
     * @author Sarah Chow 10113033
     * @return the status of the board, BoardModel.Status
     */
    public BoardModel.Status getType(){
        return type;
    }

    /**
     * Accessor to get the player.
     * @author Sarah Chow 10113033
     * @return the active player, Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Accessor to get the commands associated with the event.
     * @author Owen VanDusen 101152022
     * @return a list of the commands available, ArrayList<BoardModel.Command>
     */
    public List<BoardModel.Command> getCommands() {
        return commands;
    }

    /**
     * Accessor to get the value associated with the event.
     * @author Owen VanDusen 101152022
     * @return the value, int
     */
    public int getValue(){
        return value;
    }

    /**
     * Accessor to get the value associated with the event.
     * @author Owen VanDusen 101152022
     * @return the value, int
     */
    public int getValue2() {
        return value2;
    }

    /**
     * Returns the result of the event.
     * @author Bardia Parmoun 101143006
     * @return the result, boolean
     */
    public boolean getResult() {
        return result;
    }

    /**
     * Returns the relevant board cell.
     * @author Owen VanDusen 101152022
     * @return the board cell, BoardCell
     */
    public BoardCell getBoardCell() {
        return boardCell;
    }

    /**
     * Returns the relevant players.
     * @author Bardia Parmoun 101143006
     * @return players, List<Player>
     */
    public List<Player> getPlayers(){
        return players;
    }

    /**
     * Returns the recent dice roll.
     * @author Owen VanDusen 101152022
     * @return the dice, int[]
     */
    public int[] getDice(){
        return dice;
    }

    /**
     * Returns the list of the cells in the event.
     * @return the list of the cells passed in the event, List<BoardCell>
     */
    public List<BoardCell> getCells(){
        return cells;
    }

    public List<Property.NeighborhoodEnum> getNeighborhoods(){
        return neighborhoods;
    }

    public Property getProperty() {
        return property;
    }
}