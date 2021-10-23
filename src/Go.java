public class Go extends BoardCell {
    private int reward;

    public Go(int fee){
        super("GO", null, CellType.INCOME_TAX);
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
