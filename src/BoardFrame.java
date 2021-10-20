// Bardia Parmoun & Kyra Lothrop

import java.util.ArrayList;
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
            return false;
        }
    }
}
