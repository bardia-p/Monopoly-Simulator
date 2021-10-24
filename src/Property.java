/**
 * SYSC 3110 - Milestone 1 Property Class
 *
 * This document is the Property. This class has the name, property owner, price, rent
 * property index, if the property is buyable, and if the property was recently bought/changed.
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 1.0
 *
 */

public class Property {
    /**
     * Keeps track of the property name.
     */
    private String name;
    /**
     * Keeps track of the property owner.
     */
    private Player propertyOwner;
    /**
     * Keeps track of the property buying price.
     */
    private int price;
    /**
     * Keeps track of the property rent.
     */
    private int rent;
    /**
     * Keeps track of the property index on the board.
     */
    private int propertyIndex;
    /**
     * Keeps track of whether a property is buyable.
     */
    private boolean buyable;
    /**
     * Keeps track of whether a property attribute has been recently changed (Excluding self).
     */
    private boolean recentlyChanged;

    /**
     * Constructor for the Property, sets all values and an owner for the property.
     * @author Sarah Chow 101143033
     * @author Kyra Lothrop 101145872
     * @author Bardia Parmoun 101143006
     * @author Owen VanDusen 101152022
     * @param name Property name, String
     * @param price Property price, int
     * @param rent Property rent, int
     * @param buyable If the property can be bought, boolean
     * @param propertyOwner Player who owns the property, Player
     */
    Property(String name, int price, int rent, boolean buyable, Player propertyOwner){
        this(name, price, rent, buyable);
        this.propertyOwner = propertyOwner;
    }

    /**
     * Constructor for the Property, sets all values.
     * @author Sarah Chow 101143033
     * @author Kyra Lothrop 101145872
     * @author Bardia Parmoun 101143006
     * @author Owen VanDusen 101152022
     * @param name Property name, String.
     * @param price Property price, int.
     * @param rent Property rent, int.
     * @param buyable If the property can be bought, boolean
     */
    Property(String name, int price, int rent, boolean buyable){
        this.name = name;
        this.price = price;
        this.rent = rent;
        this.buyable = buyable;
        this.propertyOwner = null;
        this.recentlyChanged = false;
    }

    /**
     * Accessor to set the index of the property on the board.
     * @author Kyra Lothrop 101145872
     * @param propertyIndex the position of the property, int
     */
    public void setPropertyIndex(int propertyIndex) {
        this.propertyIndex = propertyIndex;
    }

    /**
     * Accessor to get the owner of the property.
     * @author Owen VanDusen 101152022
     * @return the owner object, Player
     */
    public Player getOwner() {
        return this.propertyOwner;
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
     * Accessor to get the name of the property.
     * @author Sarah Chow 101143033
     * @return name of the property, String
     */
    public String getName(){
        return name;
    }

    /**
     * Accessor to set the owner of a property to a specific player
     * @author Sarah Chow 101143033
     * @param player the player that owns the property, Player
     */
    public void setOwner(Player player) {
        this.propertyOwner = player;
    }

    /**
     * Accessor to get the rent of the property.
     * @author Bardia Parmoun 101143006
     * @return value of the rent, int
     */
    public int getRent(){
        return this.rent;
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
     * Accessor method to package relevant information into a string.
     * @author Sarah Chow 101143033
     * @return the property's current information, String
     */
    @Override
    public String toString() {
        String propertyInfo = "\n\t\tname='" + name + '\'' +
                "\n\t\tprice='" + price + '\'' +
                "\n\t\trent='" + rent + '\'' +
                "\n\t\tproperty index='" + propertyIndex + '\'';

        if (propertyOwner != null){
            propertyInfo += "\n\t\tproperty owner='" + propertyOwner.getIcon() + '\'';
        } else {
            propertyInfo += "\n\t\tproperty owner='no owner'";
        }
        return propertyInfo;
    }
}