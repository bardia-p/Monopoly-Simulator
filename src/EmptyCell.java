/**
 * Group 3
 * SYSC 3110 - Milestone 3 EmptyCell Class
 *
 * This document is the EmptyCell. This class extends BoardCell and it is used for the cells that have not been
 * implemented yet.
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 1.0
 */
public class EmptyCell extends BoardCell {
    /**
     * The constructor for EmptyCell
     * @author Sarah Chow 101143033
     * @author Kyra Lothrop 101145872
     * @author Bardia Parmoun 101143006
     * @author Owen VanDusen 101152022
     * @param name cell name, String
     * @param type cell type, CellType
     * @param imgPath, the path to the image, String
     */
    public EmptyCell(String name, CellType type, String imgPath){
        super(name, null, type, imgPath);
    }

    /**
     * The constructor for EmptyCell without imgName
     * @author Sarah Chow 101143033
     * @author Kyra Lothrop 101145872
     * @author Bardia Parmoun 101143006
     * @author Owen VanDusen 101152022
     * @param name cell name, String
     * @param type cell type, CellType
     * @param imgPath, the path to the image, String
     */
    public EmptyCell(String name, CellType type){

        this(name, type, "");
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
