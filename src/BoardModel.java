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

    public enum Command{BUY, SELL, STATUS, NEXT}


    public BoardModel(){
        views = new ArrayList<>();
        properties = new ArrayList<>();
        players = new ArrayList<>();
        dice =  new int[2];
    }

    private void constructBoard(){
        properties.addAll(Arrays.asList(
                new Property("GO"),
                new Property("Mediterranean Avenue"),
                // Community Chest here
                new Property("Baltic Avenue"),
                // Income Tax
                // Reading Railroad
                new Property("Oriental Avenue"),
                // Chance Card
                new Property("Vermont Avenue"),
                new Property("Connecticut Avenue"),
                // JAIL!
                new Property("St. Charles Place"),
                // Electric Company
                new Property("States Avenue"),
                new Property("Virginia Avenue"),
                // Pennsylvania Railroad
                new Property("St. James Place"),
                // Community Chest
                new Property("Tennessee Avenue"),
                new Property("New York Avenue"),
                // FREE PARKING
                new Property("Kentucky Avenue"),
                // Chance Card
                new Property("Indiana Avenue"),
                new Property("Illinois Avenue"),
                // B. & O. Railroad
                new Property("Atlantic Avenue"),
                new Property("Ventnor Avenue"),
                // Waterworks
                new Property("Marvin Garden"),
                // GO TO JAIL ->
                new Property("Pacific Avenue"),
                new Property("North Carolina Avenue"),
                // Community Chest
                new Property("Pennsylvania Avenue"),
                // Shortline Railroad
                // Chance Card
                new Property("Park Place"),
                // Luxury Tax
                new Property("Boardwalk")
        ));

        for (int i = 0; i < properties.size(); i++){
            properties.get(i).setPropertyIndex(i);
        }
    }

    public void play(){
        constructBoard();
        getNumPlayers();
        initiatePlayers();

        while(!gameFinish){
            for (Player player: players){
                turn = player;
                System.out.printf("Current turn: %s\n", turn.getIcon());
                if (!player.isBankrupt()){
                    roll(player);
                    move(player, dice[0] + dice[1]);
                    while (dice[0] == dice[1]){
                        System.out.println("You rolled a double");
                        roll(player);
                        move(player, dice[0] + dice[1]);
                    }

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

        commands.add(BoardModel.Command.NEXT);

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
        Random rand = new Random();
        dice[0] = rand.nextInt((6 - 1) + 1) + 1;
        dice[1] = rand.nextInt((6 - 1) + 1) + 1;

        for (BoardView view : views) {
            view.handleRoll(dice[0], dice[1], player);
        }
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
}
