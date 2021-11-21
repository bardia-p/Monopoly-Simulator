/**
 * Group 3
 * SYSC 3110 - Milestone 3 GoToJail Class
 *
 * This document is the GoToJail. This class extends BoardCell.
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 1.0
 */
public class GoToJail extends BoardCell{
    /**
     * The constructor for the GoToJail class.
     * @author Sarah Chow 101143033
     * @param name the name of the cell.
     * @param imgName the image path of the cell.
     */
    public GoToJail(String name, String imgName) {
        super(name, null, CellType.GO_TO_JAIL, imgName);
    }

    /**
     * Accessor method to package relevant information into a string.
     * @author Sarah Chow 101143033
     * @return the cell's current information, String
     */
    @Override
    public String toString() {
        return "\n\tname='" + super.getName() + '\'';
    }
}
