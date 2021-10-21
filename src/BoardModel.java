// Bardia Parmoun & Kyra Lothrop

import java.util.*;

public class BoardModel {
    private List<BoardView> views;
    public enum Status {
        PLAYERWON (""),
        PLAYERLOST ("");

        private String playerName;

        Status(String playerName){
            this.playerName = playerName;
        }

        public void setPlayer(String playerName){
            this.playerName = playerName;
        }

        public String getPlayer(){
            return this.playerName;
        }
    }

    private Status boardStatus;

    public BoardModel(){
        views = new ArrayList<>();
    }

    public void addBoardView (BoardView view){
        views.add(view);
    }

    public void removeBoardView (BoardView view){
        views.remove(view);
    }

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

    }

}
