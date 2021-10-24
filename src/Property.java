// Bardia Parmoun & Kyra Lothrop

public class Property extends BoardCell {

    private int price;
    private int rent;
    private boolean recentlyChanged;

    Property(String name, int price, int rent){
        super(name, null, CellType.PROPERTY);
        this.price = price;
        this.rent = rent;
        this.recentlyChanged = false;
    }

    public int getPrice() {
        return this.price;
    }

    public int getRent(){
        return this.rent;
    }

    public void toggleRecentlyChanged(){
        this.recentlyChanged = !this.recentlyChanged;
    }

    public boolean getRecentlyChanged() {
        return recentlyChanged;
    }

    @Override
    public String toString() {
        String propertyInfo = "\n\t\tname='" + super.getName() + '\'' +
                "\n\t\tprice='" + price + '\'' +
                "\n\t\trent='" + rent + '\'';

        if (super.getOwner() != null){
            propertyInfo += "\n\t\tproperty owner='" + super.getOwner().getIcon() + '\'';
        } else {
            propertyInfo += "\n\t\tproperty owner='no owner'";
        }
        return propertyInfo;
    }
}