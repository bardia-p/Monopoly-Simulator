// Bardia Parmoun & Kyra Lothrop

import java.lang.reflect.Array;
import java.util.*;

public class BoardModel {
    private  List<Property> properties;
    private  List<Player> players;
    private  int[] dice;
    private  int numPlayers;

    private final int SIZE_OF_BOARD = 23;

    private List<BoardView> views;


    private boolean gameFinish;

    private Player turn;

    public enum Status {GET_NUM_PLAYERS, INITIALIZE_PLAYERS, GET_COMMAND}

    public enum Command{BUY, SELL, STATUS, PASS, ROLL_AGAIN}


    public BoardModel(){
        views = new ArrayList<>();
        properties = new ArrayList<>();
        players = new ArrayList<>();
        dice =  new int[2];
    }

    private void constructBoard(){
        properties.addAll(Arrays.asList(
                new Property("GO",0),
                new Property("Mediterranean Avenue",60),
                // Community Chest here
                new Property("Baltic Avenue",60),
                // Income Tax
                // Reading Railroad
                new Property("Oriental Avenue",100),
                // Chance Card
                new Property("Vermont Avenue",100),
                new Property("Connecticut Avenue",120),
                // JAIL!
                new Property("St. Charles Place",140),
                // Electric Company
                new Property("States Avenue",140),
                new Property("Virginia Avenue",160),
                // Pennsylvania Railroad
                new Property("St. James Place",180),
                // Community Chest
                new Property("Tennessee Avenue",180),
                new Property("New York Avenue",200),
                // FREE PARKING
                new Property("Kentucky Avenue",220),
                // Chance Card
                new Property("Indiana Avenue",220),
                new Property("Illinois Avenue",240),
                // B. & O. Railroad
                new Property("Atlantic Avenue",260),
                new Property("Ventnor Avenue",260),
                // Waterworks
                new Property("Marvin Garden",280),
                // GO TO JAIL ->
                new Property("Pacific Avenue",300),
                new Property("North Carolina Avenue",300),
                // Community Chest
                new Property("Pennsylvania Avenue",320),
                // Shortline Railroad
                // Chance Card
                new Property("Park Place",350),
                // Luxury Tax
                new Property("Boardwalk",500)
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
                System.out.printf("\nCurrent turn: %s\n", turn.getIcon());
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

        if (currentProperty.getOwner() == player){
            commands.add(BoardModel.Command.SELL);
        } else if (currentProperty.getOwner() == null && player.getCash() >= currentProperty.getPrice()) {
            commands.add(BoardModel.Command.BUY);
        }

        if (player.hasAnotherRoll()){
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

            result = true;
        }

        for (BoardView view : views) {
            view.handleBuyProperty(player, property, result);
        }
    }

    public void sellProperty(Property property, Player player){
        player.sellProperty(property);
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

    public void passTurn(){
        turn = null;
    }

    public void setDoubleRoll(Player player){
        player.setNumDubbles(player.getNumDubbles()+1);
        player.setRollAgain(true);

        for (BoardView view : views) {
            view.handleRollingDoubles(player);
        }
    }
}
