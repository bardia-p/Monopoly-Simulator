import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Group 3
 * SYSC 3110 - Milestone 2 Property Class
 *
 * This document is the Property. This class extends BoardCell and has a price, rent, and if the property was
 * recently bought/changed.
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 1.0
 *
 */
public class Property extends BoardCell {
    /**
     * Keeps track of the property buying price.
     */
    private final int price;
    /**
     * Keeps track of the property rent.
     */
    private final int rent;
    /**
     * Rent if the player has one house on the property
     */
    private final int oneHouseRent;
    /**
     * Rent if the player has two houses on the property
     */
    private final int twoHouseRent;
    /**
     * Rent if the player has three houses on the property
     */
    private final int threeHouseRent;
    /**
     * Rent if the player has four houses on the property
     */
    private final int fourHouseRent;
    /**
     * Rent if the player has 5 houses(a hotel) on the property
     */
    private final int hotel;
    /**
     * Keeps track of whether a property attribute has been recently changed (Excluding self).
     */
    private boolean recentlyChanged;
    /**
     * Keeps track of which group a property belongs to.
     */
    private NeighborhoodEnum neighborhood;
    /**
     * Keeps track of the number of houses on a given property
     */
    private int numHouses;
    /**
     * Keeps track of the grouping of properties on the board according to their colours.
     */
    public enum NeighborhoodEnum{
        BROWN(2,50),
        SKY(3,50),
        MAGENTA(3,100),
        ORANGE(3,100),
        RED(3,150),
        YELLOW(3,150),
        GREEN(3,200),
        INDIGO(2,200);

        private final int numProperties;
        private final int houseCost;

        NeighborhoodEnum(int numProperties, int houseCost){
            this.numProperties = numProperties;
            this.houseCost = houseCost;
        }

        public int getNumProperties(){
            return this.numProperties;
        }

        public int getHouseCost(){return this.houseCost;}
    }

    /**
     * Constructor for the Property, sets all values and an owner for the property.
     * @author Sarah Chow 101143033
     * @author Kyra Lothrop 101145872
     * @author Bardia Parmoun 101143006
     * @author Owen VanDusen 101152022
     * @param name Property name, String
     * @param price Property price, int
     * @param rent Property rent, int
     */
    Property(String name, int price, int rent, int oneHouseRent, int twoHouseRent, int threeHouseRent,
             int fourHouseRent, int hotel, NeighborhoodEnum neighborhood, String imgName){
        super(name, null, CellType.PROPERTY, imgName);
        this.price = price;
        this.rent = rent;
        this.oneHouseRent = oneHouseRent;
        this.twoHouseRent = twoHouseRent;
        this.threeHouseRent = threeHouseRent;
        this.fourHouseRent = fourHouseRent;
        this.hotel = hotel;
        this.recentlyChanged = false;
        this.neighborhood = neighborhood;
        this.numHouses = 0;
    }

    /**
     * Accessor to get the price of the property.
     * @author Owen VanDusen 101152022
     * @return price of the property, int
     */
    public int getPrice() {
        return this.price;
    }

    /**
     * Accessor to get the rent of the property.
     * @author Bardia Parmoun 101143006
     * @return value of the rent, int
     */
    public int getRent(){
        return switch(numHouses){
            case 1 -> oneHouseRent;
            case 2 -> twoHouseRent;
            case 3 -> threeHouseRent;
            case 4 -> fourHouseRent;
            case 5 -> hotel;
            default -> rent;
        };
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
     * @author Bardia Parmoun 101143006
     * @return if the property has recently been changed, boolean
     */
    public boolean getRecentlyChanged() {
        return recentlyChanged;
    }

    /**
     * Accessor to the neighborhood of the property
     * @author Owen VanDusen 101152022
     * @return the neighborhood of the property, NeighborhoodEnum
     */
    public NeighborhoodEnum getNeighborhood(){
        return this.neighborhood;
    };

    public boolean addHouse(){
        if(numHouses == 5){
            return false;
        }
        this.numHouses += 1;
        return true;
    }

    public int getNumHouses(){
        return numHouses;
    }

    /**
     * Accessor method to package relevant information into a string.
     * @author Sarah Chow 101143033
     * @return the property's current information, String
     */
    @Override
    public String toString() {
        String propertyInfo = "\n\tname='" + super.getName() + '\'' +
                "\n\tprice='" + price + '\'' +
                "\n\trent='" + getRent() + '\'';

        if (super.getOwner() != null){
            propertyInfo += "\n\tproperty owner='" + super.getOwner().getIconName() + '\'';

        } else {
            propertyInfo += "\n\tproperty owner='no owner'";
        }
        return propertyInfo;
    }
}