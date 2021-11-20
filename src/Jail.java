import java.util.ArrayList;
import java.util.HashMap;

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

    public void startJail(Player p){
        inJail.put(p, 1);
    }

    public void endJail(Player p){
        inJail.remove(p);
    }

    /**
     * Increment the amount of rounds the player has been in jail.
     * @author Sarah Chow 101143033
     * @param p player in jail
     * @return boolean representing if the increment was successful or
     * if they have reached the maximum of 3 rounds in jail
     */
    public boolean incrementJailRound(Player p){
        inJail.put(p, inJail.get(p) + 1);

        if (inJail.get(p) < 3){
            return true;
        }

        return false;
    }

    public HashMap<Player, Integer> getInJail() {
        return inJail;
    }

    @Override
    public String toString() {
        return "\n\tname='" + super.getName() + '\'';
    }
}
