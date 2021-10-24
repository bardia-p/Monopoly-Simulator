
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * SYSC 3110 - Milestone 1 BoardListener Class
 *
 * This document is the BoardListener. This class has the BoardModel and BoardController. BoardListener
 * handles outputting information for the user to see and formatting that output.
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 1.0
 */
public class BoardListener implements BoardView {
    /**
     * Keeps track of the board model.
     */
    BoardModel model;
    /**
     * Keeps track of the board controller.
     */
    BoardController controller;

    /**
     * Constructor for the Board listener, creates the board model, adds the board listener to the board model,
     * creates the board controller and runs the play method.
     */
    public BoardListener(){
        model = new BoardModel();
        model.addBoardView(this);
        controller = new BoardController();
        model.play();
    }

    /**
     * Passes a BoardEvent to the BoardController to be interpreted.
     * @param e the board event, BoardEvent
     */
    @Override
    public void handleBoardUpdate(BoardEvent e) {
        controller.eventListener(e);
    }

    /**
     * Passes the values of both dice and the player to the BoardController to be interpreted.
     * @param die1 value of first roll, int
     * @param die2 value of second roll, int
     * @param player player performing actions, Player
     */
    @Override
    public void handleRoll(int die1, int die2, Player player){
        System.out.println("\nRolling dice for: " + player.getIcon().toUpperCase());
        System.out.printf("---> You rolled a %d and a %d\n", die1, die2);
        System.out.printf("---> Total: %d\n\n", die1 + die2);
    }

    /**
     * Displays the name of the property the current player is standing on in the terminal.
     * @param player player performing actions, Player
     */
    @Override
    public void showCurrentProperty(Player player){
        System.out.printf("Player %s is currently at: %s\n",
                player.getIcon().toUpperCase(),
                player.getCurrentProperty().getName());
    }

    /**
     * Displays whether the current player can afford the property they attempted to buy or not.
     * @param player player performing actions, Player
     * @param property property that is in contention for purchasing, Property
     * @param result if the player can afford the property, boolean
     */
    @Override
    public void handleBuyProperty(Player player, Property property, boolean result) {
        if (result){
            System.out.printf("\nPlayer %s bought %s\n", player.getIcon().toUpperCase(), property.getName());
        } else {
            System.out.printf("\nPlayer %s cannot afford %s\n", player.getIcon().toUpperCase(), property.getName());
        }
    }

    /**
     * Displays whether the current player can sell the property they attempted to sell.
     * @param player player performing actions, Player
     * @param property property that is in contention for selling, Property
     * @param result if the player can sell the property, boolean
     */
    @Override
    public void handleSellProperty(Player player, Property property, boolean result) {
        if (result){
            System.out.printf("\nPlayer %s sold %s\n", player.getIcon().toUpperCase(), property.getName());
        } else {
            System.out.printf("\nPlayer %s cannot sell %s\n", player.getIcon().toUpperCase(), property.getName());
        }
    }

    /**
     * Displays the status of the current player.
     * @param player player performing actions, Player
     */
    @Override
    public void handleGetPlayerStatus(Player player) {
        System.out.printf("\nDisplaying the status of player: %s\n", player.getIcon().toUpperCase());
        System.out.println(player);
        System.out.println("");
    }

    /**
     * Displays the status of the current board.
     * @param players player performing actions, Player
     */
    @Override
    public void handleGetBoardStatus(List<Player> players) {
        System.out.printf("\nDisplaying the status of the board: \n");

        List<Player> bankruptPlayers = new ArrayList<>();
        List<Player> nonBankruptPlayers = new ArrayList<>();

        for (Player player: players){
            if(player.isBankrupt() == true){
                bankruptPlayers.add(player);
            }
            else if (player.isBankrupt() == false){
                nonBankruptPlayers.add(player);
            }
        }

        System.out.println("\nBankrupt Players:");
        for (Player bankruptPlayer: bankruptPlayers){
            System.out.printf("\tPlayer %s, $%d\n", bankruptPlayer.getIcon().toUpperCase(), bankruptPlayer.getCash());
        }

        System.out.println("\nNon Bankrupt Players:");
        for (Player nonBankruptPlayer: nonBankruptPlayers){
            System.out.printf("\tPlayer %s, $%d\n", nonBankruptPlayer.getIcon().toUpperCase(),
                    nonBankruptPlayer.getCash());
        }

        System.out.printf("\n");
    }

    /**
     * Displays the information of the property the player is currently on.
     * @param property property the player is on, Property
     */
    @Override
    public void handleGetCellStatus(Property property){
        System.out.printf("\nDisplaying the status of the current cell: %s\n", property.getName());
        System.out.println(property);
        System.out.println("");
    }

    /**
     * Displays a prompt whenever a player rolls the same number on both dice.
     * @param player player performing actions, Player
     */
    @Override
    public void handleRollingDoubles(Player player){
        System.out.printf("Player %s rolled a double\n", player.getIcon());
    }

    /**
     * Displays the introduction message.
     */
    @Override
    public void handleWelcomeMonopoly() {
        System.out.println("WELCOME TO MONOPOLY");
        System.out.println("-------------------------------------------------------------");
        System.out.println("In this interactive business game you will try to outwit your\n" +
                "opponents by making them go bankrupt while purchasing properties\n" +
                "around the board. Spend wisely and aim for a TOTAL MONOPOLY.");
        System.out.println("-------------------------------------------------------------");
    }

    /**
     * Displays the status of a player trying to pay rent.
     * @param property property being rented, Property
     * @param renter player paying rent, Player
     * @param result if the player can afford rent, boolean
     */
    @Override
    public void handlePayRent(Property property, Player renter, boolean result) {
        if (result){
            System.out.printf("You have successfully paid %d$ to %s\n",
                    property.getRent(),
                    property.getOwner().getIcon());
        }
        else{
            System.out.printf("You cannot currently pay rent to %s. Rent costs %d$ and you only have %d$ remaining\n",
                    property.getOwner().getIcon(),
                    property.getRent(),
                    renter.getCash());
        }
    }

    /**
     * Displays a breaker to indicate change of turn.
     */
    @Override
    public void handleCurrentPlayerChange() {
        System.out.println("\n+++++++++Passing Dice To Next Player+++++++++");
    }

    /**
     * Displays a message informing that a player chose to forfeit the game.
     * @param player player forfeiting, Player
     */
    @Override
    public void handleForfeitedPlayer(Player player) {
        System.out.printf("Player %s has forfeited the game!\n", player.getIcon().toUpperCase());
    }

    /**
     * Displays the end of game statistics and the ordered rank of all players.
     * @param players list of players in the order they forfeit the game, List<Player>
     */
    @Override
    public void handleWinner(List<Player> players) {
        System.out.println("The game is over");
        Collections.sort(players, Comparator.comparingInt(Player::getRank));

        System.out.printf("Player %s won the game!\n", players.get(0).getIcon().toUpperCase());
        for (Player player: players){
            if (player.isBankrupt()) {
                System.out.printf("Player %s has the rank %d\n", player.getIcon().toUpperCase(), player.getRank());
            }
        }
    }

    /**
     * Start of the program
     * @param args args
     */
    public static void main(String[] args) {
        new BoardListener();
    }
}
