/**
 * Group 3
 * SYSC 3110 - Milestone 2 Go Class
 *
 * This document is the Railroad. This class extends BoardCell.
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 1.0
 */

public class Railroad extends BoardCell implements Buyable{

    private final int price;

    private boolean recentlyChanged;

    private Integer[] rentValues;   //final?

    public Railroad(String name, int price, String imgName) {
        super(name, null, CellType.RAILROAD, imgName);
        this.price = price;
        this.recentlyChanged = false;
        this.rentValues = new Integer[]{25, 50, 100, 200};
    }

    public int getRent(int numRailroads) {
        return rentValues[numRailroads-1];
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
