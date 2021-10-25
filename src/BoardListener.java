
import java.util.ArrayList;
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
     * @author Sarah Chow 101143033
     * @author Kyra Lothrop 101145872
     * @author Bardia Parmoun 101143006
     * @author Owen VanDusen 10115202
     */
    public BoardListener(){
        model = new BoardModel();
        model.addBoardView(this);
        controller = new BoardController();
        model.play();
    }

    /**
     * Passes a BoardEvent to the BoardController to be interpreted.
     * @author Bardia Parmoun 101143006
     * @param e the board event, BoardEvent
     */
    @Override
    public void handleBoardUpdate(BoardEvent e) {
        switch (e.getType()) {
            case PLAYER_ROLL -> handleRoll(e.getDice(), e.getPlayer());
            case PLAYER_MOVE -> showCurrentCell(e.getPlayer());
            case BUY -> handleBuyProperty(e.getPlayer(), (Property) e.getBoardCell(), e.getResult());
            case SELL -> handleSellProperty(e.getPlayer(), (Property) e.getBoardCell(), e.getResult());
            case PLAYER_STATUS -> handleGetPlayerStatus(e.getPlayer());
            case BOARD_STATUS -> handleGetBoardStatus(e.getPlayers());
            case CELL_STATUS -> handleGetCellStatus(e.getPlayer().getCurrentCell());
            case PLAYER_DOUBLE_ROLL -> handleRollingDoubles(e.getPlayer());
            case INITIALIZE_MONOPOLY -> handleWelcomeMonopoly();
            case PAY_FEES -> handlePayFees(e.getPlayer().getCurrentCell(), e.getPlayer(), e.getValue(), e.getResult());
            case PASS_TURN -> handleCurrentPlayerChange();
            case PLAYER_FORFEIT -> handleForfeitedPlayer(e.getPlayer());
            case GAME_OVER -> handleWinner(e.getPlayers());
            default -> controller.eventListener(e);
        }
    }

    /**
     * Passes the values of both dice and the player to the BoardController to be interpreted.
     * @author Sarah Chow 101143033
     * @param dice value of the dice, int[]
     * @param player player performing actions, Player
     */
    private void handleRoll(int[] dice, Player player) {
        int die1 = dice[0];
        int die2 = dice[1];
        System.out.println("\nRolling dice for: " + player.getIcon().toUpperCase());
        System.out.printf("---> You rolled a %d and a %d\n", die1, die2);
        System.out.printf("---> Total: %d\n\n", die1 + die2);
    }

    /**
     * Displays the name of the property the current player is standing on in the terminal.
     * @author Sarah Chow 101143033
     * @param player player performing actions, Player
     */
    private void showCurrentCell(Player player){
        System.out.printf("Player %s is currently at: %s\n",
                player.getIcon().toUpperCase(),
                player.getCurrentCell().getName());
    }

    /**
     * Displays whether the current player can afford the property they attempted to buy or not.
     * @author Owen VanDusen 101152022
     * @param player player performing actions, Player
     * @param property property that is in contention for purchasing, Property
     * @param result if the player can afford the property, boolean
     */
    private void handleBuyProperty(Player player, Property property, boolean result) {
        if (result){
            System.out.printf("\nPlayer %s bought %s\n", player.getIcon().toUpperCase(), property.getName());
        } else {
            System.out.printf("\nPlayer %s cannot afford %s\n", player.getIcon().toUpperCase(), property.getName());
        }
    }

    /**
     * Displays whether the current player can sell the property they attempted to sell.
     * @author Kyra Lothrop 101145872
     * @param player player performing actions, Player
     * @param property property that is in contention for selling, Property
     * @param result if the player can sell the property, boolean
     */
    private void handleSellProperty(Player player, Property property, boolean result) {
         if (result){
            System.out.printf("\nPlayer %s sold %s\n", player.getIcon().toUpperCase(), property.getName());
        } else {
            System.out.printf("\nPlayer %s cannot sell %s\n", player.getIcon().toUpperCase(), property.getName());
        }
    }

    /**
     * Displays the status of the current player.
     * @author Owen VanDusen 101152022
     * @param player player performing actions, Player
     */
    private void handleGetPlayerStatus(Player player) {
        System.out.printf("\nDisplaying the status of player: %s\n", player.getIcon().toUpperCase());
        System.out.println(player + "\n");
    }

    /**
     * Displays the status of the current board.
     * @author Kyra Lothrop 101145872
     * @param players player performing actions, Player
     */
    private void handleGetBoardStatus(List<Player> players) {
        System.out.println("\nDisplaying the status of the board:");

        List<Player> bankruptPlayers = new ArrayList<>();
        List<Player> nonBankruptPlayers = new ArrayList<>();

        for (Player player: players){
            if(player.isBankrupt()){
                bankruptPlayers.add(player);
            }
            else if (!player.isBankrupt()){
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
        System.out.println("\n");
    }

    /**
     * Displays the information of the property the player is currently on.
     * @author Bardia Parmoun 101143006
     * @param currentCell property the player is on, BoardCell
     */
    private void handleGetCellStatus(BoardCell currentCell){
        System.out.printf("\nDisplaying the status of the current cell: %s\n", currentCell.getName());
        System.out.println(currentCell + "\n");
    }

    /**
     * Displays a prompt whenever a player rolls the same number on both dice.
     * @author Bardia Parmoun 101143006
     * @param player player performing actions, Player
     */
    private void handleRollingDoubles(Player player){
        System.out.printf("Player %s rolled a double\n", player.getIcon());
    }

    /**
     * Displays the introduction message.
     * @author Sarah Chow 101143033
     */
    private void handleWelcomeMonopoly() {
        System.out.println("WELCOME TO MONOPOLY");
        System.out.println("-------------------------------------------------------------");
        System.out.println("In this interactive business game you will try to outwit your\n" +
                "opponents by making them go bankrupt while purchasing properties\n" +
                "around the board. Spend wisely and aim for a TOTAL MONOPOLY.");
        System.out.println("-------------------------------------------------------------");
    }

    /**
     * Displays the status of a player trying to pay their fees.
     * @author Kyra Lothrop 101145872
     * @param boardCell property being rented, BoardCell
     * @param player player paying rent, Player
     * @param result if the player can afford rent, boolean
     */
    private void handlePayFees(BoardCell boardCell, Player player, int fees,  boolean result) {
        if (result){
            System.out.printf("You have successfully paid %d$ to %s\n",
                    fees,
                    boardCell.getOwner().getIcon().toUpperCase());
        }
        else{
            System.out.printf("You cannot currently pay fees to %s. You must pay %d$ and you only have %d$ remaining\n",
                    boardCell.getOwner().getIcon(),
                    fees,
                    player.getCash());
        }
    }

    /**
     * Displays a breaker to indicate change of turn.
     * @author Owen VanDusen 101152022
     */
    private void handleCurrentPlayerChange() {
        System.out.println("\n+++++++++Passing Dice To Next Player+++++++++");
    }

    /**
     * Displays a message informing that a player chose to forfeit the game.
     * @author Sarah Chow 101143033
     * @param player player forfeiting, Player
     */
    private void handleForfeitedPlayer(Player player) {
        System.out.printf("Player %s has forfeited the game!\n", player.getIcon().toUpperCase());
    }

    /**
     * Displays the end of game statistics and the ordered rank of all players.
     * @author Bardia Parmoun 101143006
     * @param players list of players in the order they forfeit the game, List<Player>
     */
    private void handleWinner(List<Player> players) {
        System.out.println("-------------------------------------------------------------");
        System.out.println("GAME OVER");
        players.sort(Comparator.comparingInt(Player::getRank));

        System.out.printf("Player %s won the game!\n", players.get(0).getIcon().toUpperCase());
        for (Player player: players){
            if (player.isBankrupt()) {
                System.out.printf("Player %s has the rank %d\n", player.getIcon().toUpperCase(), player.getRank());
            }
        }
    }

    /**
     * Start of the program
     * @author Kyra Lothrop 101145872
     * @param args args
     */
    public static void main(String[] args) {
        new BoardListener();
    }
}