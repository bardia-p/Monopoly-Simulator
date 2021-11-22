import java.util.*;

/**
 * Group 3
 * SYSC 3110 - Milestone 2 BoardModel Class
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

    private List<Property.NeighborhoodEnum> buildableProperties;

    /**
     * Keeps track of the possible board statuses.
     */
    public enum Status {GET_NUM_PLAYERS, CREATE_PLAYER_ICONS, INITIALIZE_BOARD, INITIALIZE_MONOPOLY, INITIALIZE_PLAYERS,
        GET_COMMAND, PLAYER_ROLL, PLAYER_MOVE, BUY, SELL, PAY_FEES, BUILD, BUILD_SUCCESS, BUILD_FAIL, PAINT_HOUSE,
        PLAYER_STATUS, CELL_STATUS, PLAYER_FORFEIT, PLAYER_REQUEST_FORFEIT, PASS_TURN, REPAINT_BOARD, GAME_OVER}

    /**
     * Keeps track of the possible player commands.
     */
    public enum Command{
        BUY ("buy"),
        SELL ("sell"),
        PAY_RENT ("pay rent"),
        PLAYER_STATUS ("player status"),
        BOARD_STATUS ("board status"),
        PASS ("pass"),
        ROLL_AGAIN ("roll"),
        CELL_STATUS ("cell status"),
        BUILD("build house"),
        PAINT_BOARD("paint board"),
        FORFEIT ("forfeit"),
        CONFIRM_FORFEIT("confirm_forfeit"),
        PAY_TAX ("pay tax"),
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
        buildableProperties = new ArrayList<Property.NeighborhoodEnum>(8);
        bank = new Player("Bank", Icon.BANK);
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
            buyProperty((Property) turn.getCurrentCell() , turn);
        }
        else if(command.equals((Command.SELL.getStringCommand()))){
            sellPropertyPrompt(turn); //must prompt user for what to sell
        }
        else if(command.equals((Command.PAY_RENT.getStringCommand()))){
            payFees(turn.getCurrentCell(), turn);
        }
        else if(command.equals((Command.PAY_TAX.getStringCommand()))){
            payFees(turn.getCurrentCell(), turn);
        }
        else if (command.equals((Command.PLAYER_STATUS.getStringCommand()))){
            getPlayerStatus(turn);
        }
        else if (command.equals((Command.CELL_STATUS.getStringCommand()))){
            getCellStatus();
        }
        else if(command.equals((Command.BUILD.getStringCommand()))){
            getBuildStatus();
        }
        else if(command.equals((Command.FORFEIT.getStringCommand()))){
            request_forfeit(turn);
        }
        else if(command.equals((Command.CONFIRM_FORFEIT.getStringCommand()))){
            forfeit(turn);
        }
        //handling building houses on a given property
        else {
            buildHouse(command, turn);
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
                new Property("Mediterranean Avenue",60,2,10,30,90,160,250,
                        Property.NeighborhoodEnum.BROWN, "images/board/mediterranean.jpg"),
                new EmptyCell("Community Chest", BoardCell.CellType.UTILITY, "images/board/chest_1.jpg"),
                new Property("Baltic Avenue",60,4,20,60,180,320,450,
                        Property.NeighborhoodEnum.BROWN,"images/board/baltic.jpg"),
                new Tax( "Income Tax", 200, bank, "images/board/income_tax.jpg"),
                new EmptyCell("Reading Railroad", BoardCell.CellType.RAILROAD,
                        "images/board/railroad_1.jpg"),
                new Property("Oriental Avenue",100,6,30,90,270,400,550,
                        Property.NeighborhoodEnum.SKY,"images/board/oriental.jpg"),
                new EmptyCell("Chance Card", BoardCell.CellType.UTILITY, "images/board/chance_1.jpg"),
                new Property("Vermont Avenue",100,6,30,90,270,400,550,
                        Property.NeighborhoodEnum.SKY,"images/board/vermont.jpg"),
                new Property("Connecticut Avenue",120,8,40,100,300,450,600,
                        Property.NeighborhoodEnum.SKY,"images/board/connecticut.jpg"),
                new EmptyCell("JAIL", BoardCell.CellType.JAIL, "images/board/jail.jpg"),
                new Property("St. Charles Place",140,10,50,150,450,625,750,
                        Property.NeighborhoodEnum.MAGENTA,"images/board/st_charles.jpg"),
                new EmptyCell("Electric Company", BoardCell.CellType.UTILITY, "images/board/electric.jpg"),
                new Property("States Avenue",140,10,50,150,450,625,750,
                        Property.NeighborhoodEnum.MAGENTA,"images/board/states_avenue.jpg"),
                new Property("Virginia Avenue",160,12,60,180,500,700,900,
                        Property.NeighborhoodEnum.MAGENTA,"images/board/virginia.jpg"),
                new EmptyCell("Pennsylvania Railroad", BoardCell.CellType.RAILROAD,
                        "images/board/railroad_2.jpg"),
                new Property("St. James Place",180,14,70,200,550,750,950,
                        Property.NeighborhoodEnum.ORANGE,"images/board/st_james.jpg"),
                new EmptyCell("Community Chest", BoardCell.CellType.UTILITY, "images/board/chest_2.jpg"),
                new Property("Tennessee Avenue",180,14,70,200,550,750,950,
                        Property.NeighborhoodEnum.ORANGE,"images/board/tennessee.jpg"),
                new Property("New York Avenue",200,16,80,220,600,800,1000,
                        Property.NeighborhoodEnum.ORANGE,"images/board/new_york.jpg"),
                new EmptyCell("FREE PARKING", BoardCell.CellType.FREE_PARKING,"images/board/free_parking.jpg"),
                new Property("Kentucky Avenue",220,18,90,250,700,875,1050,
                        Property.NeighborhoodEnum.RED,"images/board/kentucky.jpg"),
                new EmptyCell("Chance Card", BoardCell.CellType.UTILITY, "images/board/chance_2.jpg"),
                new Property("Indiana Avenue",220,18,90,250,700,875,1050,
                        Property.NeighborhoodEnum.RED,"images/board/indiana.jpg"),
                new Property("Illinois Avenue",240,20,100,300,750,925,1100,
                        Property.NeighborhoodEnum.RED,"images/board/illinois.jpg"),
                new EmptyCell("B. & O. Railroad", BoardCell.CellType.RAILROAD,
                        "images/board/railroad_3.jpg"),
                new Property("Atlantic Avenue",260,22,110,330,800,975,1150,
                        Property.NeighborhoodEnum.YELLOW,"images/board/atlantic.jpg"),
                new Property("Ventnor Avenue",260,22,110,330,800,975,1150,
                        Property.NeighborhoodEnum.YELLOW,"images/board/ventnor.jpg"),
                new EmptyCell("Water Works", BoardCell.CellType.UTILITY, "images/board/water_works.jpg"),
                new Property("Marvin Garden",280,24,120,360,850,1025,1200,
                        Property.NeighborhoodEnum.YELLOW,"images/board/marvin.jpg"),
                new EmptyCell("GO TO JAIL", BoardCell.CellType.GO_TO_JAIL, "images/board/go_to_jail.jpg"),
                new Property("Pacific Avenue",300,26,130,390,900,1100,1275,
                        Property.NeighborhoodEnum.GREEN,"images/board/pacific.jpg"),
                new Property("North Carolina Avenue",300,26,130,390,900,1100,1275,
                        Property.NeighborhoodEnum.GREEN,"images/board/north_carolina.jpg"),
                new EmptyCell("Community Chest", BoardCell.CellType.UTILITY, "images/board/chest_3.jpg"),
                new Property("Pennsylvania Avenue",320,28,150,450,1000,1200,1400,
                        Property.NeighborhoodEnum.GREEN,"images/board/pennsylvania.jpg"),
                new EmptyCell("Shortline Railroad", BoardCell.CellType.RAILROAD,
                        "images/board/railroad_4.jpg"),
                new EmptyCell("Chance Card", BoardCell.CellType.UTILITY, "images/board/chance_3.jpg"),
                new Property("Park Place",350,35,175,500,1100,1300,1500,
                        Property.NeighborhoodEnum.INDIGO,"images/board/park_place.jpg"),
                new Tax( "Luxury Tax", 100, bank, "images/board/luxury_tax.jpg"),
                new Property("Boardwalk",500,50,200,600,1400,1700,2000,
                        Property.NeighborhoodEnum.INDIGO,"images/board/boardwalk.jpg")
        ));

        sendBoardUpdate(new BoardEvent(this,Status.INITIALIZE_BOARD, cells));
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
     * Accessor method for the dice rolled using roll(). ONLY USED FOR TEST CASES
     * @return Two element array representing each individual die rolled
     * @author Owen VanDusen 101152022
     */
    public int[] getDice() {
        return dice;
    }

    /**
     * Generates a board event to create players for the game based on the number of players selected.
     * @author Kyra Lothrop 101145872
     */
    private void initiatePlayers(){
        sendBoardUpdate(new BoardEvent(this, Status.INITIALIZE_PLAYERS, numPlayers));
        sendBoardUpdate(new BoardEvent(this, players, Status.CREATE_PLAYER_ICONS));

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

        if (currentCell.getType() == BoardCell.CellType.PROPERTY){
            Property currentProperty = (Property) currentCell;

            // Handles the buying command by checking to see if you can afford it
            // and the property has not been sold by you recently (you cannot buy back the property you just sold).
            if (currentProperty.getOwner() == null &&
                    player.getCash() >= currentProperty.getPrice() &&
                    !currentProperty.getRecentlyChanged()) {
                commands.add(Command.BUY);
            }
        }

        // Checks to see if the sell has an owner and if you have paid the fees for it.
        if (currentCell.getOwner() != player && currentCell.getOwner() != null &&
                player.getFeesStatus() != Player.StatusEnum.PAID_FEES) {
            if (currentCell.getType() == BoardCell.CellType.PROPERTY){
                commands.add(Command.PAY_RENT);
            } else if (currentCell.getType() == BoardCell.CellType.TAX){
                commands.add(Command.PAY_TAX);
            }
            player.setFeesStatus(Player.StatusEnum.UNPAID_FEES);
        }

        // Handles selling the property
        if (player.getProperties(true).size() > 0){
            commands.add(Command.SELL);
        }

        // If the player has paid their rent they can pass or roll again.
        if (player.getFeesStatus() != Player.StatusEnum.UNPAID_FEES) {
            if (player.hasAnotherRoll()) {
                commands.add(Command.ROLL_AGAIN);
            } else {
                commands.add(Command.PASS);
            }
        }

        //If the player has a complete neighborhood they can buy houses on those properties
        if(setBuildableProperties()){
            commands.add(Command.BUILD);
        };

        // Handles the status commands.
        commands.add(Command.PLAYER_STATUS);
        commands.add(Command.CELL_STATUS);

        // Handling forfeiting the game and declaring bankruptcy.
        commands.add(Command.FORFEIT);


        sendBoardUpdate(new BoardEvent(this, BoardModel.Status.GET_COMMAND, commands, player));
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


    private boolean setBuildableProperties(){
        boolean buildable = false;
        buildableProperties.clear();
        if(turn != null) {
            int brown = 0, sky = 0, magenta = 0, orange = 0, red = 0, yellow = 0, green = 0, indigo = 0;
            for (Property p : turn.getProperties(false)) {
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
                            buildableProperties.add( Property.NeighborhoodEnum.MAGENTA);
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
        return buildable;
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

        move(player, 1);
    }

    /**
     * Moves the player a number of spaces equal to the value of the dice rolled.
     * @author Bardia Parmoun 101143006
     * @param player the active player, Player
     * @param amountToMove distance the player should move, int
     */
    public void move(Player player, int amountToMove){
        sendBoardUpdate(new BoardEvent(this, Status.PLAYER_MOVE, player, amountToMove, player.getPosition()));
        int newPlayerPosition = (player.getPosition() + amountToMove) % SIZE_OF_BOARD;
        player.setPosition(newPlayerPosition);
        player.setCurrentCell(cells.get(newPlayerPosition));
        getCommand(player);
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
     * Handles prompting the view for which property to sell.
     * @author Sarah Chow 101143033
     * @param player player selling the property, Player
     */
    public void sellPropertyPrompt(Player player){
        sendBoardUpdate(new BoardEvent(this, Status.SELL, player));

        if (player.getConfirmSell()){
            sellProperty(player, player.getPropertyToSell());
        }
    }

    /**
     * Sells a property owned by the active player and removes it from their owned properties if
     * the property may be sold.
     * @author Sarah Chow 101143033
     * @param player player selling the property, Player
     * @param propertyToSell the property to sell, Property
     */
    public void sellProperty(Player player, Property propertyToSell){
        player.sellProperty(propertyToSell);
        propertyToSell.toggleRecentlyChanged();
        propertyToSell.setOwner(null);
    }

    /**
     * Accessor to display the information of the current player.
     * @author Sarah Chow 101143033
     * @param player active player, Player
     */
    public void getPlayerStatus(Player player){
        sendBoardUpdate(new BoardEvent(this, Status.PLAYER_STATUS, player));
    }


    /**
     * Accessor to display the information of a specific cell on the board.
     * @author Kyra Lothrop 101145872
     */
    public void getCellStatus(){
        sendBoardUpdate(new BoardEvent(this, Status.CELL_STATUS, turn));
    }

    public void getBuildStatus(){
        sendBoardUpdate(new BoardEvent(this, Status.BUILD, turn, buildableProperties));
    }

    public void buildHouse(String propertyName, Player player){
        Property realEstate = null;

        for(Property p: player.getProperties(false)){
            if(propertyName.equals(p.getName())){
                realEstate = p;
                break;
            }
        }
        //player can afford house on the property
        if(player.getCash() >= realEstate.getNeighborhood().getHouseCost() &&
                realEstate.getNumHouses() < 5){
            player.pay(realEstate.getNeighborhood().getHouseCost());
            realEstate.addHouse();
            sendBoardUpdate(new BoardEvent(this, Status.BUILD_SUCCESS, player));
            sendBoardUpdate(new BoardEvent(this, Status.PAINT_HOUSE, realEstate));
        }
        else {
            sendBoardUpdate(new BoardEvent(this, Status.BUILD_FAIL));
        }
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
        for (BoardCell cell: cells){
            if (cell.getType() == BoardCell.CellType.PROPERTY){
                Property p = (Property) cell;
                if (p.getRecentlyChanged()){
                    p.toggleRecentlyChanged(); // Set all to false
                }
            }
        }

        // Reset the player rent statuses
        player.setFeesStatus(Player.StatusEnum.NO_FEES);
        player.resetNumDoubles();

        // Reset the turn.
        turn = null;

        if (numPlayers > 1) {
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
    }

    public void request_forfeit(Player player){
        sendBoardUpdate(new BoardEvent(this, Status.PLAYER_REQUEST_FORFEIT, player));
        if (player.getRequest_forfeit()){
            forfeit(player);
        }
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
        sendBoardUpdate(new BoardEvent(this, players, Status.GAME_OVER));
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

    public int findCellIndex(BoardCell cell){
        return cells.indexOf(cell);
    }
}