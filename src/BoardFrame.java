// Bardia Parmoun & Kyra Lothrop

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

    public static void main(String[] args) {
        new BoardFrame();
    }
}
