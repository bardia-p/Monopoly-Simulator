/**
 * Group 3
 * SYSC 3110 - Milestone 2 Go Class
 *
 * This document is the Utility. This class extends BoardCell.
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 1.0
 */

public class Utility extends BoardCell implements Buyable{
    private final int price;

    private boolean recentlyChanged;

    private Integer[] rentValues;   //final?

    public Utility(String name, int price, String imgName) {
        super(name, null, CellType.UTILITY, imgName);
        this.price = price;
        this.recentlyChanged = false;
        this.rentValues = new Integer[]{4, 10};
    }

    public int getRent(int numUtilities, int[] dice) {
        return rentValues[numUtilities-1] * (dice[0]+ dice[1]);
    }

    public int getPrice() {
        return this.price;
    }

    public void toggleRecentlyChanged(){
        this.recentlyChanged = !this.recentlyChanged;
    }

    public boolean getRecentlyChanged() {
        return recentlyChanged;
    }

    @Override
    public String toString() {
        return "\n\tname='" + super.getName() + '\'';
    }
}
