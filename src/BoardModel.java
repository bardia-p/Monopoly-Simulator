import java.util.*;

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
    private  int numPlayers;
    /**
     * Keeps track of the size of the board.
     */
    public static final int SIZE_OF_BOARD = 40;
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
     * Keeps track of the possible board statuses.
     */
    public enum Status {GET_NUM_PLAYERS, CREATE_PLAYER_ICONS, INITIALIZE_BOARD, INITIALIZE_MONOPOLY, INITIALIZE_PLAYERS,
        GET_COMMAND, PLAYER_ROLL, PLAYER_MOVE, BUY, SELL, PAY_FEES, PLAYER_STATUS, CELL_STATUS, GO_TO_JAIL,
        EXIT_JAIL, FORCE_PAY_JAIL, PLAYER_REQUEST_FORFEIT, PASS_TURN, REPAINT_BOARD, GAME_OVER, PASS_GO, FREE_PARKING}

    /**
     * Keeps track of the possible player commands.
     */
    public enum Command{
        BUY ("buy"),
        SELL ("sell"),
        PLAYER_STATUS ("player status"),
        PASS ("pass"),
        ROLL_AGAIN ("roll"),
        CELL_STATUS ("cell status"),
        FORFEIT ("forfeit"),
        PAY_FEES ("pay fees"),
        REPAINT("repaint");

        private final String stringCommand;

        Command(String stringCommand){
            this.stringCommand = stringCommand;
        }

        public String getStringCommand(){
            return stringCommand;
        }
    }

    /**
     * Keeps track of the player icons
     */
    public enum Icon{
        BOOT ("boot","images/icons/boot.png"),
        IRON ("iron","images/icons/iron.png"),
        SCOTTIE_DOG ("scottie dog","images/icons/scottie_dog.png"),
        BATTLESHIP ("battleship", "images/icons/battleship.png"),
        TOP_HAT ("top hat", "images/icons/top_hat.png"),
        WHEELBARROW ("wheelbarrow", "images/icons/wheelbarrow.png"),
        THIMBLE ("thimble","images/icons/thimble.png"),
        RACING_CAR ("racing car","images/icons/racing_car.png"),
        BANK("bank", "");

        private final String name;
        private final String imgPath;
        private boolean used;

        Icon(String name, String imgPath){
            this.name = name;
            this.imgPath = imgPath;
            this.used = false;
        }

        public String getImgPath(){
            return imgPath;
        }

        public String getName(){ return name;}

        public boolean getUsed(){return used;}

        public void setUsed(){
            used = true;
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
        bank = new Player("Bank", Icon.BANK, 0);
        gameFinish = false;
        turn = null;
        numPlayers = 0;
        checkDoubleRoll = false;
    }

    /**
     * Select what method to call based on the given command.
     * @author Kyra Lothrop 101145872
     * @param command the type of command to run, Strng
     */
    public void sendCommand(String command) {
        if(command.equals(Command.REPAINT.getStringCommand())){
            repaint();
        }
        else if(command.equals(Command.ROLL_AGAIN.getStringCommand())){
            checkDoubleRoll = true;
        }
        else if(command.equals(Command.PASS.getStringCommand())){
            passTurn(turn);
        }
        else if(command.equals((Command.BUY.getStringCommand()))){
            buyLocation(turn.getCurrentCell(), turn);
        }
        else if(command.equals((Command.SELL.getStringCommand()))){
            sellLocationPrompt(turn);
        }
        else if(command.equals((Command.PAY_FEES.getStringCommand()))){
            payFees(turn.getCurrentCell(), turn);
        }
        else if (command.equals((Command.PLAYER_STATUS.getStringCommand()))){
            getPlayerStatus(turn);
        }
        else if (command.equals((Command.CELL_STATUS.getStringCommand()))){
            getCellStatus();
        }
        else if(command.equals((Command.FORFEIT.getStringCommand()))){
            request_forfeit(turn);
        }

        // Avoids race conditions.
        if(turn!= null && !command.equals((Command.PASS.getStringCommand())) &&
                !command.equals((Command.FORFEIT.getStringCommand()))) {
            getCommand(turn);
        }
    }


    /**
     * Constructor for BoardModel, sets all values
     * @author Sarah Chow 101143033
     * @author Kyra Lothrop 101145872
     * @author Bardia Parmoun 101143006
     * @author Owen VanDusen 101152022
     */
    public void constructBoard(){
        cells.addAll(Arrays.asList(
                new Go(200, "images/board/go.jpg"),
                new Property("Mediterranean Avenue",60,2, "images/board/mediterranean.jpg"),
                new EmptyCell("Community Chest", BoardCell.CellType.CHANCE_AND_CHEST, "images/board/chest_1.jpg"),
                new Property("Baltic Avenue",60,4, "images/board/baltic.jpg"),
                new Tax( "Income Tax", 200, bank, "images/board/income_tax.jpg"),
                new Railroad("Reading Railroad", 200, new Integer[]{25, 50, 100, 200}, "images/board/railroad_1.jpg"),
                new Property("Oriental Avenue",100,6, "images/board/oriental.jpg"),
                new EmptyCell("Chance Card", BoardCell.CellType.CHANCE_AND_CHEST, "images/board/chance_1.jpg"),
                new Property("Vermont Avenue",100,6, "images/board/vermont.jpg"),
                new Property("Connecticut Avenue",120,8, "images/board/connecticut.jpg"),
                new Jail("JAIL", bank,"images/board/jail.jpg"),
                new Property("St. Charles Place",140,10, "images/board/st_charles.jpg"),
                new Utility("Electric Company", 150,  new Integer[]{4, 10}, "images/board/electric.jpg"),
                new Property("States Avenue",140,10, "images/board/states_avenue.jpg"),
                new Property("Virginia Avenue",160,12, "images/board/virginia.jpg"),
                new Railroad("Pennsylvania Railroad", 200, new Integer[]{25, 50, 100, 200},  "images/board/railroad_2.jpg"),
                new Property("St. James Place",180,14, "images/board/st_james.jpg"),
                new EmptyCell("Community Chest", BoardCell.CellType.CHANCE_AND_CHEST, "images/board/chest_2.jpg"),
                new Property("Tennessee Avenue",180,14, "images/board/tennessee.jpg"),
                new Property("New York Avenue",200,16, "images/board/new_york.jpg"),
                new FreeParking("images/board/free_parking.jpg"),
                new Property("Kentucky Avenue",220,18, "images/board/kentucky.jpg"),
                new EmptyCell("Chance Card", BoardCell.CellType.CHANCE_AND_CHEST, "images/board/chance_2.jpg"),
                new Property("Indiana Avenue",220,18, "images/board/indiana.jpg"),
                new Property("Illinois Avenue",240,20, "images/board/illinois.jpg"),
                new Railroad("B. & O. Railroad", 200, new Integer[]{25, 50, 100, 200}, "images/board/railroad_3.jpg"),
                new Property("Atlantic Avenue",260,22, "images/board/atlantic.jpg"),
                new Property("Ventnor Avenue",260,22, "images/board/ventnor.jpg"),
                new Utility("Water Works", 150,  new Integer[]{4, 10}, "images/board/water_works.jpg"),
                new Property("Marvin Garden",280,24, "images/board/marvin.jpg"),
                new GoToJail("GO TO JAIL", "images/board/go_to_jail.jpg"),
                new Property("Pacific Avenue",300,26, "images/board/pacific.jpg"),
                new Property("North Carolina Avenue",300,26, "images/board/north_carolina.jpg"),
                new EmptyCell("Community Chest", BoardCell.CellType.CHANCE_AND_CHEST, "images/board/chest_3.jpg"),
                new Property("Pennsylvania Avenue",320,28, "images/board/pennsylvania.jpg"),
                new Railroad("Shortline Railroad", 200, new Integer[]{25, 50, 100, 200}, "images/board/railroad_4.jpg"),
                new EmptyCell("Chance Card", BoardCell.CellType.CHANCE_AND_CHEST, "images/board/chance_3.jpg"),
                new Property("Park Place",350,35, "images/board/park_place.jpg"),
                new Tax( "Luxury Tax", 100, bank, "images/board/luxury_tax.jpg"),
                new Property("Boardwalk",500,50, "images/board/boardwalk.jpg")
        ));

        sendBoardUpdate(new BoardEvent(this,Status.INITIALIZE_BOARD));
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
     * @param monopolyEvent the event occurring on the board, MonopolyEvent
     */
    private void sendBoardUpdate(MonopolyEvent monopolyEvent){
        for (BoardView view : views) {
            view.handleMonopolyUpdate(monopolyEvent);
        }
    }

    /**
     * Generates a board event to create players for the game based on the number of players selected.
     * @author Kyra Lothrop 101145872
     */
    private void initiatePlayers(){
        sendBoardUpdate(new BoardEvent(this, Status.INITIALIZE_PLAYERS));
        sendBoardUpdate(new BoardEvent(this, Status.CREATE_PLAYER_ICONS));

        for (Player p : players){
            move(p, 0);
        }
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

        if (currentCell.getType() == BoardCell.CellType.PROPERTY ||
                currentCell.getType() == BoardCell.CellType.RAILROAD ||
                currentCell.getType() == BoardCell.CellType.UTILITY){

            // Handles the buying command by checking to see if you can afford it
            // and the property has not been sold by you recently (you cannot buy back the property you just sold).
            if (currentCell.getOwner() == null &&
                    player.getCash() >= ((Buyable) currentCell).getPrice() &&
                    !((Buyable)currentCell).getRecentlyChanged()) {
                commands.add(BoardModel.Command.BUY);
            }
        }

        // Handles selling the property
        if (player.getOwnedLocations(true).size() > 0){
            commands.add(BoardModel.Command.SELL);
        }

        if (checkPlayerFeeStatus(player)){
            commands.add(Command.PAY_FEES);

            if (player.getFeesStatus() != Player.StatusEnum.UNPAID_FEES){
                commands.add(BoardModel.Command.PASS);
            }
        } else {
            // If the player has paid their fees they can pass or roll again.
            if (player.hasAnotherRoll() && !player.getResortInJail()) {
                commands.add(Command.ROLL_AGAIN);
            } else {
                commands.add(BoardModel.Command.PASS);
            }
        }

        // Handles the status commands.
        commands.add(BoardModel.Command.PLAYER_STATUS);
        commands.add(BoardModel.Command.CELL_STATUS);

        // Handling forfeiting the game and declaring bankruptcy.
        commands.add(Command.FORFEIT);


        sendBoardUpdate(new BoardEvent(this, BoardModel.Status.GET_COMMAND, player, commands));
    }

    /**
     * Method to check the player's fee status.
     * @author Bardia Parmoun 101143006
     * @param player player object to check status, Player
     * @return if the player needs to pay fees, boolean
     */
    private boolean checkPlayerFeeStatus(Player player){
        BoardCell currentCell = player.getCurrentCell();

        if (currentCell.getType() == BoardCell.CellType.JAIL){
            Jail jail = (Jail) currentCell;
            if (jail.isPlayersLastRound(player)){
                player.setFeesStatus(Player.StatusEnum.UNPAID_FEES);
                sendBoardUpdate(new PlayerEvent(this, Status.FORCE_PAY_JAIL, player));
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
     * @author Sarah Chow 101143033
     */
    private void initializeMonopoly(){
        sendBoardUpdate(new BoardEvent(this, Status.INITIALIZE_MONOPOLY));
    }

    /**
     * Repaints the entire board to make sure the players are updated.
     * @author Bardia Parmoun 101143006
     */
    private void repaint() {
        sendBoardUpdate(new BoardEvent(this, Status.REPAINT_BOARD));
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

        sendBoardUpdate(new BoardEvent(this, Status.PLAYER_ROLL));

        if (dice[0] == dice[1]){
            if (player.getResortInJail()){
                player.addNumDoubles();
                exitJail(player, true);
            }
            else{
                setDoubleRoll(player);
            }
        }


        if (!player.getResortInJail()){
            move(player, dice[0] + dice[1]);
        }
    }

    /**
     * Moves the player a number of spaces equal to the value of the dice rolled.
     * @author Bardia Parmoun 101143006
     * @param player the active player, Player
     * @param amountToMove distance the player should move, int
     */
    public void move(Player player, int amountToMove){
        sendBoardUpdate(new PlayerEvent(this, Status.PLAYER_MOVE, player, amountToMove, true));

        int originalPosition = player.getPosition();
        int newPlayerPosition = (player.getPosition() + amountToMove) % SIZE_OF_BOARD;

        player.setPosition(newPlayerPosition);
        player.setCurrentCell(cells.get(newPlayerPosition));

        // Player lands on go to Jail, do not have other options.
        if (player.getCurrentCell().getType() == BoardCell.CellType.GO_TO_JAIL) {
            sendToJail(player);
        } else {
            getCommand(player);
        }

        for(int i = 1; i<=amountToMove; i++){
            if(cells.get((originalPosition + i)% SIZE_OF_BOARD).getType() == BoardCell.CellType.GO) {
                sendBoardUpdate(new PlayerEvent(this, Status.PASS_GO, player));  //here
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
     * @author Sarah Chow 101143033
     * @param player player in jail, Player
     */
    private void sendToJail(Player player){
        sendBoardUpdate(new PlayerEvent(this, Status.GO_TO_JAIL, player));

        int newPlayerPosition = BoardFrame.JAIL_LOCATION;
        player.setPosition(newPlayerPosition);
        player.setCurrentCell(cells.get(newPlayerPosition));
        player.setResortInJail(true); // Set to True
        ((Jail)player.getCurrentCell()).incrementJailRound(player);
    }

    /**
     * The player exits jail.
     * @author Bardia Parmoun 101143006
     * @param player player in jail, Player
     */
    private void exitJail(Player player, boolean rolledDouble){
        Jail jail = (Jail) player.getCurrentCell();
        jail.endJail(player);
        sendBoardUpdate(new PlayerEvent(this, Status.EXIT_JAIL, player));
        player.setResortInJail(false); // Set to True

        if (!rolledDouble){
            player.setRollAgain(true);
        }
    }

    /**
     * Assigns an unowned property that the player landed on to the player if they choose to buy it.
     * @author Owen VanDusen 101152022
     * @param cellToBuy property being bought, Property
     * @param player player purchasing the property, Player
     */
    public void buyLocation(BoardCell cellToBuy, Player player){

        boolean result = false;

        if (player.getCash() > ((Buyable) cellToBuy).getPrice()){
            player.buyLocation(cellToBuy);
            cellToBuy.setOwner(player);
            ((Buyable) cellToBuy).toggleRecentlyChanged();

            if(cellToBuy.getType() == BoardCell.CellType.RAILROAD){
                player.setNumRailroadsOwned(player.getNumRailroadsOwned() + 1);
            }
            else if(cellToBuy.getType() == BoardCell.CellType.UTILITY){
                player.setNumUtilitiesOwned(player.getNumUtilitiesOwned() + 1);
            }

            result = true;
        }

        sendBoardUpdate(new PlayerEvent(this, Status.BUY, player, result));

    }

    /**
     * Handles propmprting the view for which property to sell.
     * @author Sarah Chow 101143033
     * @param player player selling the property, Player
     */
    public void sellLocationPrompt(Player player){
        sendBoardUpdate(new PlayerEvent(this, Status.SELL, player));
    }

    /**
     * Sells a property owned by the active player and removes it from their owned properties if
     * the property may be sold.
     * @author Sarah Chow 101143033
     * @param player player selling the property, Player
     * @param locationToSell the property to sell, Property
     */
    public void sellProperty(Player player, BoardCell locationToSell){
        player.sellProperty(locationToSell);
        ((Buyable) locationToSell).toggleRecentlyChanged();
        locationToSell.setOwner(null);

        if(locationToSell.getType() == BoardCell.CellType.RAILROAD){
            player.setNumRailroadsOwned(player.getNumRailroadsOwned() - 1);
        }
        else if(locationToSell.getType() == BoardCell.CellType.UTILITY){
            player.setNumUtilitiesOwned(player.getNumUtilitiesOwned() - 1);
        }
    }

    /**
     * Accessor to display the information of the current player.
     * @author Sarah Chow 101143033
     * @param player active player, Player
     */
    public void getPlayerStatus(Player player){
        sendBoardUpdate(new PlayerEvent(this, Status.PLAYER_STATUS, player));
    }


    /**
     * Accessor to display the information of a specific cell on the board.
     * @author Kyra Lothrop 101145872
     */
    public void getCellStatus(){
        sendBoardUpdate(new PlayerEvent(this, Status.CELL_STATUS, turn));
    }

    /**
     * Action the player must take when landing on property owned by another player. Pays the
     * rent of the property from the current player to the owner. Pays jail fee if that is the case.
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
        } else if (boardCell.getType() == BoardCell.CellType.JAIL){
            fees = Jail.FEE;
        } else if (boardCell.getType() == BoardCell.CellType.RAILROAD){
            fees = ((Railroad) boardCell).getRent(boardCell.getOwner().getNumRailroadsOwned());
        } else if (boardCell.getType() == BoardCell.CellType.UTILITY){
            fees = ((Utility) boardCell).getRent(boardCell.getOwner().getNumUtilitiesOwned(), getDice());
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

            if (boardCell.getType() == BoardCell.CellType.JAIL){
                exitJail(player, false);
            }
        }

        //Inform player they have paid rent
        sendBoardUpdate(new PlayerEvent(this, Status.PAY_FEES, player, fees, result));
    }

    /**
     * Action the player takes to end their turn. Resets many temporary board changes so the
     * next player can take actions as intended.
     * @author Sarah Chow 101143033
     * @param player active player, Player
     */
    public void passTurn(Player player){
        // Remove the recently changed from the player's cells.
        for (BoardCell cell: cells){
            if (cell.getType() == BoardCell.CellType.PROPERTY ||
                    cell.getType() == BoardCell.CellType.RAILROAD ||
                    cell.getType() == BoardCell.CellType.UTILITY){
                if (((Buyable) cell).getRecentlyChanged()){
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
            sendBoardUpdate(new PlayerEvent(this, Status.PASS_TURN, player));
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

        // Roll 3 doubles in a row
        if (player.getNumDoubles() >= Jail.JAIL_ROLL_LIMIT){
            sendToJail(player);
            player.resetNumDoubles();
        }
        else{
            player.setRollAgain(true);
        }
    }

    /**
     * Method if player requests to forfeit.
     * @author Bardia Parmoun 101143006
     * @param player player who requested to forfeit.
     */
    public void request_forfeit(Player player){
        sendBoardUpdate(new PlayerEvent(this, Status.PLAYER_REQUEST_FORFEIT, player));
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
        passTurn(player);
    }

    /**
     * Method called to finalize game results and output the player ranks.
     * @author Owen VanDusen 101152022
     */
    public void gameOver(){
        sendBoardUpdate(new BoardEvent(this, Status.GAME_OVER));
    }

    /**
     * A method for handling free parking.
     * @param player Player
     * @author Bardia Parmoun, 101143006
     */
    public void handleFreeParking(Player player){
        player.getMoney(bank.getCash());
        bank.setCash(0);
        sendBoardUpdate(new PlayerEvent(this, Status.FREE_PARKING, player));
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

                    // Checks to see if the player is in jail.
                    if (player.getResortInJail()){
                        Jail jail = (Jail) player.getCurrentCell();
                        jail.incrementJailRound(player); // Pass their turn in jail
                    }

                    roll(player);
                    getCommand(player);

                    // Keeps prompting the player for commands until their turn is over.
                    while (turn != null){
                        if (checkDoubleRoll){
                            roll(player);
                            checkDoubleRoll = false;
                        }
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

    /**
     * Returns the list of the cells on the board.
     * @return the list of all the cells on the board.
     * @author Bardia Parmoun, 101143006
     */
    public List<BoardCell> getCells (){
        return cells;
    }

    /**
     * Returns the list of the players for the board.
     * @return the list of all the players on the board.
     * @author Bardia Parmoun, 101143006
     */
    public List<Player> getPlayers (){
        return players;
    }

    /**
     * Returns the number of players for the board.
     * @return the number of players.
     * @author Bardia Parmoun, 101143006
     */
    public int getPlayerCount(){
        return numPlayers;
    }

    /**
     * Accessor method for the dice rolled using roll().
     * @return Two element array representing each individual die rolled
     * @author Owen VanDusen 101152022
     */
    public int[] getDice() {
        return dice;
    }

    /**
     * Accessor method to set the dice values. This is for testing.
     * @author Sarah Chow 101143033
     * @param dice1 first dice value, int
     * @param dice2 second dice value, int
     */
    public void setDice(int dice1, int dice2){
        dice[0] = dice1;
        dice[1] = dice2;
    }

    /**
     * Accessor method to get the current turn.
     * @return the current turn, Player
     * @author Owen VanDusen 101152022
     */
    public Player getCurrentTurn() {
        return turn;
    }

    /**
     * Returns the total money in the bank.
     * @return the money inside the bank, int
     * @author Owen VanDusen 101152022
     */
    public int getBankMoney(){
        return bank.getCash();
    }
}