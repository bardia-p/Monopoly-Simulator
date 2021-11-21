/**
 * Group 3
 * SYSC 3110 - Milestone 3 Utility Class
 *
 * This document is the Utility. This class extends BoardCell and implements Buyable.
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 1.0
 */

public class Utility extends BoardCell implements Buyable{
    /**
     * Keeps track of the utility buying price.
     */
    private final int price;
    /**
     * Keeps track of whether a utility attribute has been recently changed (Excluding self).
     */
    private boolean recentlyChanged;
    /**
     * Keeps track of the rent value depending on the number of utilities owned
     */
    private final Integer[] rentValues;

    /**
     * Constructor for the Utility
     * @author Kyra Lothrop 101145872
     * @param name Utility name, String
     * @param price Utility price, int
     * @param imgName Utility image, String
     */
    public Utility(String name, int price, Integer[] rentValues, String imgName) {
        super(name, null, CellType.UTILITY, imgName);
        this.price = price;
        this.recentlyChanged = false;
        this.rentValues = rentValues;
    }

    /**
     * Accessor to calculate rent.
     * @author Kyra Lothrop
     * @param numUtilities number of utilities the player owns, int
     * @return the rent for the utility
     */
    public int getRent(int numUtilities, int[] dice) {
        return rentValues[numUtilities-1] * (dice[0]+ dice[1]);
    }

    /**
     * Accessor for the price of the utility.
     * @author Kyra Lothrop 101145872
     * @return price of the utility
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
     * @return if the utility has recently been changed, boolean
     */
    public boolean getRecentlyChanged() {
        return recentlyChanged;
    }

    /**
     * Accessor method to package relevant information into a string.
     * @author Kyra Lothrop 101145872
     * @return the utility's current information, String
     */
    @Override
    public String toString() {
        return "\n\tname='" + super.getName() + '\'';
    }
}
