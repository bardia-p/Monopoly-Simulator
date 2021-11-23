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
     * The limit of the number of houses the AI can build.
     */
    private static final int BUILD_LIMIT = 2;
    /**
     * Keeps track of the number of houses built.
     */
    private int numHousesBuilt;

    /**
     * The constructor for the AI player.
     * @param model the board model, BoardModel
     * @param name the name of the player, String
     * @param icon the icon of the player, BoardModel.Icon
     * @author Bardia Parmoun, 101143006
     */
    public AIPlayer(BoardModel model, String name, BoardModel.Icon icon) {
        super(name, icon, true);
        this.model = model;
        this.numHousesBuilt = 0;
    }

    /**
     * Figures out the next move of the AI based on its current location.
     * @author Bardia Parmoun, 101143006.
     */
    public void nextMove(){
        List<BoardModel.Command> availableCommands = model.getCommand(this);

        // Checks to see if it can buy a property.
        if (availableCommands.contains(BoardModel.Command.BUY)) {
            handleBuy();
            // Checks to see if it has any fees to pay
        } else if (availableCommands.contains(BoardModel.Command.PAY_FEES)) {
            handlePayFees(availableCommands);
            // Checks to see if it can roll.
        } else if (availableCommands.contains(BoardModel.Command.ROLL_AGAIN)) {
            handleRoll();
            // Handles building houses
        } else if (numHousesBuilt < BUILD_LIMIT && availableCommands.contains(BoardModel.Command.BUILD)){
            handleBuild();
        } else { // Finally, if it cannot do anything it will pass turn.
            handlePass();
        }
    }

    /**
     * Handles the time when the player has to pay fees.
     * @param availableCommands the list of the command, BoardModel.Command.
     * @author Bardia Parmoun 101143006
     */
    private void handlePayFees(List<BoardModel.Command> availableCommands) {
        BoardCell currentCell = getCurrentCell();
        // If it cannot afford the fees.
        if (!model.payFees(currentCell, this)) {
            // Tries to sell properties.
            if (availableCommands.contains(BoardModel.Command.SELL)) {
                handleSell();
            } else { // If cannot sell it must forfeit.
                handleForfeit();
            }
        }
    }

    /**
     * Handles the event when the player has to sell.
     * @author Bardia Parmoun 101143006
     */
    private void handleSell() {
        BoardCell sellableCell = this.getOwnedLocations(true).get(0);
        model.sellLocation(this, sellableCell);
    }

    /**
     * Handles the AI player forfeit event.
     * @author Bardia Parmoun 101143006
     */
    private void handleForfeit(){
        model.forfeit(this);
    }

    /**
     * Handles the building event.
     * @author Bardia Parmoun 101143006
     */
    private void handleBuild(){
        List<Property.NeighborhoodEnum> buildableProperites = this.getBuildableProperties();

        for (BoardCell bc: getOwnedLocations(false)){
            if (bc.getType() == BoardCell.CellType.PROPERTY){
                Property p = (Property) bc;
                // We are limiting the number of houses built to only 2.
                if (numHousesBuilt < BUILD_LIMIT && buildableProperites.contains(p.getNeighborhood())){
                    model.buildHouse(p, this);
                    numHousesBuilt += 1;
                }
            }
        }
    }

    /**
     * Handles the event when the player has to buy.
     * @author Bardia Parmoun 101143006
     */
    private void handleBuy(){
        model.buyLocation(getCurrentCell(), this);
    }

    /**
     * Handles the event when the player passes.
     * @author Bardia Parmoun 101143006
     */
    private void handlePass(){
        model.passTurn(this);
        numHousesBuilt = 0;
    }

    /**
     * Handles the event when the AI player has to roll.
     * @author Bardia Parmoun 10114300
     */
    private void handleRoll(){
        model.roll(this);
    }
}
