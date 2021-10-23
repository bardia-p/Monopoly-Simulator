// Bardia Parmoun & Kyra Lothrop

import java.util.List;

public interface BoardView {
    void handleBoardUpdate(BoardEvent e);
    void handleRoll(int die1, int die2, Player player);
    void showCurrentCell(Player player);
    void handleBuyProperty(Player player, Property property, boolean result);
    void handleSellProperty(Player player, Property property, boolean result);
    void handleGetPlayerStatus(Player player);
    void handleGetBoardStatus(List<Player> players);
    void handleGetCellStatus(BoardCell currentCell);
    void handleRollingDoubles(Player player);
    void handleWelcomeMonopoly();
    void handlePayFees(BoardCell boardCell, Player renter, int fees, boolean result);
    void handleCurrentPlayerChange();
    void handleForFeitedPlayer(Player player);
    void handleWinner(List<Player> player);
}