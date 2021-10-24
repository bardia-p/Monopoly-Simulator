// Bardia Parmoun & Kyra Lothrop

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BoardListener implements BoardView {

    BoardModel model;
    BoardController controller;

    public BoardListener(){
        model = new BoardModel();
        model.addBoardView(this);
        controller = new BoardController();
        model.play();
    }

    @Override
    public void handleBoardUpdate(BoardEvent e) {
        switch (e.getType()) {
            case PLAYER_ROLL:
                handleRoll(e.getDice(), e.getPlayer());
                break;
            case PLAYER_MOVE:
                showCurrentCell(e.getPlayer());
                break;
            case BUY:
                handleBuyProperty(e.getPlayer(), (Property) e.getBoardCell(), e.getResult());
                break;
            case SELL:
                handleSellProperty(e.getPlayer(), (Property) e.getBoardCell(), e.getResult());
                break;
            case PLAYER_STATUS:
                handleGetPlayerStatus(e.getPlayer());
                break;
            case BOARD_STATUS:
                handleGetBoardStatus(e.getPlayers());
                break;
            case CELL_STATUS:
                handleGetCellStatus(e.getBoardCell());
                break;
            case PLAYER_DOUBLE_ROLL:
                handleRollingDoubles(e.getPlayer());
                break;
            case INITIALIZE_MONOPOLY:
                handleWelcomeMonopoly();
                break;
            case PAY_FEES:
                handlePayFees(e.getPlayer().getCurrentCell(), e.getPlayer(), e.getValue(), e.getResult());
                break;
            case PASS_TURN:
                handleCurrentPlayerChange();
                break;
            case PLAYER_FORFEIT:
                handleForFeitedPlayer(e.getPlayer());
                break;
            case GAME_OVER:
                handleWinner(e.getPlayers());
                break;
            default:
                controller.eventListener(e);
        }
    }

    @Override
    public void handleRoll(int[] dice, Player player){
        int die1 = dice[0];
        int die2 = dice[1];
        System.out.println("\nRolling dice for: " + player.getIcon().toUpperCase());
        System.out.printf("---> You rolled a %d and a %d\n", die1, die2);
        System.out.printf("---> Total: %d\n\n", die1 + die2);
    }

    @Override
    public void showCurrentCell(Player player){
        System.out.printf("Player %s is currently at: %s\n",
                player.getIcon().toUpperCase(),
                player.getCurrentCell().getName());
    }

    @Override
    public void handleBuyProperty(Player player, Property property, boolean result) {
        if (result){
            System.out.printf("\nPlayer %s bought %s\n", player.getIcon().toUpperCase(), property.getName());
        } else {
            System.out.printf("\nPlayer %s cannot afford %s\n", player.getIcon().toUpperCase(), property.getName());
        }
    }

    @Override
    public void handleSellProperty(Player player, Property property, boolean result) {
        if (result){
            System.out.printf("\nPlayer %s sold %s\n", player.getIcon().toUpperCase(), property.getName());
        } else {
            System.out.printf("\nPlayer %s cannot sell %s\n", player.getIcon().toUpperCase(), property.getName());
        }
    }

    @Override
    public void handleGetPlayerStatus(Player player) {
        System.out.printf("\nDisplaying the status of player: %s\n", player.getIcon().toUpperCase());
        System.out.println(player);
        System.out.println("");
    }

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

    @Override
    public void handleGetCellStatus(BoardCell currentCell){
        System.out.printf("\nDisplaying the status of the current cell: %s\n", currentCell.getName());
        System.out.println(currentCell);
        System.out.println("");
    }

    @Override
    public void handleRollingDoubles(Player player){
        System.out.printf("Player %s rolled a double\n", player.getIcon());
    }

    @Override
    public void handleWelcomeMonopoly() {
        System.out.println("WELCOME TO MONOPOLY");
        System.out.println("-------------------------------------------------------------");
        System.out.println("In this interactive business game you will try to outwit your\n" +
                "opponents by making them go bankrupt while purchasing properties\n" +
                "around the board. Spend wisely and aim for a TOTAL MONOPOLY.");
        System.out.println("-------------------------------------------------------------");
    }

    @Override
    public void handlePayFees(BoardCell boardCell, Player player, int fees,  boolean result) {
        if (result){
            System.out.printf("You have successfully paid %d$ to %s\n",
                    fees,
                    boardCell.getOwner().getIcon());
        }
        else{
            System.out.printf("You cannot currently pay fees to %s. Rent costs %d$ and you only have %d$ remaining\n",
                    boardCell.getOwner().getIcon(),
                    fees,
                    player.getCash());
        }
    }

    @Override
    public void handleCurrentPlayerChange() {
        System.out.println("\n+++++++++Passing Dice To Next Player+++++++++");
    }

    @Override
    public void handleForFeitedPlayer(Player player) {
        System.out.printf("Player %s has forfeited the game!\n", player.getIcon().toUpperCase());
    }

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

    public static void main(String[] args) {
        new BoardListener();
    }
}