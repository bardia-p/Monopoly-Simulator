import java.util.Map;
import java.util.Objects;

/**
 * Group 3
 * SYSC 3110 - Milestone 2 BoardCell Class
 *
 * This document is the BoardCell. This class extends BoardCell and has a name, owner, and CellType.
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 1.0
 *
 */
public abstract class BoardCell {
    /**
     * Keeps track of the cell name.
     */
    private final String name;
    /**
     * Keeps track of the cell owner.
     */
    private Player owner;
    /**
     * Keeps track of the cell type.
     */
    private final CellType type;
    /**
     * Keeps track of the image path of the cell.
     */
    private final String imgPath;

    /**
     * Keeps track of all the possible cell types.
     */
    public enum CellType {PROPERTY, GO, FREE_PARKING, JAIL, TAX, UTILITY, RAILROAD, GO_TO_JAIL}

    /**
     * The constructor for board cell.
     * @author Owen VanDusen 101152022
     * @param name keeps track of the name of the cell, String
     * @param owner keeps track of the cell owner, Player
     * @param type keeps track of the cell type, CellType
     */
    public BoardCell(String name, Player owner, CellType type, String imgName){
        this.name = name;
        this.owner = owner;
        this.type = type;
        this.imgPath = imgName;
    }

    /**
     * Accessor to get the owner of the property.
     * @author Owen VanDusen 101152022
     * @return the owner object, Player
     */
    public Player getOwner() {
        return this.owner;
    }

    /**
     * Accessor to get the name of the cell.
     * @author Sarah Chow 101143033
     * @return name of the property, String
     */
    public String getName(){
        return name;
    }

    /**
     * Accessor to set the owner of a cell to a specific player
     * @author Sarah Chow 101143033
     * @param player the player that owns the property, Player
     */
    public void setOwner(Player player) {
        this.owner = player;
    }

    /**
     * Accessor method to get the type of the cell
     * @author Owen VanDusen 101152022
     * @return the cell type, CellType
     */
    public CellType getType(){
        return type;
    }

    /**
     * Returns the image path of the cell.
     * @author Bardia Parmoun 101143006
     * @return the imgPath of the cell, String
     */
    public String getImgPath(){
        return imgPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardCell boardCell = (BoardCell) o;
        return Objects.equals(name, boardCell.name) && type == boardCell.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    /**
     * Accessor method to package relevant information into a string.
     * @author Sarah Chow 101143033
     * @return the cell's current information, String
     */
    public abstract String toString();
}
