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

    /**
     * Keeps track of players in jail and number of rounds spent in jail.
     */
    private final HashMap<Player, Integer> inJail;

    /**
     * Fee required to pay to leave jail.
     */
    public static final int FEE = 50;
    /**
     * Keeps track of the number of doubles a player rolls before going to jail.
     */
    public static final int JAIL_ROLL_LIMIT = 3;

    /**
     * The constructor for jail cell.
     *
     * @param name keeps track of the name of the cell, String
     * @param imgName the image path of the cell, String.
     * @author Sarah Chow 101143033
     */
    public Jail(String name, Player owner, String imgName) {
        super(name, owner, CellType.JAIL, imgName);

        inJail = new HashMap<>();

    }

    /**
     * Remove player from jail.
     * @author Sarah Chow 101143033
     * @param player the player in jail, Player
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
        if (inJail.containsKey(player)){
            inJail.put(player, inJail.get(player) + 1);
        } else {
            inJail.put(player, 0);
        }
    }

    /**
     * Method to get the amount of rounds the player has been in jail.
     * @author Sarah Chow 101143033
     * @param player player in jail, Player
     * @return the amount of rounds the player has been in jail.
     */
    public int getRoundCountJail(Player player){
        return (inJail.getOrDefault(player, 0));
    }

    /**
     * Checks to see if this is the player's last round in jail.
     * @author Bardia Parmoun, 101143006
     * @param player the player to check.
     * @return true if it is the player's last round.
     */
    public boolean isPlayersLastRound(Player player){
        return getRoundCountJail(player) == 3;
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
