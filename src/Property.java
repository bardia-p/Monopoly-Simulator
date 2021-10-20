// Bardia Parmoun & Kyra Lothrop

public class Property {
    private String name;
    private Player propertyOwner;
    private int price;

    Property(){
        this.propertyOwner = null;
        this.price = 0;
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
}