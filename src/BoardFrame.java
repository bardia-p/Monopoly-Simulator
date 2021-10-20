// Bardia Parmoun & Kyra Lothrop

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoardFrame implements BoardView {
    List<Property> properties;
    List<Player> players;
    BoardModel model;

    public BoardFrame(){
        properties = new ArrayList<>();
        players = new ArrayList<>();
        model = new BoardModel();
        model.addBoardView(this);
        constructBoard();
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
    }

    private Property getPropertyByName(String propertyName){
        for (Property property: properties){
            if (property.getName().equals(propertyName)){
                return property;
            }
        }

        return null;
    }

    private Player getPlayerByName(String playerName){
        for (Player player: players){
            if (player.getName().equals(playerName)){
                return player;
            }
        }

        return null;
    }

    @Override
    public boolean handleBuyProperty(String propertyName, String playerName) {
        Property property = getPropertyByName(propertyName);
        Player player = getPlayerByName(playerName);

        if (property.getOwner() == null && property.getPrice() <= player.getCash()) {
            property.setOwner(player);
            player.pay(property.getPrice());
        }
        else{
            return false;
        }

        return true;
    }

    @Override
    public boolean handlePayRent(String propertyName, String playerName) {
        Property property = getPropertyByName(propertyName);
        Player propertyOwner = property.getOwner();
        Player playerPaysRent = getPlayerByName(playerName);
        int amountToPay = property.getRent();

        if (property.getOwner() != null && amountToPay <= playerPaysRent.getCash()) {
            propertyOwner.getMoney(amountToPay);
            playerPaysRent.pay(amountToPay);
            return true;
        }
        else if(amountToPay >= playerPaysRent.getCash()){
            playerPaysRent.setBankrupt(true);
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public boolean handleNewPlayer(String playerName, String playerIcon) {
        this.players.add(new Player(playerName, playerIcon));
        return true;
    }
}
