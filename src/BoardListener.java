// Bardia Parmoun & Kyra Lothrop

import java.sql.SQLOutput;

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
        controller.eventListener(e);
    }

    @Override
    public void handleRoll(int die1, int die2, Player player){
        System.out.println("\nRolling dice for: " + player.getIcon().toUpperCase());
        System.out.printf("---> You rolled a %d and a %d\n", die1, die2);
        System.out.printf("---> Total: %d\n\n", die1 + die2);
    }

    @Override
    public void showCurrentProperty(Player player){
        System.out.printf("Player %s is currently at: %s\n",
                player.getIcon().toUpperCase(),
                player.getCurrentProperty().getName());
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

    @Override
    public void handleCurrentPlayerChange() {
        System.out.println("\n+++++++++Passing Dice To Next Player+++++++++");
    }

    public static void main(String[] args) {
        new BoardListener();
    }
}
