import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Group 3
 * SYSC 3110 - Milestone 3 BoardModel Class
 *
 * This document is the BoardModel. This class has a list of properties, a list of players, an array of dice,
 * the number of players, the size of the board, the board views, if the game has finished, the current turn,
 * the bank, the board statuses and the player commands. This is the primary brain class that handles
 * nearly all logic needed to make monopoly run.
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 3.0
 */
public class BoardModel {
    /**
     * Keeps track of the cells.
     */
    private final List<BoardCell> cells;
    /**
     * Keeps track of the players.
     */
    private final List<Player> players;
    /**
     * Keeps track of the dice rolls.
     */
    private final int[] dice;
    /**
     * Keeps track of the number of players.
     */
    private int numPlayers;
    /**
     * Keeps track of the number of AI players.
     */
    private int numAIPlayer;
    /**
     * Keeps track of the size of the board.
     */
    public static final int SIZE_OF_BOARD = 40;
    /**
     * Keeps track of max number of houses.
     */
    public static final int MAX_HOUSE_VAL = 5;
    /**
     * Keeps track of the views.
     */
    private final List<BoardView> views;
    /**
     * Keeps track of when the game should end.
     */
    private boolean gameFinish;
    /**
     * Keeps track of the current player turn.
     */
    private Player turn;
    /**
     * Keeps track of the bank player.
     */
    private final Player bank;
    /**
     * Checks to see if the roll button was pressed.
     */
    private boolean checkDoubleRoll;
    /**
     * Checks to see if the animation is running.
     */
    private boolean animationRunning;

    /**
     * Keeps track of the possible board statuses.
     */
    public enum Status {
        GET_NUM_PLAYERS, CREATE_PLAYER_ICONS, INITIALIZE_BOARD, INITIALIZE_MONOPOLY, INITIALIZE_PLAYERS,
        GET_COMMAND, GO_TO_JAIL, EXIT_JAIL, FORCE_PAY_JAIL, GAME_OVER, PASS_GO, FREE_PARKING, PLAYER_INPUT
    }

    /**
     * Keeps track of the possible player commands.
     */
    public enum Command {
        BUY("Buy"),
        SELL("Sell"),
        PASS("Pass"),
        ROLL_AGAIN("Roll Again"),
        CELL_STATUS("Cell Status"),
        FORFEIT("Forfeit"),
        PAY_FEES("Pay Fees"),
        MOVE("Move"),
        REQUEST_SELL("Request Sell"),
        REQUEST_FORFEIT("Request Forfeit"),
        BUILD("Build"),
        PAINT_HOUSE("Paint House"),
        CHANGE_WINDOW("Change Window");

        private final String stringCommand;

        Command(String stringCommand) {
            this.stringCommand = stringCommand;
        }

        public String getStringCommand() {
            return stringCommand;
        }
    }

    /**
     * Keeps track of the player icons
     */
    public enum Icon {
        BOOT("boot", "images/icons/boot.png"),
        IRON("iron", "images/icons/iron.png"),
        SCOTTIE_DOG("scottie dog", "images/icons/scottie_dog.png"),
        BATTLESHIP("battleship", "images/icons/battleship.png"),
        TOP_HAT("top hat", "images/icons/top_hat.png"),
        WHEELBARROW("wheelbarrow", "images/icons/wheelbarrow.png"),
        THIMBLE("thimble", "images/icons/thimble.png"),
        RACING_CAR("racing car", "images/icons/racing_car.png"),
        BANK("bank", "");

        private final String name;
        private final String imgPath;
        private boolean used;

        Icon(String name, String imgPath) {
            this.name = name;
            this.imgPath = imgPath;
            this.used = false;
        }

        public String getImgPath() {
            return imgPath;
        }

        public String getName() {
            return name;
        }

        public boolean getUsed() {
            return used;
        }

        public void setUsed() {
            used = true;
        }
    }

    /**
     * Constructor for BoardModel, sets all values
     *
     * @author Sarah Chow 101143033
     * @author Kyra Lothrop 101145872
     * @author Bardia Parmoun 101143006
     * @author Owen VanDusen 101152022
     */
    public BoardModel() {
        views = new ArrayList<>();
        cells = new ArrayList<>();
        players = new ArrayList<>();
        dice = new int[]{0, 0};
        bank = new Player("Bank", Icon.BANK, 0);
        gameFinish = false;
        turn = null;
        numPlayers = 0;
        checkDoubleRoll = false;
        animationRunning = false;
    }

