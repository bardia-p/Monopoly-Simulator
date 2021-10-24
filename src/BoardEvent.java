import java.util.ArrayList;
import java.util.EventObject;

/**
 * SYSC 3110 - Milestone 1 BoardEvent Class
 *
 * This document is the BoardEvent. This class has a BoardModel.Status value of GET_NUM_PLAYERS, INITIALIZE_PLAYERS, GET_COMMAND,
 * a player, a value, and a list of commands. The class is primarily used to create board events containing pertinent information
 * that can be passed to other classes.
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
    private BoardModel.Status type;
    /**
     * Keeps track of the player the event refers to.
     */
    private Player player;
    /**
     * Keeps track of an integer value.
     */
    private int value;
    /**
     * Keeps track of a list of commands.
     */
    private ArrayList<BoardModel.Command> commands;

    /**
     * Constructor for the Board event, instantiates the board model, the type of event, the relevant player, and the
     * command chosen by the player.
     * @author Kyra Lothrop 101145872
     * @param model the board model, BoardModel
     * @param type the status enum, BoardModel.Status
     * @param player the relevant player, Player
     * @param commands the possible commands, ArrayList<BoardModel.Command>
     */
    public BoardEvent(BoardModel model, BoardModel.Status type, Player player, ArrayList<BoardModel.Command> commands){
        this(model, type);
        this.player = player;
        this.commands = commands;
    }

    /**
     * Constructor for the Board event, instantiates the board model, the type of event and a value.
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
     * Constructor for the Board event, instantiates the board model and the type of event.
     * @author Bardia Parmoun 101143006
     * @param model the board model, BoardModel
     * @param type the status enum, BoardModel.Status
     */
    public BoardEvent(BoardModel model, BoardModel.Status type){
        super(model);
        this.type = type;
        this.player = null;
        this.value = 0;
        this.commands = new ArrayList<>();
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
    public ArrayList<BoardModel.Command> getCommands(){
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
}
