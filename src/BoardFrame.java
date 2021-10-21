// Bardia Parmoun & Kyra Lothrop

import javax.sound.midi.Soundbank;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class BoardFrame implements BoardView {

    BoardModel model;
    BoardController controller;

    public BoardFrame(){
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
        System.out.println("Rolling dice for " + player.getIcon());
        System.out.printf("You rolled a %d and a %d\n", die1, die2);
    }

    @Override
    public void showCurrentProperty(Player player){
        System.out.printf("Player %s is currently at %s\n", player.getIcon(), player.getCurrentProperty().getName());
    }

    @Override
    public void handleBuyProperty(Player player, Property property, boolean result) {
        if (result){
            System.out.printf("Player %s bought %s\n", player.getIcon(), property.getName());
        } else {
            System.out.printf("Player %s cannot afford %s\n", player.getIcon(), property.getName());
        }
    }

    @Override
    public void handleSellProperty(Player player, Property property, boolean result) {
        if (result){
            System.out.printf("Player %s sold %s\n", player.getIcon(), property.getName());
        } else {
            System.out.printf("Player %s cannot sell %s\n", player.getIcon(), property.getName());
        }
    }

    @Override
    public void handleGetPlayerStatus(Player player) {
        System.out.printf("Displaying the status of player: %s\n", player.getIcon());
        System.out.println(player);
    }

    public static void main(String[] args) {
        new BoardFrame();
    }
}
