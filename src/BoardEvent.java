import java.util.ArrayList;
import java.util.List;

/**
 * Group 3
 * SYSC 3110 - Milestone 3 BoardEvent Class
 *
 * This document is the BoardEvent. This class has a player and a list of commands.
 *
 * The class is the main point of contact between the BoardModel and the BoardListener. The current status of the board
 * is packaged inside of an event and is passed to the listener which is then handled internally. This class is in
 * charge of handling the events that relate to the board.
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 3.0
 */
public class BoardEvent extends MonopolyEvent {
    /**
     * Keeps track of the list of the possible commands.
     */
    private List<BoardModel.Command> commands;
    /**
     * Keeps track of the player in the event.
     */
    private Player player;

    /**
     * The main constructor for the BoardEvent class.
     * @author Bardia Parmoun, 101143006
     * @param model the main boardmodel, BoardModel
     * @param status the status of the board, BoardModel.Status
     */
    public BoardEvent(BoardModel model, BoardModel.Status status){
        super(model, status, MonopolyEvent.EventType.BOARD_EVENT);
        this.commands = new ArrayList<>();
        this.player = model.getCurrentTurn();
    }

    /**
     * The constructor for BoardEvent with commands
     * @author Bardia Parmoun, 101143006
     * @param model the main boardmodel, BoardModel
     * @param status the status of the board, BoardModel.Status
     * @param commands keeps track of the list of the commands.
     */
    public BoardEvent(BoardModel model, BoardModel.Status status, Player player, List<BoardModel.Command> commands){
        this(model, status);
        this.commands = commands;
        this.player = player;
    }

    /**
     * Gets the list of the board commands.
     * @author Bardia Parmoun, 101143006
     * @return the current board commands, List<Command> Commands.
     */
    public List<BoardModel.Command> getCommands(){
        return commands;
    }

    /**
     * Gets the player in the event.
     * @author Bardia Parmoun, 101143006
     * @return the player, Player.
     */
    public Player getPlayer(){
        return player;
    }
}