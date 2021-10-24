/**
 * SYSC 3110 - Milestone 1 Go Class
 *
 * This document is the Go. This class extends BoardCell and it as a reward attribute
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 1.0
 */
public class Go extends BoardCell {
    /**
     * Keeps track of the reward for passing Go.
     */
    private int reward;

    /**
     * The constructor for Go
     * @author Owen VanDusen 101152022
     * @param fee keeps track of the Go reward, int
     */
    public Go(int fee){
        super("GO", null, CellType.GO);
        this.reward = reward;
    }

    /**
     * Accessor for the reward attribute.
     * @author Owen VanDusen 101152022
     * @return the reward, int
     */
    public int getReward(){
        return reward;
    }

    /**
     * Accessor method to package relevant information into a string.
     * @author Sarah Chow 101143033
     * @return the cell's current information, String
     */
    @Override
    public String toString() {
        return "\n\t\tname='" + super.getName() + '\'';
    }
}
