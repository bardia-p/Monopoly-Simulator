// Bardia Parmoun & Kyra Lothrop

public class Property {
    private String name;
    private Player propertyOwner;
    private int price;
    private int rent;
    private int propertyIndex;
    private boolean buyable;

    Property(String name, int price, int rent, boolean buyable){
        this.name = name;
        this.price = price;
        this.propertyOwner = null;
        this.rent = 100;
        this.buyable = buyable;
    }

    Property(String name, int price, int rent){
        this.name = name;
        this.price = price;
        this.propertyOwner = null;
        this.rent = 100;
        this.buyable = true;
    }

    public void setPropertyIndex(int propertyIndex) {
        this.propertyIndex = propertyIndex;
    }

    public Player getOwner() {
        return this.propertyOwner;
    }

    public int getPrice() {
        return this.price;
    }

    public String getName(){
        return name;
    }

    public void setOwner(Player player) {
        this.propertyOwner = player;
    }

    public int getRent(){
        return this.rent;
    }

    public int getPropertyIndex() {
        return propertyIndex;
    }

    @Override
    public String toString() {
        return "Property{" +
                "name='" + name + '\'' +
                ", propertyOwner=" + propertyOwner.getIcon() +
                ", price=" + price +
                ", rent=" + rent +
                ", propertyIndex=" + propertyIndex +
                '}';
    }
}