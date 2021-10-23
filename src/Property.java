// Bardia Parmoun & Kyra Lothrop

public class Property {
    private String name;
    private Player propertyOwner;
    private int price;
    private int rent;
    private int propertyIndex;
    private boolean buyable;
    private boolean recentlyChanged;

    Property(String name, int price, int rent, boolean buyable, Player propertyOwner){
        this(name, price, rent, buyable);
        this.propertyOwner = propertyOwner;
    }

    Property(String name, int price, int rent, boolean buyable){
        this.name = name;
        this.price = price;
        this.rent = 200;
        this.buyable = buyable;
        this.propertyOwner = null;
        this.recentlyChanged = false;
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

    public boolean isBuyable() {
        return buyable;
    }

    public void toggleRecentlyChanged(){
        this.recentlyChanged = !this.recentlyChanged;
    }

    public boolean getRecentlyChanged() {
        return recentlyChanged;
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