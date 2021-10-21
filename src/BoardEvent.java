import java.util.EventObject;

public class BoardEvent extends EventObject {
    private BoardModel.Status type;
    private Player player;
    public int amount;

    public BoardEvent(BoardModel model, BoardModel.Status type, Player player, int amount){
        this(model, type, player);
        this.amount = amount;
    }

    public BoardEvent(BoardModel model, BoardModel.Status type, int amount){
        this(model, type);
        this.amount = amount;
    }

    public BoardEvent(BoardModel model, BoardModel.Status type, Player player){
        this(model, type);
        this.player = player;
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

    public int getAmount(){
        return amount;
    }
}
