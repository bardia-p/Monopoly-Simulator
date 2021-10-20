// Bardia Parmoun & Kyra Lothrop

import java.util.ArrayList;
import java.util.List;

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
    }

    private Status playerStatus;

    public BoardModel(){
        views = new ArrayList<>();
    }

    public void addBoardView (BoardView view){
        views.add(view);
    }

    public void removeBoardView (BoardView view){
        views.remove(view);
    }

    public boolean buyProperty (String propertyName, String playerName){
        boolean success = true;
        for (BoardView view: views){
            success &= view.handleBuyProperty(propertyName, playerName);
        }
        return success;
    }

    public boolean payRent (String propertyName, String playerName){
        boolean success = true;
        for (BoardView view: views){
            success &= view.handlePayRent(propertyName, playerName);
        }

        if (!success){
            this.updateStatus(playerName, false);
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

    public Status getStatus() {return playerStatus;}

    private void updateStatus(String playerName, boolean status) {
        if (status){
            playerStatus.PLAYERWON.setPlayer(playerName);
        } else {
            playerStatus.PLAYERLOST.setPlayer(playerName);
        }
    }
}
