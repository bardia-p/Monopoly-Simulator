// Bardia Parmoun & Kyra Lothrop

public class Property {
    private String name;
    private Player propertyOwner;
    private int price;
    private int rent;

    Property(String name){
        this.name = name;
        this.propertyOwner = null;
        this.price = 0;
        this.rent = 100;
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
}