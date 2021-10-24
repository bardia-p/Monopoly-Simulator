public class Tax extends BoardCell{
    private int tax;

    Tax(String name, int tax, Player owner){
        super(name, owner, CellType.TAX);
        this.tax = tax;
    }

    public int getTax(){
        return tax;
    }

    @Override
    public String toString() {
        String cellInfo = "\n\t\tname='" + super.getName() + '\'' +
                "\n\t\ttax='" + tax + '\'';
        return cellInfo;
    }
}
