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
    private int value;
    private Player player;
    private boolean result;

    /**
     * Constructor for PlayerEvent with value.
     * @param model the model in the event.
     * @param status the status of event.
     * @param player the player in event.
     * @param value the value of the event.
     * @param result the result of event.
     * @author Bardia Parmoun, 101143006
     */
    public PlayerEvent(BoardModel model, BoardModel.Status status, Player player,  int value, boolean result) {
        this(model, status, player, result);
        this.value = value;
    }

    /**
     * Constructor for playerevent with result
     * @param model the model in the event.
     * @param status the status of event.
     * @param player the player in event.
     * @param result the result of event.
     * @author Bardia Parmoun, 101143006
     */
    public PlayerEvent(BoardModel model, BoardModel.Status status, Player player,  boolean result) {
        this(model, status, player);
        this.result = result;
    }

    /**
     * Constructor for PlayerEvent.
     * @param model the model in the event.
     * @param status the status of event.
     * @param player the player in event.
     * @author Bardia Parmoun, 101143006
     */
    public PlayerEvent(BoardModel model, BoardModel.Status status, Player player) {
        super(model, status, EventType.PLAYER_EVENT);
        this.result = true;
        this.player = player;
        this.value = 0;
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
}
