import java.util.ArrayList;
import java.util.EventObject;

public class BoardEvent extends EventObject {
    private BoardModel.Status type;
    private Player player;
    private int value;
    private ArrayList<BoardModel.Command> commands;


    public BoardEvent(BoardModel model, BoardModel.Status type, Player player, ArrayList<BoardModel.Command> commands){
        this(model, type);
        this.player = player;
        this.commands = commands;
    }

    public BoardEvent(BoardModel model, BoardModel.Status type, int value){
        this(model, type);
        this.value = value;
    }

    public BoardEvent(BoardModel model, BoardModel.Status type){
        super(model);
        this.type = type;
    }

    public BoardModel.Status getType(){
        return type;
    }


    public Player getPlayer() {
        return player;
    }

    public ArrayList<BoardModel.Command> getCommands(){
        return commands;
    }

    public int getValue(){
        return value;
    }
}
