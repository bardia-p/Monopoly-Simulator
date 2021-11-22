import java.util.List;

/**
 * Group 3
 * SYSC 3110 - Milestone 3 AIPlayer Class
 *
 * This document is the AIPlayer. This class is an instance of the Player class.
 * The class replicates the behaviour of a simple AI.
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 3.0
 */
public class AIPlayer extends Player {
    /**
     * Keeps track of the model.
     */
    private final BoardModel model;

    /**
     * The constructor for the AI player.
     * @param model the board model, BoardModel
     * @param name the name of the player, String
     * @param icon the icon of the player, BoardModel.Icon
     * @author Bardia Parmoun, 101143006
     */
    AIPlayer(BoardModel model, String name, BoardModel.Icon icon) {
        super(name, icon, true);
        this.model = model;
    }

    /**
     * Figures out the next move of the AI based on its current location.
     * @author Bardia Parmoun, 101143006.
     */
    public void nextMove(){
        BoardCell currentCell = getCurrentCell();

        List<BoardModel.Command> availableCommands = model.getCommand(this);

        // Checks to see if it can buy a property.
        if (availableCommands.contains(BoardModel.Command.BUY)) {
            model.buyLocation(currentCell, this);

            // Checks to see if it has any fees to pay
        } else if (availableCommands.contains(BoardModel.Command.PAY_FEES)) {
            // If it cannot afford the fees.
            if (!model.payFees(currentCell, this)) {
                // Tries to sell properties.
                if (availableCommands.contains(BoardModel.Command.SELL)) {
                    BoardCell sellableCell = this.getOwnedLocations(true).get(0);
                    model.sellLocation(this, sellableCell);
                } else { // If cannot sell it must forfeit.
                    model.forfeit(this);
                }
            }
            // Checks to see if it can roll.
        } else if (availableCommands.contains(BoardModel.Command.ROLL_AGAIN)) {
            model.roll(this);
        } else { // Finally if it cannot do anything it will pass turn.
            model.passTurn(this);
        }
    }
}
