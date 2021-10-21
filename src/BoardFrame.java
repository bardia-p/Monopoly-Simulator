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

        for (int i = 0; i < properties.size(); i++){
            properties.get(i).setPropertyIndex(i);
        }
    }

    private Property getPropertyByIndex(int propertyIndex){
        for (Property property: properties){
            if (property.getPropertyIndex() == (propertyIndex)){
                return property;
            }
        }

        return null;
    }

    private Player getPlayerByIcon(String playerIcon){
        for (Player player: players){
            if (player.getIcon().equals(playerIcon)){
                return player;
            }
        }

        return null;
    }

    @Override
    public boolean handleBuyProperty(int propertyIndex, String playerIcon) {
        Property property = getPropertyByIndex(propertyIndex);
        Player player = getPlayerByIcon(playerIcon);

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
    public boolean handlePayRent(int propertyIndex, String playerIcon) {
        Property property = getPropertyByIndex(propertyIndex);
        Player propertyOwner = property.getOwner();
        Player playerPaysRent = getPlayerByIcon(playerIcon);
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
//
//    @Override
//    public boolean handlePropertyBoughtCheck(int propertyIndex) {
//        if (properties.get(propertyIndex).getOwner() != null){
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public String handleGetPropertyName(int propertyIndex) {
//        String propertyName = "";
//        for (Property property: properties){
//            if (property.getPropertyIndex() == (propertyIndex)){
//                propertyName = property.getName();
//            }
//        }
//        return propertyName;
//    }
//
//    @Override
//    public int handleGetPropertyRent(int propertyIndex) {
//        int propertyRent = 0;
//        for (Property property: properties){
//            if (property.getPropertyIndex() == (propertyIndex)){
//                propertyRent = property.getRent();
//            }
//        }
//        return propertyRent;
//    }

    @Override
    public List<String> handleGetPlayerStatus(String playerIcon) {
        List<String> playerStatusList = new ArrayList<>();
        for (Player player: players){
            if (player.getIcon().equals(playerIcon)){
                playerStatusList = Arrays.asList(
                        player.getName(),
                        player.getIcon(),
                        String.valueOf(player.getCash())
                );
            }
        }
        return playerStatusList;
    }

    @Override
    public List<String> handleGetPropertyStatus(int propertyIndex) {
        List<String> propertyStatusList = new ArrayList<>();
        for (Property property: properties){
            if (property.getPropertyIndex() == propertyIndex){
                propertyStatusList.add(property.getName());
                System.out.println(propertyStatusList.get(0)); // Something is wrong

                if (property.getOwner() == null){
                    propertyStatusList.add(null);
                }
                else{
                    property.getOwner().getName();
                }

                propertyStatusList = Arrays.asList(
                        String.valueOf(property.getPrice()),
                        String.valueOf(property.getRent())
                );
            }
        }
        return propertyStatusList;
    }




}
