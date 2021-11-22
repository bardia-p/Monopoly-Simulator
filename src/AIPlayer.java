import java.util.List;

public class AIPlayer extends Player {
    BoardModel model;
    AIPlayer(BoardModel model, String name, BoardModel.Icon icon) {
        super(name, icon, true);
        this.model = model;
    }

    public void nextMove(){
        BoardCell currentCell = getCurrentCell();
        List<BoardModel.Command> availableCommands =  model.getCommand(this);

        boolean turnOver = false;

        while (!turnOver){
            if (availableCommands.contains(BoardModel.Command.BUY)){
                model.buyLocation(currentCell, this);
            } else if (availableCommands.contains(BoardModel.Command.PAY_FEES)){
                if (!model.payFees(currentCell, this)){
                    if (availableCommands.contains(BoardModel.Command.SELL)){
                        BoardCell sellableCell = this.getOwnedLocations(true).get(0);
                        model.sellProperty(this, sellableCell);
                    } else {
                        model.forfeit(this);
                    }
                }
            } else if (availableCommands.contains(BoardModel.Command.ROLL_AGAIN)){
                model.roll(this);
            } else {
                model.passTurn(this);
                turnOver = true;
            }
        }
    }
}
