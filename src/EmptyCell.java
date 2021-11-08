import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Group 3
 * SYSC 3110 - Milestone 1 Go Class
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
     * Accessor method to package relevant information into a linked hash map.
     * @author Sarah Chow 101143033
     * @return attributes of empty cell, Map
     */
    public Map<String, String> getAttributes() {

        Map<String, String> attributes = new LinkedHashMap<String, String>();

        attributes.put("Cell Name: ", String.valueOf(super.getName()));

        return attributes;
    }
}
