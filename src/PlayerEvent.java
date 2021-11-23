/**
 * Group 3
 * SYSC 3110 - Milestone 3 PlayerEvent Class
 *
 * This document is the PlayerEvent. This class has a BoardModel.Status status and an EventType eventType.
 *
 * The class is the main point of contact between the BoardModel and the BoardListener. It handles all the events that
 * are related to the player.
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 1.0
 */
public class PlayerEvent extends MonopolyEvent {
    /**
     * Keeps track of the value passed through the event.
     */
    private int value;
    /**
     * Keeps track of the player in the event.
     */
    private final Player player;
    /**
     * Keeps track of the result of the event.
     */
    private boolean result;
    /**
     * The boardcell passed in the event.
     */
    private BoardCell cell;
    /**
     * The command that is requested by the player.
     */
    private BoardModel.Command command;

    /**
     * The constructor for the playerevent with a cell.
     * @param model the model in the event.
     * @param player the player in event.
     * @param cell the cell that is passed in the event.
     * @author Bardia Parmoun, 101143006
     */
    public PlayerEvent(BoardModel model, BoardModel.Command command,  Player player, BoardCell cell){
        this(model, command, player);
        this.cell = cell;
    }

    /**
     * Constructor for PlayerEvent with result and value.
     * @param model the model in the event.
     * @param player the player in event.
     * @param value the value of the event.
     * @param result the result of event.
     * @author Bardia Parmoun, 101143006
     */
    public PlayerEvent(BoardModel model, BoardModel.Command command, Player player, int value, boolean result) {
        this(model, command, player, result);
        this.value = value;
    }

    /**
     * Constructor for playerevent with results.
     * @param model the model in the event.
     * @param player the player in event.
     * @param result the result of event.
     * @author Bardia Parmoun, 101143006
     */
    public PlayerEvent(BoardModel model, BoardModel.Command command, Player player,
                       boolean result) {
        this(model, command, player);
        this.result = result;
    }

    /**
     * Constructor for PlayerEvent with no false results.
     * @param model the model in the event.
     * @param player the player in event.
     * @author Bardia Parmoun, 101143006
     */
    public PlayerEvent(BoardModel model, BoardModel.Command command, Player player) {
        super(model, BoardModel.Status.PLAYER_INPUT);
        this.result = true;
        this.player = player;
        this.value = 0;
        this.command = command;
        this.cell = player.getCurrentCell();
    }

    /**
     * Gets the result of the event.
     * @return result, boolean
     * @author Bardia Parmoun, 101143006
     */
    public boolean getResult(){
        return result;
    }

    /**
     * Gets the value of the event.
     * @return value, int
     * @author Bardia Parmoun, 101143006
     */
    public int getValue(){
        return value;
    }

    /**
     * Gets the player that is carried in the event.
     * @return player, Player
     * @author Bardia Parmoun, 101143006
     */
    public Player getPlayer(){
        return player;
    }

    /**
     * Gets the cell that is carried in the event.
     * @return cell, BoardCell
     * @author Bardia Parmoun, 101143006
     */
    public BoardCell getCell(){
        return cell;
    }

    /**
     * Return the player command.
     * @return BoardModel.Command.
     */
    public BoardModel.Command getCommand(){
        return command;
    }
}
