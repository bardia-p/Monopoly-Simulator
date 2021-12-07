/**
 * Group 3
 * SYSC 3110 - Milestone 3 Railroad Class
 *
 * This document is the Railroad. This class extends BoardCell and implements Buyable.
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 1.0
 */

public class Railroad extends BoardCell implements Buyable{

    /**
     * Keeps track of the railroad buying price.
     */
    private final int price;
    /**
     * Keeps track of whether a railroad attribute has been recently changed (Excluding self).
     */
    private boolean recentlyChanged;
    /**
     * Keeps track of the rent value depending on the number of railroads owned
     */
    private final Integer[] rentValues;

    /**
     * Constructor for the Railroad
     * @author Kyra Lothrop 101145872
     * @param name Railroad name, String
     * @param price Railroad price, int
     * @param imgName Railroad image, String
     */
    public Railroad(String name, int price, Integer[] rentValues, String imgName) {
        super(name, null, CellType.RAILROAD, imgName);
        this.price = price;
        this.recentlyChanged = false;
        this.rentValues = rentValues;
    }

    /**
     * Constructor for the Railroad without imgName.
     * @author Kyra Lothrop 101145872
     * @param name Railroad name, String
     * @param price Railroad price, int
     */
    public Railroad(String name, int price, Integer[] rentValues) {
        this(name, price, rentValues, "");
    }

    /**
     * Accessor to calculate rent.
     * @author Kyra Lothrop
     * @param numRailroads number of railroads the player owns, int
     * @return the rent for the railroad
     */
    public int getRent(int numRailroads) {
        return rentValues[numRailroads-1];
    }

    /**
     * Accessor for the price of the railroad.
     * @author Kyra Lothrop 101145872
     * @return price of the railroad
     */
    public int getPrice() {
        return this.price;
    }

    /**
     * Accessor to toggle the value of recentlyChanged.
     * @author Kyra Lothrop 101145872
     */
    public void toggleRecentlyChanged(){
        this.recentlyChanged = !this.recentlyChanged;
    }

    /**
     * Accessor to get recentlyChanged attribute.
     * @author Kyra Lothrop 101145872
     * @return if the railroad has recently been changed, boolean
     */
    public boolean getRecentlyChanged() {
        return recentlyChanged;
    }

    /**
     * Accessor method to package relevant information into a string.
     * @author Kyra Lothrop 101145872
     * @return the railroad's current information, String
     */
    @Override
    public String toString() {
        String railroadInfo = "\n\tname='" + super.getName() + '\'' +
                "\n\tprice='" + price + '\'' +
                "\n\trent='" + rentValues[0] + ", if the owner owns one railroad." +
                "\n\t\t'" + rentValues[1] + ", if the owner owns two railroads." +
                "\n\t\t'" + rentValues[2] + ", if the owner owns three railroads." +
                "\n\t\t'" + rentValues[3] + ", if the owner owns four railroads." + '\'';

        if (super.getOwner() != null){
            railroadInfo += "\n\tproperty owner='" + super.getOwner().getIconName().toUpperCase() + '\'';

        } else {
            railroadInfo += "\n\tproperty owner='no owner'";
        }
        return railroadInfo;
    }
}