    /**
     * Select what method to call based on the given command.
     *
     * @param command the type of command to run, String
     * @author Kyra Lothrop 101145872
     */
    public void sendCommand(String command) {
        if (command.equals(Command.CHANGE_WINDOW.getStringCommand())) {
            repaint();
        } else if (command.equals(Command.ROLL_AGAIN.getStringCommand())) {
            checkDoubleRoll = true;
        } else if (command.equals(Command.PASS.getStringCommand())) {
            passTurn(turn);
        } else if (command.equals((Command.BUY.getStringCommand()))) {
            buyLocation(turn.getCurrentCell(), turn);
        } else if (command.equals((Command.SELL.getStringCommand()))) {
            sellLocationPrompt(turn);
        } else if (command.equals((Command.PAY_FEES.getStringCommand()))) {
            payFees(turn.getCurrentCell(), turn);
        } else if (command.equals((Command.CELL_STATUS.getStringCommand()))) {
            getCellStatus();
        } else if (command.equals((Command.BUILD.getStringCommand()))) {
            getBuildStatus();
        } else if (command.equals((Command.FORFEIT.getStringCommand()))) {
            request_forfeit(turn);
        }
        // Avoids race conditions.
        if (turn != null && !command.equals((Command.PASS.getStringCommand())) &&
                !command.equals((Command.FORFEIT.getStringCommand()))) {
            getCommand(turn);
        }
    }

