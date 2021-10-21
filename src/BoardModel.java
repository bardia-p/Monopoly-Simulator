// Bardia Parmoun & Kyra Lothrop

import java.util.*;

public class BoardModel {
    private static List<Property> properties;
    private static List<Player> players;
    private static int[] dice;
    private static int numPlayers;

    private final int SIZE_OF_BOARD = 23;

    private List<BoardView> views;


    private boolean gameFinish;

    private int turnsPassed;

    public enum Status {GET_NUM_PLAYERS, INITIALIZE_PLAYERS, ROLL, MOVE_PLAYER, GET_PLAYER_STATUS}

    private Status boardStatus;

    public BoardModel(){
        views = new ArrayList<>();
        properties = new ArrayList<>();
        players = new ArrayList<>();
        turnsPassed = 0;
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

        for (Player player: players){
            System.out.println(player);
        }

        while(!gameFinish){
            for (Player player: players){
                if (!player.getBankrupt()){
                    roll(player);
                    move(player, dice[0] + dice[1]);
                    getPlayerStatus(player);
                    while (dice[0] == dice[1]){
                        System.out.println("You rolled a double");
                        roll(player);
                        move(player, dice[0] + dice[1]);
                        getPlayerStatus(player);
                    }
                }
            }
            /*
            //which player
            turn = turnsPassed % numPlayers;
            String currentIcon = activeIcons.get(turn);

            playerTurn(currentIcon);

            turnsPassed++;*/
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

    public void setNumPlayers(int numPlayers){
        this.numPlayers = numPlayers;
    }

    public void setDice(int die1, int die2){
        dice[0] = die1;
        dice[1] = die2;
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
        for (BoardView view : views) {
            view.handleBoardUpdate(new BoardEvent(this, Status.ROLL, player));
        }
    }

    public void move(Player player, int amountToMove){
        for (BoardView view : views) {
            view.handleBoardUpdate(new BoardEvent(this, Status.MOVE_PLAYER, player, amountToMove));
        }
    }

    public void getPlayerStatus(Player player){
        for (BoardView view : views) {
            view.handleBoardUpdate(new BoardEvent(this, Status.GET_PLAYER_STATUS, player));
        }
    }

    public void updatePlayer(Player newPlayer, int newPosition){
        for (Player player: players){
            if (player.getIcon().equals(newPlayer.getIcon())){
                int newPlayerPosition = (player.getPosition() + newPosition) % SIZE_OF_BOARD;
                player.setPosition(newPlayerPosition);
                player.setCurrentProperty(properties.get(newPlayerPosition));
            }
        }
    }

    /*
    public boolean isPropertyBought(int propertyIndex){
        boolean propertyCheck = false;
        for (BoardView view : views){
            List<String> propertyStatus = view.handleGetPropertyStatus(propertyIndex);

            if (propertyStatus.get(1) != null){
                propertyCheck = true;
            }
        }
        return propertyCheck;
    }

    public boolean buyProperty (int propertyIndex, String playerIcon){
        boolean success = true;
        for (BoardView view: views){
            success &= view.handleBuyProperty(propertyIndex, playerIcon);
        }
        return success;
    }

    public boolean payRent (int propertyIndex, String playerIcon){
        boolean success = true;
        for (BoardView view: views){
            success &= view.handlePayRent(propertyIndex, playerIcon);
        }

        if (!success){
            this.updateStatus(playerIcon, false);
        }

        return success;
    }

    public boolean addPlayer(String playerName, String playerIcon){
        boolean success = true;
        for (BoardView view: views){
            success &= view.handleNewPlayer(playerName, playerIcon);
        }
        return success;
    }

    public String getBoardStatus() {return boardStatus.getPlayer();}

    private void updateStatus(String playerName, boolean status) {
        if (status){
            boardStatus.PLAYERWON.setPlayer(playerName);
        } else {
            boardStatus.PLAYERLOST.setPlayer(playerName);
        }
    }

    public String getPropertyName(int propertyIndex){
        String finalString = "";
        for (BoardView view : views){
            finalString = (view.handleGetPropertyStatus(propertyIndex)).get(0);
        }
        return finalString;
    }

    public int getPropertyRent(int propertyIndex){
        int rent = 0;
        for (BoardView view : views){
            rent = Integer.valueOf(view.handleGetPropertyStatus(propertyIndex).get(3));
        }
        return rent;
    }

    public int getPlayerCash(String playerIcon){
        int cash = 0;
        for (BoardView view : views){
            cash = Integer.valueOf(view.handleGetPlayerStatus(playerIcon).get(2));
        }
        return cash;
    }

    public String getPropertyStatus(int propertyIndex){
        String finalString = "";
        int i = 0;
        List<String> propertyItemLabel = Arrays.asList(
                "Name of Property = ",
                "Owner of Property = ",
                "Price of Property = ",
                "Rent of Property = "
        );

        for (BoardView view : views){
            for(String propetyStatusItem : view.handleGetPropertyStatus(propertyIndex)){
                finalString += propertyItemLabel.get(i) + propetyStatusItem + "\n";
                i++;
            }
        }
        return finalString;
    }

    public String getPlayerStatus(String playerIcon){
        String finalString = "";
        int i = 0;
        List<String> playerStatusLabel = Arrays.asList(
                "Name of Player = ",
                "Icon of Player = ",
                "Cash of Player = "
        );


        for (BoardView view : views){
            for(String playerStatusItem : view.handleGetPlayerStatus(playerIcon)){
                finalString += playerStatusLabel.get(i) + playerStatusItem + "\n";
                i++;
            }
        }
        return finalString;

    }*/

}
