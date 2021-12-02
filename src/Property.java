/**
 * Group 3
 * SYSC 3110 - Milestone 3 Property Class
 *
 * This document is the Property. This class extends BoardCell and has a price, rent, and if the property was
 * recently bought/changed.
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 3.0
 *
 */
public class Property extends BoardCell implements Buyable{
    /**
     * Keeps track of the property buying price.
     */
    private final int price;
    /**
     * Keeps track of the all the possible rents for the property.
     */
    private final Integer[] houseRents;
    /**
     * Keeps track of whether a property attribute has been recently changed (Excluding self).
     */
    private boolean recentlyChanged;
    /**
     * Keeps track of which group a property belongs to.
     */
    private final NeighborhoodEnum neighborhood;
    /**
     * Keeps track of the number of houses on a given property
     */
    private int numHouses;
    /**
     * Keeps track of the grouping of properties on the board according to their colours.
     */
    public enum NeighborhoodEnum{
        BROWN(2,50, "#955436"),
        SKY(3,50, "#aae0fa"),
        MAGENTA(3,100, "#d93a96"),
        ORANGE(3,100, "#f7941d"),
        RED(3,150, "#ed1b24"),
        YELLOW(3,150, "#fef200"),
        GREEN(3,200, "#1bb359"),
        INDIGO(2,200, "#0072bb");

        private final int numProperties;
        private final int houseCost;
        private String color;

        NeighborhoodEnum(int numProperties, int houseCost, String color){
            this.numProperties = numProperties;
            this.houseCost = houseCost;
            this.color = color;
        }

        public int getNumProperties(){
            return this.numProperties;
        }

        public int getHouseCost(){return this.houseCost;}

        public String getColor(){return this.color;}
    }

    /**
     * Constructor for the Property, sets all values and an owner for the property.
     * @author Sarah Chow 101143033
     * @author Kyra Lothrop 101145872
     * @author Bardia Parmoun 101143006
     * @author Owen VanDusen 101152022
     * @param name Property name, String
     * @param price Property price, int
     * @param houseRents int[]
     * @param neighborhood NeighborhoodNum
     * @param imgName String
     */
    Property(String name, int price, Integer[] houseRents, NeighborhoodEnum neighborhood, String imgName){
        super(name, null, CellType.PROPERTY, imgName);
        this.price = price;
        this.houseRents = houseRents;
        this.recentlyChanged = false;
        this.neighborhood = neighborhood;
        this.numHouses = 0;
    }

    /**
     * Constructor for the Property, without the imgName
     * @author Sarah Chow 101143033
     * @author Kyra Lothrop 101145872
     * @author Bardia Parmoun 101143006
     * @author Owen VanDusen 101152022
     * @param name Property name, String
     * @param price Property price, int
     * @param houseRents int[]
     * @param neighborhood NeighborhoodNum
     */
    Property(String name, int price, Integer[] houseRents, NeighborhoodEnum neighborhood){
        this(name, price, houseRents, neighborhood, "");
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
        return houseRents[numHouses];
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
    }

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
            propertyInfo += "\n\tproperty owner='" + super.getOwner().getIconName().toUpperCase() + '\'';

        } else {
            propertyInfo += "\n\tproperty owner='no owner'";
        }
        return propertyInfo;
    }
}