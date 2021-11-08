import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Group 3
 * SYSC 3110 - Milestone 1 Tax Class
 *
 * This document is the Tax. This class extends BoardCell and has a tax property.
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 1.0
 *
 */
public class Tax extends BoardCell{
    /**
     * Keeps track of the tax for the cell.
     */
    private final int tax;

    /**
     * The main constructor for Tax.
     * @author Bardia Parmoun 101143006
     * @param name of the cell, String
     * @param tax amount of tax for the cell, int
     * @param owner, the owner of the cell Player
     */
    Tax(String name, int tax, Player owner, String imgName){
        super(name, owner, CellType.TAX, imgName);
        this.tax = tax;
    }

    /**
     * Returns the tax amount of the cell.
     * @author Owen VanDusen 101152022
     * @return the tax of the property, int
     */
    public int getTax(){
        return tax;
    }


    /**
     * Accessor method to package relevant information into a linked hash map.
     * @author Sarah Chow 101143033
     * @return attributes of Tax, Map
     */
    public Map<String, String> getAttributes() {

        Map<String, String> attributes = new LinkedHashMap<String, String>();

        attributes.put("Name: ", String.valueOf(super.getName()));
        attributes.put("Tax: ", String.valueOf(tax));

        return attributes;
    }
}
