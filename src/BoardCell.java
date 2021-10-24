public abstract class BoardCell {
    private String name;
    private Player owner;
    private CellType type;

    public enum CellType {PROPERTY, GO, FREE_PARKING, JAIL, TAX}


    public BoardCell(String name, Player owner, CellType type){
        this.name = name;
        this.owner = owner;
        this.type = type;
    }

    public Player getOwner() {
        return this.owner;
    }

    public String getName(){
        return name;
    }

    public void setOwner(Player player) {
        this.owner = player;
    }

    public abstract String toString();

    public CellType getType(){
        return type;
    }
}
