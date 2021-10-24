public class Go extends BoardCell {
    private int reward;

    public Go(int fee){
        super("GO", null, CellType.GO);
        this.reward = reward;
    }

    public int getReward(){
        return reward;
    }

    @Override
    public String toString() {
        return "\n\t\tname='" + super.getName() + '\'';
    }
}
