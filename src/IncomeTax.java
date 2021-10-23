public class IncomeTax extends BoardCell{
    private int tax;

    IncomeTax (String name, int tax, Player owner){
        super(name, owner, CellType.INCOME_TAX);
        this.tax = tax;
    }

    public int getTax(){
        return tax;
    }

    @Override
    public String toString() {
        String cellInfo = "\n\t\tname='" + super.getName() + '\'' +
                "\n\t\ttax='" + tax + '\'';
        return null;
    }
}
