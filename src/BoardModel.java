// Bardia Parmoun & Kyra Lothrop

import java.lang.reflect.Array;
import java.util.*;

public class BoardModel {
    private  List<Property> properties;
    private  List<Player> players;
    private  int[] dice;
    private  int numPlayers;

    private final int SIZE_OF_BOARD = 24;

    private List<BoardView> views;


    private boolean gameFinish;

    private Player turn;

    private Player bank;

    public enum Status {GET_NUM_PLAYERS, INITIALIZE_PLAYERS, GET_COMMAND}

    public enum Command{BUY, SELL, PAY_RENT, STATUS, PASS, ROLL_AGAIN}


    public BoardModel(){
        views = new ArrayList<>();
        properties = new ArrayList<>();
        players = new ArrayList<>();
        dice =  new int[2];
        bank = new Player("Bank", "Bank");
    }

    private void constructBoard(){
        properties.addAll(Arrays.asList(
                new Property("GO",0,0,false, bank),
                new Property("Mediterranean Avenue",60,2, true),
                // Community Chest here
                new Property("Baltic Avenue",60,4, true),
                new Property( "Income Tax", 0, 200, false, bank),
                // Reading Railroad
                new Property("Oriental Avenue",100,6, true),
                // Chance Card
                new Property("Vermont Avenue",100,6, true),
                new Property("Connecticut Avenue",120,8, true),
                // JAIL!
                new Property("St. Charles Place",140,10, true),
                // Electric Company
                new Property("States Avenue",140,10, true),
                new Property("Virginia Avenue",160,12, true),
                // Pennsylvania Railroad
                new Property("St. James Place",180,14, true),
                // Community Chest
                new Property("Tennessee Avenue",180,14, true),
                new Property("New York Avenue",200,16, true),
                // FREE PARKING
                new Property("Kentucky Avenue",220,18, true),
                // Chance Card
                new Property("Indiana Avenue",220,18, true),
                new Property("Illinois Avenue",240,20, true),
                // B. & O. Railroad
                new Property("Atlantic Avenue",260,22, true),
                new Property("Ventnor Avenue",260,22, true),
                // Waterworks
                new Property("Marvin Garden",280,24, true),
                // GO TO JAIL ->
                new Property("Pacific Avenue",300,26, true),
                new Property("North Carolina Avenue",300,26, true),
                // Community Chest
                new Property("Pennsylvania Avenue",320,28, true),
                // Shortline Railroad
                // Chance Card
                new Property("Park Place",350,35, true),
                // Luxury Tax
                new Property("Boardwalk",500,50, true)
        ));

        for (int i = 0; i < properties.size(); i++){
            properties.get(i).setPropertyIndex(i);
        }
    }

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

