/**
 * Group 3
 * SYSC 3110 - Milestone 3 Buyable Interface
 *
 * This document is the Buyable interface. It holds method signatures that are implemented
 * by buyable locations on the board.
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 1.0
 */
public interface Buyable {
    int getPrice();
    void toggleRecentlyChanged();
    boolean getRecentlyChanged();
}
