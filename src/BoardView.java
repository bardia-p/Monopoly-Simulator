import java.util.List;

/**
 * SYSC 3110 - Milestone 1 BoardView Interface
 *
 * This document is the BoardView interface. It holds method signatures that are implemented
 * by the BoardListener.
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 1.0
 */
public interface BoardView {
    void handleBoardUpdate(BoardEvent e);
    void handleRoll(int die1, int die2, Player player);
    void showCurrentProperty(Player player);
    void handleBuyProperty(Player player, Property property, boolean result);
    void handleSellProperty(Player player, Property property, boolean result);
    void handleGetPlayerStatus(Player player);
    void handleGetBoardStatus(List<Player> players);
    void handleGetCellStatus(Property property);
    void handleRollingDoubles(Player player);
    void handleWelcomeMonopoly();
    void handlePayRent(Property property, Player renter, boolean result);
    void handleCurrentPlayerChange();
    void handleForfeitedPlayer(Player player);
    void handleWinner(List<Player> player);
}
