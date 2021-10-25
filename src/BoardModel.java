import java.util.*;

/**
 * Group 3
 * SYSC 3110 - Milestone 1 BoardModel Class
 *
 * This document is the BoardModel. This class has a list of properties, a list of players, an array of dice,
 * the number of players, the size of the board, the board views, if the game has finished, the current turn,
 * the bank, the board statuses and the player commands. This is the primary brain class that handles
 * nearly all logic needed to make monopoly run.
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 1.0
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
    private  int numPlayers;
    /**
     * Keeps track of the size of the board.
     */
    private static final int SIZE_OF_BOARD = 25;
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
     * Keeps track of the possible board statuses.
     */
    public enum Status {GET_NUM_PLAYERS, INITIALIZE_MONOPOLY, INITIALIZE_PLAYERS, GET_COMMAND, PLAYER_ROLL,
        PLAYER_DOUBLE_ROLL, PLAYER_MOVE, BUY, SELL, PAY_FEES, PLAYER_STATUS, CELL_STATUS, BOARD_STATUS, PLAYER_FORFEIT,
        PASS_TURN, GAME_OVER}
    /**
     * Keeps track of the possible player commands.
     */
    public enum Command{
        BUY ("buy"),
        SELL ("sell"),
        PAY_RENT ("pay rent"),
        STATUS ("status"),
        BOARD_STATUS ("board status"),
        PASS ("pass"),
        ROLL_AGAIN ("roll"),
        CELL_STATUS ("cell status"),
        FORFEIT ("forfeit"),
        PAY_TAX ("pay tax");

        private final String stringCommand;

        Command(String stringCommand){
            this.stringCommand = stringCommand;
        }

        public String getStringCommand(){
            return stringCommand;
        }
    }

    /**
     * Constructor for BoardModel, sets all values
     * @author Sarah Chow 101143033
     * @author Kyra Lothrop 101145872
     * @author Bardia Parmoun 101143006
     * @author Owen VanDusen 101152022
     */
    public BoardModel(){
        views = new ArrayList<>();
        cells = new ArrayList<>();
        players = new ArrayList<>();
        dice =  new int[2];
        bank = new Player("Bank", "Bank");
        gameFinish = false;
        turn = null;
        numPlayers = 0;
    }

    /**
     * Constructor for BoardModel, sets all values
     * @author Sarah Chow 101143033
     * @author Kyra Lothrop 101145872
     * @author Bardia Parmoun 101143006
     * @author Owen VanDusen 101152022
     */
    private void constructBoard(){
        cells.addAll(Arrays.asList(
                new Go(200),
                new Property("Mediterranean Avenue",60,2),
                // Community Chest here
                new Property("Baltic Avenue",60,4),
                new Tax( "Income Tax", 200, bank),
                // Reading Railroad
                new Property("Oriental Avenue",100,6),
                // Chance Card
                new Property("Vermont Avenue",100,6),
                new Property("Connecticut Avenue",120,8),
                // JAIL!
                new Property("St. Charles Place",140,10),
                // Electric Company
                new Property("States Avenue",140,10),
                new Property("Virginia Avenue",160,12),
                // Pennsylvania Railroad
                new Property("St. James Place",180,14),
                // Community Chest
                new Property("Tennessee Avenue",180,14),
                new Property("New York Avenue",200,16),
                // FREE PARKING
                new Property("Kentucky Avenue",220,18),
                // Chance Card
                new Property("Indiana Avenue",220,18),
                new Property("Illinois Avenue",240,20),
                // B. & O. Railroad
                new Property("Atlantic Avenue",260,22),
                new Property("Ventnor Avenue",260,22),
                // Waterworks
                new Property("Marvin Garden",280,24),
                // GO TO JAIL ->
                new Property("Pacific Avenue",300,26),
                new Property("North Carolina Avenue",300,26),
                // Community Chest
                new Property("Pennsylvania Avenue",320,28),
                // Shortline Railroad
                // Chance Card
                new Property("Park Place",350,35),
                new Tax( "Luxury Tax", 100, bank),
                new Property("Boardwalk",500,50)
        ));
    }

    /**
     * Accessor to add a view to the list of views.
     * @author Owen VanDusen 101152022
     * @param view the view of the board, BoardView
     */
    public void addBoardView (BoardView view){
        views.add(view);
    }

    /**
     * Accessor to remove a view from the list of views.
     * @author Owen VanDusen 101152022
     * @param view the view of the board, BoardView
     */
    public void removeBoardView (BoardView view){
        views.remove(view);
    }

    /**
     * Accessor to set the number of players starting in the game.
     * @author Kyra Lothrop 101145872
     * @param numPlayers number of players, int
     */
    public void setNumPlayers(int numPlayers){
        this.numPlayers = numPlayers;
    }

    /**
     * Accessor to add a player to the board, starting on GO space.
     * @author Bardia Parmoun 101143006
     * @param player the current player, Player
     */
    public void addPlayer(Player player){
        player.setCurrentCell(this.cells.get(0));
        this.players.add(player);
    }

    /**
     * Updates the views with a generated board event.
     * @author Sarah Chow 101143033
     * @param boardEvent the event occurring on the board, BoardEvent
     */
    private void sendBoardUpdate(BoardEvent boardEvent){
        for (BoardView view : views) {
            view.handleBoardUpdate(boardEvent);
        }
    }

    /**
     * Generates a board event to create players for the game based on the number of players selected.
     * @author Kyra Lothrop 101145872
     */
    private void initiatePlayers(){
        sendBoardUpdate(new BoardEvent(this, Status.INITIALIZE_PLAYERS, numPlayers));
    }

    /**
     * Generates a board event to get the number of players intending to play the game.
     * @author Owen VanDusen 101152022
     */
    public void getNumPlayers() {
        sendBoardUpdate(new BoardEvent(this, Status.GET_NUM_PLAYERS));
    }

    /**
     * Determines which actions a player can take based on their current situation.
     * @author Bardia Parmoun 101143006
     * @param player the active player, Player
     */
    public void getCommand(Player player){
        BoardCell currentCell = player.getCurrentCell();
        ArrayList<BoardModel.Command> commands = new ArrayList<>();

        if (currentCell.getType() == BoardCell.CellType.PROPERTY){
            Property currentProperty = (Property) currentCell;

            // Handles the buying command by checking to see if you can afford it
            // and the property has not been sold by you recently (you cannot buy back the property you just sold).
            if (currentProperty.getOwner() == null &&
                    player.getCash() >= currentProperty.getPrice() &&
                    !currentProperty.getRecentlyChanged()) {
                commands.add(BoardModel.Command.BUY);
            }
        }

        // Checks to see if the sell has an owner and if you have paid the fees for it.
        if (currentCell.getOwner() != player && currentCell.getOwner() != null &&
                player.getFeesStatus() != Player.StatusEnum.PAID_FEES) {
            if (currentCell.getType() == BoardCell.CellType.PROPERTY){
                commands.add(BoardModel.Command.PAY_RENT);
            } else if (currentCell.getType() == BoardCell.CellType.TAX){
                commands.add(BoardModel.Command.PAY_TAX);
            }
            player.setFeesStatus(Player.StatusEnum.UNPAID_FEES);
        }



        // Handles selling the property
        if (player.getProperties(true).size() > 0){
            commands.add(BoardModel.Command.SELL);
        }

        // If the player has paid their rent they can pass or roll again.
        if (player.getFeesStatus() != Player.StatusEnum.UNPAID_FEES) {
            if (player.hasAnotherRoll()) {
                commands.add(Command.ROLL_AGAIN);
            } else {
                commands.add(BoardModel.Command.PASS);
            }
        }

        // Handles the status commands.
        commands.add(BoardModel.Command.STATUS);
        commands.add(BoardModel.Command.BOARD_STATUS);
        commands.add(BoardModel.Command.CELL_STATUS);

        // Handling forfeiting the game and declaring bankruptcy.
        commands.add(Command.FORFEIT);

        sendBoardUpdate(new BoardEvent(this, BoardModel.Status.GET_COMMAND, player, commands));
    }

    /**
     * Calls the view to output the startup message.
     * @author Sarah Chow 101143033
     */
    private void initializeMonopoly(){
        sendBoardUpdate(new BoardEvent(this, Status.INITIALIZE_MONOPOLY));
    }

    /**
     * Randomly sets the value of two integers between 1-6 to represent rolling two dice. If the player
     * rolls doubles allows them to move again. Calls the move method to move the player.
     * @author Kyra Lothrop 101145872
     * @param player active player, Player
     */
    public void roll(Player player){
        player.setRollAgain(false);
        Random rand = new Random();
        dice[0] = rand.nextInt((6 - 1) + 1) + 1;
        dice[1] = rand.nextInt((6 - 1) + 1) + 1;

        if (dice[0] == dice[1]){
            setDoubleRoll(player);
        }

        sendBoardUpdate(new BoardEvent(this, Status.PLAYER_ROLL, player, dice));

        move(player, dice[0] + dice[1]);
    }

    /**
     * Moves the player a number of spaces equal to the value of the dice rolled.
     * @author Bardia Parmoun 101143006
     * @param player the active player, Player
     * @param amountToMove distance the player should move, int
     */
    public void move(Player player, int amountToMove){
        int newPlayerPosition = (player.getPosition() + amountToMove) % SIZE_OF_BOARD;
        player.setPosition(newPlayerPosition);
        player.setCurrentCell(cells.get(newPlayerPosition));

        sendBoardUpdate(new BoardEvent(this, Status.PLAYER_MOVE, player));
    }

    /**
     * Assigns an unowned property that the player landed on to the player if they choose to buy it.
     * @author Owen VanDusen 101152022
     * @param property property being bought, Property
     * @param player player purchasing the property, Player
     */
    public void buyProperty(Property property, Player player){
        boolean result = false;
        if (player.getCash() > property.getPrice()){
            player.buyProperty(property);
            property.setOwner(player);
            property.toggleRecentlyChanged();

            result = true;
        }

        sendBoardUpdate(new BoardEvent(this, Status.BUY, player, property, result));

    }

    /**
     * Sells a property owned by the active player and removes it from their owned properties if
     * the property may be sold.
     * @author Bardia Parmoun 101143006
     * @param property property being sold, Property
     * @param player player selling the property, Player
     */
    public void sellProperty(Property property, Player player){
        player.sellProperty(property);
        property.toggleRecentlyChanged();
        property.setOwner(null);

        sendBoardUpdate(new BoardEvent(this, Status.SELL, player, property, true));
    }

    /**
     * Accessor to display the information of the current player.
     * @author Sarah Chow 101143033
     * @param player active player, Player
     */
    public void getStatus(Player player){
        sendBoardUpdate(new BoardEvent(this, Status.PLAYER_STATUS, player));
    }

    /**
     * Accessor to display the information of the players on each view.
     * @author Owen VanDusen 101152022
     */
    public void getBoardStatus(){
        sendBoardUpdate(new BoardEvent(this, Status.BOARD_STATUS, players));
    }

    /**
     * Accessor to display the information of a specific cell on the board.
     * @author Kyra Lothrop 101145872
     */
    public void getCellStatus(){
        sendBoardUpdate(new BoardEvent(this, Status.CELL_STATUS, turn));
    }

    /**
     * Action the player must take when landing on property owned by another player. Pays the
     * rent of the property from the current player to the owner.
     * @author Owen VanDusen 101152022
     * @param boardCell board cell with the fees, BoardCell
     * @param player player paying rent, Player
     */
    public void payFees(BoardCell boardCell, Player player){
        boolean result = false;

        int fees = 0;

        if (boardCell.getType() == BoardCell.CellType.PROPERTY){
            fees = ((Property) boardCell).getRent();
        } else if (boardCell.getType() == BoardCell.CellType.TAX){
            fees = ((Tax) boardCell).getTax();
        }

        //If the player can't pay rent inform them
        if(player.getCash() < fees){
            player.setFeesStatus(Player.StatusEnum.UNPAID_FEES);
        }
        else{
            boardCell.getOwner().getMoney(fees);
            player.pay(fees);
            player.setFeesStatus(Player.StatusEnum.PAID_FEES);
            result = true;
        }

        //Inform player they have paid rent
        sendBoardUpdate(new BoardEvent(this, Status.PAY_FEES, player, fees, result));
    }

    /**
     * Action the player takes to end their turn. Resets many temporary board changes so the
     * next player can take actions as intended.
     * @author Sarah Chow 101143033
     * @param player active player, Player
     */
    public void passTurn(Player player){
        // Remove the recently changed from the player's cells.
        for (Property property : player.getProperties(false)){
            if (property.getRecentlyChanged()){
                property.toggleRecentlyChanged(); // Set all to false
            }
        }

        // Reset the player rent statuses
        player.setFeesStatus(Player.StatusEnum.NO_FEES);
        player.resetNumDoubles();

        // Reset the turn.
        turn = null;

        if (numPlayers >1) {
            sendBoardUpdate(new BoardEvent(this, Status.PASS_TURN, player));
        }
    }

    /**
     * Allows the player to move again if they rolled the same value on both dice.
     * @author Sarah Chow 101143033
     * @param player active player, Player
     */
    public void setDoubleRoll(Player player){
        player.setFeesStatus(Player.StatusEnum.NO_FEES);
        player.addNumDoubles();
        player.setRollAgain(true);

        sendBoardUpdate(new BoardEvent(this, Status.PLAYER_DOUBLE_ROLL, player));
    }

    /**
     * Action taken by the player to remove them from the turn sequence. They immediately go
     * bankrupt and assigned the next available rank in the end result of players.
     * @author Bardia Parmoun 101143006
     * @param player active player, Player
     */
    public void forfeit(Player player) {
        player.setBankrupt();
        player.setRank(numPlayers--);
        sendBoardUpdate(new BoardEvent(this, Status.PLAYER_FORFEIT, player));
        passTurn(player);
    }

    /**
     * Method called to finalize game results and output the player ranks.
     * @author Owen VanDusen 101152022
     */
    public void gameOver(){
        sendBoardUpdate(new BoardEvent(this, Status.GAME_OVER, players));
    }

    /**
     * Primary loop of the program. Alternates the active players based on the list generated
     * and gets the actions they are able to take. Once there is only one player remaining finishes
     * the game and exits the loop.
     * @author Kyra Lothrop 101145872
     */
    public void play(){
        constructBoard();
        initializeMonopoly();
        getNumPlayers();
        initiatePlayers();

        while(!gameFinish){
            for (Player player: players){
                turn = player;
                if (!player.isBankrupt()){
                    roll(player);

                    // Keeps prompting the player for commands until their turn is over.
                    while (turn != null){
                        getCommand(player);
                    }
                }

                // Checks to see if the game is over
                if (numPlayers == 1){
                    gameFinish = true;
                    break;
                }
            }
        }

        gameOver();
    }
}