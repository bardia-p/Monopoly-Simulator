import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Group 3
 * SYSC 3110 - Milestone 2 Go Class
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
    private final int reward;

    /**
     * The constructor for Go
     * @author Owen VanDusen 101152022
     * @param reward keeps track of the Go reward, int
     */
    public Go(int reward, String imgName){
        super("GO", null, CellType.GO, imgName);
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
     * Accessor method to package relevant information into a linked hash map.
     * @author Sarah Chow 101143033
     * @return attributes of GO, Map
     */
    public Map<String, String> getAttributes() {

        Map<String, String> attributes = new LinkedHashMap<>();

        attributes.put("Cell Name: ", String.valueOf(super.getName()));

        return attributes;
    }
}
