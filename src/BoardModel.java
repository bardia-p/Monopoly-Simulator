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

    public void buyProperty (Property property, Player player){
        if (property.getOwner() == null && property.getPrice() >= player.getCash()) {
            for (BoardView view: views){
                view.handleBuyProperty(property, player);
            }
        }
    }

}