                    while (turn != null){
                        getCommand(player);
                    }
                }
            }
        }
    }

    private void initiatePlayers(){
        for (BoardView view : views) {
            view.handleBoardUpdate(new BoardEvent(this, Status.INITIALIZE_PLAYERS, numPlayers));
        }
    }

    private void initializeMonopoly(){
        for (BoardView view : views) {
            view.handleWelcomeMonopoly();
        }
    }

    public void getNumPlayers() {
        for (BoardView view : views) {
            view.handleBoardUpdate(new BoardEvent(this, Status.GET_NUM_PLAYERS));
        }
    }

    public void getCommand(Player player){
        Property currentProperty = player.getCurrentProperty();
        ArrayList<BoardModel.Command> commands = new ArrayList<>();

        commands.add(BoardModel.Command.STATUS);

        if(currentProperty.getOwner() != player && currentProperty.getOwner() != null &&
                (player.getRentStatus() == Player.StatusEnum.UNPAID_RENT ||
                        player.getRentStatus() == Player.StatusEnum.NO_RENT)) {
            commands.add(BoardModel.Command.PAY_RENT);
            player.setRentStatus(Player.StatusEnum.UNPAID_RENT);

        } else if (currentProperty.getOwner() == player && !currentProperty.getRecentlyChanged()){
                commands.add(BoardModel.Command.SELL);

        } else if (currentProperty.getOwner() == null &&
                player.getCash() >= currentProperty.getPrice() &&
                !currentProperty.getRecentlyChanged()) {
                commands.add(BoardModel.Command.BUY);
        }

        if (player.hasAnotherRoll() && player.getRentStatus() != Player.StatusEnum.UNPAID_RENT){
            commands.add(Command.ROLL_AGAIN);
        } else {
            commands.add(BoardModel.Command.PASS);
        }

        for (BoardView view : views) {
            view.handleBoardUpdate(new BoardEvent(this, BoardModel.Status.GET_COMMAND, player, commands));
        }

    }

    public void setNumPlayers(int numPlayers){
        this.numPlayers = numPlayers;
    }


    public void addPlayer(Player player){
        player.setCurrentProperty(this.properties.get(0));
        this.players.add(player);
    }

    public void addBoardView (BoardView view){
        views.add(view);
    }

    public void removeBoardView (BoardView view){
        views.remove(view);
    }

    public void roll(Player player){
        player.setRollAgain(false);
        Random rand = new Random();
        dice[0] = rand.nextInt((6 - 1) + 1) + 1;
        dice[1] = rand.nextInt((6 - 1) + 1) + 1;

        if (dice[0] == dice[1]){
            setDoubleRoll(player);
        }

        for (BoardView view : views) {
            view.handleRoll(dice[0], dice[1], player);
        }

        move(player, dice[0] + dice[1]);
    }

    public void move(Player player, int amountToMove){
        int newPlayerPosition = (player.getPosition() + amountToMove) % SIZE_OF_BOARD;
        player.setPosition(newPlayerPosition);
        player.setCurrentProperty(properties.get(newPlayerPosition));
        for (BoardView view : views) {
            view.showCurrentProperty(player);
        }
    }

    public void buyProperty(Property property, Player player){
        boolean result = false;
        if (player.getCash() > property.getPrice()){
            player.buyProperty(property);
            property.setOwner(player);
            property.toggleRecentlyChanged();

            result = true;
        }

        for (BoardView view : views) {
            view.handleBuyProperty(player, property, result);
        }
    }

    public void sellProperty(Property property, Player player){
        player.sellProperty(property);
        property.toggleRecentlyChanged();
        property.setOwner(null);

        for (BoardView view : views) {
            view.handleSellProperty(player, property, true);
        }
    }

    public void getStatus(Player player){
        for (BoardView view : views) {
            view.handleGetPlayerStatus(player);
        }
    }

    public void payRent(Property property, Player player){
        boolean result = false;

        //If the player can't pay rent inform them
        if(player.getCash() < property.getRent()){
            player.setRentStatus(Player.StatusEnum.UNPAID_RENT);
        }
        else{
            property.getOwner().getMoney(property.getRent());
            player.pay(property.getRent());
            player.setRentStatus(Player.StatusEnum.PAID_RENT);
            result = true;
        }


        //Inform player they have paid rent
        for (BoardView view: views) {
            view.handlePayRent(property, player, result);
        }
    }

    public void passTurn(Player player){
        if(player.getRentStatus() == Player.StatusEnum.UNPAID_RENT) {
            player.setBankrupt(true);
        }
        else{
            for (Property property : player.getProperties()){
                if (property.getRecentlyChanged()){
                    property.toggleRecentlyChanged(); // Set all to false
                }
            }
            player.setRentStatus(Player.StatusEnum.NO_RENT);
        }
        turn = null;

        for (BoardView view: views) {
            view.handleCurrentPlayerChange();
        }
    }

    public void setDoubleRoll(Player player){
        player.setRentStatus(Player.StatusEnum.NO_RENT);
        player.setNumDubbles(player.getNumDubbles()+1);
        player.setRollAgain(true);

        for (BoardView view : views) {
            view.handleRollingDoubles(player);
        }
    }
}
