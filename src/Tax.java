/**
 * Group 3
 * SYSC 3110 - Milestone 3 Tax Class
 *
 * This document is the Tax. This class extends BoardCell and has a tax property.
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 3.0
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
     * The main constructor for Tax, without imgName
     * @author Bardia Parmoun 101143006
     * @param name of the cell, String
     * @param tax amount of tax for the cell, int
     * @param owner, the owner of the cell Player
     */
    Tax(String name, int tax, Player owner){
        super(name, owner, CellType.TAX, "");
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
     * Accessor method to package relevant information into a string.
     * @author Sarah Chow 101143033
     * @return the cell's current information, String
     */
    @Override
    public String toString() {
        return "\n\tname='" + super.getName().toUpperCase() + '\'' +
                "\n\ttax='" + tax + '\'';
    }
}
