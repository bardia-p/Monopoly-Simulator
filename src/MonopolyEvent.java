import java.util.EventObject;

/**
 * Group 3
 * SYSC 3110 - Milestone 3 MonopolyEvent Class
 *
 * This document is the MonopolyEvent. This class has a BoardModel.Status status and an EventType eventType.
 *
 * The class is the main point of contact between the BoardModel and the BoardListener. It is an abstract representation
 * of all the different event types that can be passed to the listener from the model.
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 1.0
 */
public abstract class MonopolyEvent extends EventObject {
    /**
     * Keeps track of the status in the event.
     */
    private final BoardModel.Status status;

    /**
     * The main constructor for MonopolyEvent
     * @param model the source of the event, BoardModel
     * @author Bardia Parmoun, 101143006
     */
    public MonopolyEvent(BoardModel model, BoardModel.Status status) {
        super(model);
        this.status = status;
    }

    /**
     * Returns the type of the event.
     * @return type, BoardModel.Status
     * @author Bardia Parmoun, 101143006
     */
    public BoardModel.Status getStatus(){
        return status;
    }
}
