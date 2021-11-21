/**
 * Group 3
 * SYSC 3110 - Milestone 3 Free Parking Class
 *
 * This document is FreeParking Go. This class extends BoardCell.
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 1.0
 */
public class FreeParking extends BoardCell{
    /**
     * The constructor of free parking
     * @author Bardia Parmoun, 101143006
     * @param imgName the image of the cell, String
     */
    public FreeParking(String imgName){
        super("FREE PARKING", null, CellType.FREE_PARKING, imgName);
    }

    /**
     * Accessor method to package relevant information into a string.
     * @author Bardia Parmoun, 101143006
     * @return the cell's current information, String
     */
    @Override
    public String toString() {
        return "\n\tname='" + super.getName() + '\'';
    }
}
