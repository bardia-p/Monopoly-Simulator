import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Group 3
 * SYSC 3110 - Milestone 1 Property Class
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
     */
    Property(String name, int price, int rent, String imgName){
        super(name, null, CellType.PROPERTY, imgName);
        this.price = price;
        this.rent = rent;
        this.recentlyChanged = false;
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
     * Accessor method to package relevant information into a linked hash map.
     * @author Sarah Chow 101143033
     * @return attributes of the property, Map
     */
    public Map<String, String> getAttributes() {

        Map<String, String> attributes = new LinkedHashMap<String, String>();

        String propertyOwner = "Not Owned";

        if (super.getOwner() != null){
            propertyOwner = super.getOwner().getIconName();
        }

        attributes.put("Name: ", String.valueOf(super.getName()));
        attributes.put("Price: ", String.valueOf(price));
        attributes.put("Rent: ", String.valueOf(rent));
        attributes.put("Property Owner: ", propertyOwner.toUpperCase());

        return attributes;
    }
}