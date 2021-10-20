// Bardia Parmoun & Kyra Lothrop

import java.util.ArrayList;
import java.util.List;

public class BoardModel {
    private List<BoardView> views;

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
        return success;
    }

}
