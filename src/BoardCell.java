/**
 * SYSC 3110 - Milestone 1 BoardCell Class
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
    private String name;
    /**
     * Keeps track of the cell owner.
     */
    private Player owner;
    /**
     * Keeps track of the cell type.
     */
    private CellType type;
    /**
     * Keeps track of all the possible cell types.
     */
    public enum CellType {PROPERTY, GO, FREE_PARKING, JAIL, TAX}

    /**
     * The constructor for board cell.
     * @author Owen VanDusen 101152022
     * @param name keeps track of the name of the cell, String
     * @param owner keeps track of the cell owner, Player
     * @param type keeps track of the cell type, CellType
     */
    public BoardCell(String name, Player owner, CellType type){
        this.name = name;
        this.owner = owner;
        this.type = type;
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
     * Accessor method to package relevant information into a string.
     * @author Sarah Chow 101143033
     * @return the cell's current information, String
     */
    public abstract String toString();

    /**
     * Accessor method to get the type of the cell
     * @author Owen VanDusen 101152022
     * @return the cell type, CellType
     */
    public CellType getType(){
        return type;
    }
}
