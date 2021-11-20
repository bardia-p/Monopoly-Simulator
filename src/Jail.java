import java.util.HashMap;

/**
 * Group 3
 * SYSC 3110 - Milestone 3 Jail Class
 *
 * This document is the Jail. This class extends BoardCell.
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 1.0
 *
 */

public class Jail extends BoardCell{

    private HashMap<Player, Integer> inJail;
    public static final int FEE = 50;

    /**
     * The constructor for jail cell.
     *
     * @param name    keeps track of the name of the cell, String
     * @param imgName
     * @author Sarah Chow 101143033
     */
    public Jail(String name, Player owner, String imgName) {
        super(name, owner, CellType.JAIL, imgName);

        inJail = new HashMap<>();

    }

    /**
     * Put player in jail, start counting their rounds.
     * @author Sarah Chow 101143033
     * @param player player in jail, Player
     */
    public void startJail(Player player){
        inJail.put(player, 1);
    }

    /**
     * Remove player from jail.
     * @author Sarah Chow 101143033
     * @param player
     */
    public void endJail(Player player){
        inJail.remove(player);
    }

    /**
     * Increment the amount of rounds the player has been in jail.
     * @author Sarah Chow 101143033
     * @param player player in jail
     */
    public void incrementJailRound(Player player){
        inJail.put(player, inJail.get(player) + 1);
    }

    /**
     * Method to get the amount of rounds the player has been in jail.
     * @author Sarah Chow 101143033
     * @param player player in jail, Player
     * @return the amount of rounds the player has been in jail.
     */
    public int getRoundCountJail(Player player){
        return (inJail.get(player));
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