    /**
     * Constructor for BoardModel, sets all values
     *
     * @author Sarah Chow 101143033
     * @author Kyra Lothrop 101145872
     * @author Bardia Parmoun 101143006
     * @author Owen VanDusen 101152022
     */
    public void constructBoard(String filename) {
        try {
            //File f = new File("board_config/rickAndMortyBoard.xml");
            File f = new File(filename);

            readSAX(f, new XMLParser(this, bank));

            sendBoardUpdate(new BoardEvent(this, Status.INITIALIZE_BOARD));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Adds a cell to the list of the cells in the board.
     * @param cell the cell to add to the board, BoardCell
     * @author Bardia Parmoun 101143006
     */
    public void addCell(BoardCell cell) {
        cells.add(cell);
    }

    /**
     * Accessor to add a view to the list of views.
     *
     * @param view the view of the board, BoardView
     * @author Owen VanDusen 101152022
     */
    public void addBoardView(BoardView view) {
        views.add(view);
    }

    /**
     * Accessor to remove a view from the list of views.
     *
     * @param view the view of the board, BoardView
     * @author Owen VanDusen 101152022
     */
    public void removeBoardView(BoardView view) {
        views.remove(view);
    }

    /**
     * Accessor to set the number of players starting in the game.
     *
     * @param numPlayers number of players, int
     * @author Kyra Lothrop 101145872
     */
    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    /**
     * Accessor to add a player to the board, starting on GO space.
     *
     * @param player the current player, Player
     * @author Bardia Parmoun 101143006
     */
    public void addPlayer(Player player) {
        player.setCurrentCell(this.cells.get(0));
        this.players.add(player);
    }

    /**
     * Getter for the number of AI players
     *
     * @return the number of AI players, int
     * @author Kyra Lothrop 101145872
     */
    public int getNumAIPlayer() {
        return numAIPlayer;
    }

    /**
     * Setter for the number of AI players
     *
     * @param numAIPlayer, number of AI players, int
     * @author Kyra Lothrop 101145872
     */
    public void setNumAIPlayer(int numAIPlayer) {
        this.numAIPlayer = numAIPlayer;
    }

    /**
     * Updates the views with a generated board event.
     *
     * @param monopolyEvent the event occurring on the board, MonopolyEvent
     * @author Sarah Chow 101143033
     */
    private void sendBoardUpdate(MonopolyEvent monopolyEvent) {
        for (BoardView view : views) {
            view.handleMonopolyUpdate(monopolyEvent);
        }
    }

    /**
     * Generates a board event to create players for the game based on the number of players selected.
     *
     * @author Kyra Lothrop 101145872
     */
    private void initiatePlayers() {
        sendBoardUpdate(new BoardEvent(this, Status.INITIALIZE_PLAYERS));
        sendBoardUpdate(new BoardEvent(this, Status.CREATE_PLAYER_ICONS));

        for (Player p : players) {
            move(p);
        }
    }

    /**
     * Generates a board event to get the number of players intending to play the game.
     *
     * @author Owen VanDusen 101152022
     */
    public void getNumPlayers() {
        sendBoardUpdate(new BoardEvent(this, Status.GET_NUM_PLAYERS));
    }

    /**
     * Determines which actions a player can take based on their current situation.
     *
     * @param player the active player, Player
     * @author Bardia Parmoun 101143006
     */
    public List<Command> getCommand(Player player) {
        BoardCell currentCell = player.getCurrentCell();
        ArrayList<BoardModel.Command> commands = new ArrayList<>();

        if (currentCell.getType() == BoardCell.CellType.PROPERTY ||
                currentCell.getType() == BoardCell.CellType.RAILROAD ||
                currentCell.getType() == BoardCell.CellType.UTILITY) {

            // Handles the buying command by checking to see if you can afford it
            // and the property has not been sold by you recently (you cannot buy back the property you just sold).
            if (currentCell.getOwner() == null &&
                    player.getCash() > ((Buyable) currentCell).getPrice() &&
                    !((Buyable) currentCell).getRecentlyChanged()) {
                commands.add(BoardModel.Command.BUY);
            }
        }

        // Handles selling the property
        if (player.getOwnedLocations(true).size() > 0) {
            commands.add(BoardModel.Command.SELL);
        }

        if (checkPlayerFeeStatus(player)) {
            commands.add(Command.PAY_FEES);

            if (player.getFeesStatus() != Player.StatusEnum.UNPAID_FEES) {
                commands.add(BoardModel.Command.PASS);
            }
        } else {
            // If the player has paid their fees they can pass or roll again.
            if (player.hasAnotherRoll() && !player.getResortInJail()) {
                commands.add(Command.ROLL_AGAIN);
            } else {
                commands.add(Command.PASS);
            }
        }

        //If the player has a complete neighborhood they can buy houses on those properties
        if (setBuildableProperties()) {
            commands.add(Command.BUILD);
        }

        // Handles the status commands.
        commands.add(BoardModel.Command.CELL_STATUS);

        // Handling forfeiting the game and declaring bankruptcy.
        commands.add(Command.FORFEIT);


        sendBoardUpdate(new BoardEvent(this, BoardModel.Status.GET_COMMAND, player, commands));

        return commands;
    }

    /**
     * Method to check the player's fee status.
     *
     * @param player player object to check status, Player
     * @return if the player needs to pay fees, boolean
     * @author Bardia Parmoun 101143006
     */
    private boolean checkPlayerFeeStatus(Player player) {
        BoardCell currentCell = player.getCurrentCell();

        if (currentCell.getType() == BoardCell.CellType.JAIL) {
            Jail jail = (Jail) currentCell;
            if (jail.isPlayersLastRound(player)) {
                player.setFeesStatus(Player.StatusEnum.UNPAID_FEES);
                sendBoardUpdate(new BoardEvent(this, Status.FORCE_PAY_JAIL, player));
                return true;
            } else return jail.getRoundCountJail(player) != 0 && player.getFeesStatus() != Player.StatusEnum.PAID_FEES;
        } // Checks to see if the cell has an owner and if you have paid the fees for it.
        else if (currentCell.getOwner() != player && currentCell.getOwner() != null &&
                player.getFeesStatus() != Player.StatusEnum.PAID_FEES) {
            player.setFeesStatus(Player.StatusEnum.UNPAID_FEES);
            return true;
        }

        return false;
    }

    /**
     * Calls the view to output the startup message.
     *
     * @author Sarah Chow 101143033
     */
    private void initializeMonopoly() {
        sendBoardUpdate(new BoardEvent(this, Status.INITIALIZE_MONOPOLY));
    }

    /**
     * Repaints the entire board to make sure the players are updated.
     *
     * @author Bardia Parmoun 101143006
     */
    private void repaint() {
        sendBoardUpdate(new PlayerEvent(this, Command.CHANGE_WINDOW, turn));
    }

    /**
     * Checks if the player owns any sets of properties and updates the buildableProperties arraylist to reflect
     * the current player's options
     *
     * @return whether or not the player can build houses on any of the properties.
     * @author Owen VanDusen 101152022
     */
    private boolean setBuildableProperties() {
        boolean buildable = false;
        ArrayList<Property.NeighborhoodEnum> buildableProperties = new ArrayList<>();
        if (turn != null) {
            int brown = 0, sky = 0, magenta = 0, orange = 0, red = 0, yellow = 0, green = 0, indigo = 0;
            for (BoardCell bc : turn.getOwnedLocations(false)) {
                if (bc.getType() == BoardCell.CellType.PROPERTY) {
                    Property p = (Property) bc;
                    switch (p.getNeighborhood()) {
                        case BROWN -> {
                            brown += 1;
                            if (p.getNeighborhood().getNumProperties() == brown) {
                                buildableProperties.add(Property.NeighborhoodEnum.BROWN);
                                buildable = true;
                            }
                        }
                        case SKY -> {
                            sky += 1;
                            if (p.getNeighborhood().getNumProperties() == sky) {
                                buildableProperties.add(Property.NeighborhoodEnum.SKY);
                                buildable = true;
                            }
                        }
                        case MAGENTA -> {
                            magenta += 1;
                            if (p.getNeighborhood().getNumProperties() == magenta) {
                                buildableProperties.add(Property.NeighborhoodEnum.MAGENTA);
                                buildable = true;
                            }
                        }
                        case ORANGE -> {
                            orange += 1;
                            if (p.getNeighborhood().getNumProperties() == orange) {
                                buildableProperties.add(Property.NeighborhoodEnum.ORANGE);
                                buildable = true;
                            }
                        }
                        case RED -> {
                            red += 1;
                            if (p.getNeighborhood().getNumProperties() == red) {
                                buildableProperties.add(Property.NeighborhoodEnum.RED);
                                buildable = true;
                            }
                        }
                        case YELLOW -> {
                            yellow += 1;
                            if (p.getNeighborhood().getNumProperties() == yellow) {
                                buildableProperties.add(Property.NeighborhoodEnum.YELLOW);
                                buildable = true;
                            }
                        }
                        case GREEN -> {
                            green += 1;
                            if (p.getNeighborhood().getNumProperties() == green) {
                                buildableProperties.add(Property.NeighborhoodEnum.GREEN);
                                buildable = true;
                            }
                        }
                        case INDIGO -> {
                            indigo += 1;
                            if (p.getNeighborhood().getNumProperties() == indigo) {
                                buildableProperties.add(Property.NeighborhoodEnum.INDIGO);
                                buildable = true;
                            }
                        }
                    }
                }
            }

            turn.setBuildableProperties(buildableProperties);
        }

        return buildable;
    }

    /**
     * Randomly sets the value of two integers between 1-6 to represent rolling two dice. If the player
     * rolls doubles allows them to move again. Calls the move method to move the player.
     *
     * @param player active player, Player
     * @author Kyra Lothrop 101145872
     */
    public void roll(Player player) {
        animationRunning = true;
        player.setRollAgain(false);
        Random rand = new Random();
        dice[0] = rand.nextInt((6 - 1) + 1) + 1;
        dice[1] = rand.nextInt((6 - 1) + 1) + 1;

        sendBoardUpdate(new PlayerEvent(this, Command.ROLL_AGAIN, player));

        if (dice[0] == dice[1]) {
            if (player.getResortInJail()) {
                player.addNumDoubles();
                exitJail(player, true);
            } else {
                setDoubleRoll(player);
            }
        }

        if (!player.getResortInJail()) {
            move(player);
        } else {
            animationRunning = false;
        }
    }

    /**
     * Moves the player a number of spaces equal to the value of the dice rolled.
     *
     * @param player the active player, Player
     * @author Bardia Parmoun 101143006
     */
    public void move(Player player) {
        int amountToMove = dice[0] + dice[1];
        animationRunning = true;
        sendBoardUpdate(new PlayerEvent(this, Command.MOVE, player, amountToMove, true));

        int originalPosition = player.getPosition();
        int newPlayerPosition = (player.getPosition() + amountToMove) % SIZE_OF_BOARD;

        player.setPosition(newPlayerPosition);
        player.setCurrentCell(cells.get(newPlayerPosition));
        animationRunning = false;

        // Player lands on go to Jail, do not have other options.
        if (player.getCurrentCell().getType() == BoardCell.CellType.GO_TO_JAIL) {
            sendToJail(player);
        } else {
            getCommand(player);
        }

        for (int i = 1; i <= amountToMove; i++) {
            if (cells.get((originalPosition + i) % SIZE_OF_BOARD).getType() == BoardCell.CellType.GO) {
                sendBoardUpdate(new BoardEvent(this, Status.PASS_GO, player));  //here
                player.setCash(player.getCash() + ((Go) cells.get(0)).getReward());
            }
        }

        getCommand(player);

        if (player.getCurrentCell().getType() == BoardCell.CellType.FREE_PARKING) {
            handleFreeParking(player);
        }
    }

    /**
     * Sends the player to jail
     *
     * @param player player in jail, Player
     * @author Sarah Chow 101143033
     */
    private void sendToJail(Player player) {
        sendBoardUpdate(new BoardEvent(this, Status.GO_TO_JAIL, player));

        int newPlayerPosition = BoardFrame.JAIL_LOCATION;
        player.setPosition(newPlayerPosition);
        player.setCurrentCell(cells.get(newPlayerPosition));
        player.setResortInJail(true); // Set to True
        ((Jail) player.getCurrentCell()).incrementJailRound(player);
    }

    /**
     * The player exits jail.
     *
     * @param player player in jail, Player
     * @author Bardia Parmoun 101143006
     */
    private void exitJail(Player player, boolean rolledDouble) {
        Jail jail = (Jail) player.getCurrentCell();
        jail.endJail(player);
        sendBoardUpdate(new BoardEvent(this, Status.EXIT_JAIL, player));
        player.setResortInJail(false); // Set to True

        if (!rolledDouble) {
            player.setRollAgain(true);
        }
    }

    /**
     * Assigns an unowned property that the player landed on to the player if they choose to buy it.
     *
     * @param cellToBuy property being bought, Property
     * @param player    player purchasing the property, Player
     * @author Owen VanDusen 101152022
     */
    public void buyLocation(BoardCell cellToBuy, Player player) {

        boolean result = false;

        if (player.getCash() > ((Buyable) cellToBuy).getPrice()) {
            player.buyLocation(cellToBuy);
            cellToBuy.setOwner(player);
            ((Buyable) cellToBuy).toggleRecentlyChanged();

            if (cellToBuy.getType() == BoardCell.CellType.RAILROAD) {
                player.setNumRailroadsOwned(player.getNumRailroadsOwned() + 1);
            } else if (cellToBuy.getType() == BoardCell.CellType.UTILITY) {
                player.setNumUtilitiesOwned(player.getNumUtilitiesOwned() + 1);
            }

            result = true;
        }

        sendBoardUpdate(new PlayerEvent(this, Command.BUY, player, result));
    }

    /**
     * Handles prompting the view for which property to sell.
     *
     * @param player player selling the property, Player
     * @author Sarah Chow 101143033
     */
    public void sellLocationPrompt(Player player) {
        sendBoardUpdate(new PlayerEvent(this, Command.REQUEST_SELL, player));
    }

    /**
     * Sells a property owned by the active player and removes it from their owned properties if
     * the property may be sold.
     *
     * @param player         player selling the property, Player
     * @param locationToSell the property to sell, Property
     * @author Sarah Chow 101143033
     */
    public void sellLocation(Player player, BoardCell locationToSell) {
        player.sellLocation(locationToSell);
        ((Buyable) locationToSell).toggleRecentlyChanged();
        locationToSell.setOwner(null);

        if (locationToSell.getType() == BoardCell.CellType.RAILROAD) {
            player.setNumRailroadsOwned(player.getNumRailroadsOwned() - 1);
        } else if (locationToSell.getType() == BoardCell.CellType.UTILITY) {
            player.setNumUtilitiesOwned(player.getNumUtilitiesOwned() - 1);
        }

        sendBoardUpdate(new PlayerEvent(this, Command.SELL, player, locationToSell));
    }

    /**
     * Accessor to display the information of a specific cell on the board.
     *
     * @author Kyra Lothrop 101145872
     */
    public void getCellStatus() {
        sendBoardUpdate(new PlayerEvent(this, Command.CELL_STATUS, turn));
    }

    /**
     * Accessor to open the build house UI for the current player.
     *
     * @author Owen VanDusen 1011522022
     */
    public void getBuildStatus() {
        sendBoardUpdate(new PlayerEvent(this, Command.BUILD, turn));
    }

    /**
     * Action taken when the player attempts to build a house on one of their properties .
     *
     * @param property the property that is having a house built on it
     * @param player   the player building the house
     * @author Owen VanDusen 101152022
     */
    public void buildHouse(Property property, Player player) {
        if (player.getCash() >= property.getNeighborhood().getHouseCost() &&
                property.getNumHouses() < MAX_HOUSE_VAL) {
            player.pay(property.getNeighborhood().getHouseCost());
            property.addHouse();
            sendBoardUpdate(new PlayerEvent(this, Command.PAINT_HOUSE, player, property));
        }

    }

    /**
     * Action the player must take when landing on property owned by another player. Pays the
     * rent of the property from the current player to the owner. Pays jail fee if that is the case.
     *
     * @param boardCell board cell with the fees, BoardCell
     * @param player    player paying rent, Player
     * @return if the player successfully paid fees.
     * @author Owen VanDusen 101152022
     */
    public boolean payFees(BoardCell boardCell, Player player) {
        boolean result = false;

        int fees = 0;

        if (boardCell.getType() == BoardCell.CellType.PROPERTY) {
            fees = ((Property) boardCell).getRent();
        } else if (boardCell.getType() == BoardCell.CellType.TAX) {
            fees = ((Tax) boardCell).getTax();
        } else if (boardCell.getType() == BoardCell.CellType.JAIL) {
            fees = Jail.FEE;
        } else if (boardCell.getType() == BoardCell.CellType.RAILROAD) {
            fees = ((Railroad) boardCell).getRent(boardCell.getOwner().getNumRailroadsOwned());
        } else if (boardCell.getType() == BoardCell.CellType.UTILITY) {
            fees = ((Utility) boardCell).getRent(boardCell.getOwner().getNumUtilitiesOwned(), getDice());
        }

        //If the player can't pay rent inform them
        if (player.getCash() < fees) {
            player.setFeesStatus(Player.StatusEnum.UNPAID_FEES);
        } else {
            boardCell.getOwner().getMoney(fees);
            player.pay(fees);
            player.setFeesStatus(Player.StatusEnum.PAID_FEES);
            result = true;

            if (boardCell.getType() == BoardCell.CellType.JAIL) {
                exitJail(player, false);
            }
        }

        //Inform player they have paid rent
        sendBoardUpdate(new PlayerEvent(this, Command.PAY_FEES, player, fees, result));

        return result;
    }

    /**
     * Action the player takes to end their turn. Resets many temporary board changes so the
     * next player can take actions as intended.
     *
     * @param player active player, Player
     * @author Sarah Chow 101143033
     */
    public void passTurn(Player player) {
        // Remove the recently changed from the player's cells.
        for (BoardCell cell : cells) {
            if (cell.getType() == BoardCell.CellType.PROPERTY ||
                    cell.getType() == BoardCell.CellType.RAILROAD ||
                    cell.getType() == BoardCell.CellType.UTILITY) {
                if (((Buyable) cell).getRecentlyChanged()) {
                    ((Buyable) cell).toggleRecentlyChanged(); // Set all to false
                }
            }
        }

        // Reset the player rent statuses
        player.setFeesStatus(Player.StatusEnum.NO_FEES);
        player.resetNumDoubles();

        // Reset the turn.
        turn = null;

        if (numPlayers > 1) {
            sendBoardUpdate(new PlayerEvent(this, Command.PASS, player));
        }
    }

    /**
     * Allows the player to move again if they rolled the same value on both dice.
     *
     * @param player active player, Player
     * @author Sarah Chow 101143033
     */
    public void setDoubleRoll(Player player) {
        player.setFeesStatus(Player.StatusEnum.NO_FEES);
        player.addNumDoubles();

        // Roll 3 doubles in a row
        if (player.getNumDoubles() >= Jail.JAIL_ROLL_LIMIT) {
            sendToJail(player);
            player.resetNumDoubles();
            getCommand(player);
        } else {
            player.setRollAgain(true);
        }
    }

    /**
     * Method if player requests to forfeit.
     *
     * @param player player who requested to forfeit.
     * @author Bardia Parmoun 101143006
     */
    public void request_forfeit(Player player) {
        sendBoardUpdate(new PlayerEvent(this, Command.REQUEST_FORFEIT, player));
    }

    /**
     * Action taken by the player to remove them from the turn sequence. They immediately go
     * bankrupt and assigned the next available rank in the end result of players.
     *
     * @param player active player, Player
     * @author Bardia Parmoun 101143006
     */
    public void forfeit(Player player) {
        player.setBankrupt();
        player.setRank(numPlayers--);
        sendBoardUpdate(new PlayerEvent(this, Command.FORFEIT, player));
        passTurn(player);
    }

    /**
     * Method called to finalize game results and output the player ranks.
     *
     * @author Owen VanDusen 101152022
     */
    public void gameOver() {
        sendBoardUpdate(new BoardEvent(this, Status.GAME_OVER));
    }

    /**
     * A method for handling free parking.
     *
     * @param player Player
     * @author Bardia Parmoun, 101143006
     */
    public void handleFreeParking(Player player) {
        player.getMoney(bank.getCash());
        bank.setCash(0);
        sendBoardUpdate(new BoardEvent(this, Status.FREE_PARKING, player));
    }

    /**
     * Finds the index of the cell in the board.
     * @param cell the cell to find the index, BoardCell
     * @return the index of the cell, int.
     */
    public int findCellIndex(BoardCell cell) {
        return cells.indexOf(cell);
    }

    /**
     * Returns the list of the cells on the board.
     *
     * @return the list of all the cells on the board.
     * @author Bardia Parmoun, 101143006
     */
    public List<BoardCell> getCells() {
        return cells;
    }

    /**
     * Returns the list of the players for the board.
     *
     * @return the list of all the players on the board.
     * @author Bardia Parmoun, 101143006
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Returns the number of players for the board.
     *
     * @return the number of players.
     * @author Bardia Parmoun, 101143006
     */
    public int getPlayerCount() {
        return numPlayers;
    }

    /**
     * Accessor method for the dice rolled using roll().
     *
     * @return Two element array representing each individual die rolled
     * @author Owen VanDusen 101152022
     */
    public int[] getDice() {
        return dice;
    }

    /**
     * Accessor method to set the dice values.
     *
     * @param dice1 first dice value, int
     * @param dice2 second dice value, int
     * @author Sarah Chow 101143033
     */
    public void setDice(int dice1, int dice2) {
        dice[0] = dice1;
        dice[1] = dice2;
    }

    /**
     * Accessor method to get the current turn.
     *
     * @return the current turn, Player
     * @author Owen VanDusen 101152022
     */
    public Player getCurrentTurn() {
        return turn;
    }

    /**
     * Returns the total money in the bank.
     *
     * @return the money inside the bank, int
     * @author Owen VanDusen 101152022
     */
    public int getBankMoney() {
        return bank.getCash();
    }

    /**
     * It parses the file with a parser
     *
     * @param f the file to parse, File
     * @param handler the parser, DefaultHandler
     * @throws Exception file not found.
     *
     * @author Bardia Parmoun 101143006
     */
    private void readSAX(File f, DefaultHandler handler) throws Exception {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser s = spf.newSAXParser();
        s.parse(f, handler);
    }

    /**
     * Primary loop of the program. Alternates the active players based on the list generated
     * and gets the actions they are able to take. Once there is only one player remaining finishes
     * the game and exits the loop.
     *
     * @author Kyra Lothrop 101145872
     */
    public void play() {
        constructBoard("board_config/originalBoard.xml"); //for debug
        initializeMonopoly();
        getNumPlayers();
        initiatePlayers();

        while (!gameFinish) {
            for (Player player : players) {
                turn = player;
                if (!player.isBankrupt()) {

                    // Checks to see if the player is in jail.
                    if (player.getResortInJail()) {
                        Jail jail = (Jail) player.getCurrentCell();
                        jail.incrementJailRound(player); // Pass their turn in jail
                    }

                    roll(player);
                    getCommand(player);

                    // Keeps prompting the player for commands until their turn is over.
                    while (turn != null) {
                        if (turn.isPlayerAI()) {
                            if (!animationRunning) {
                                ((AIPlayer) turn).nextMove();
                                try {
                                    TimeUnit.MILLISECONDS.sleep(50);
                                } catch (Exception e) {
                                    System.out.println("wait failed");
                                }
                            }
                        }

                        if (checkDoubleRoll) {
                            roll(player);
                            checkDoubleRoll = false;
                        }
                    }
                }

                // Checks to see if the game is over
                if (numPlayers == 1) {
                    gameFinish = true;
                    break;
                }
            }
        }

        gameOver();
    }
}