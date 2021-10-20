// Bardia Parmoun & Kyra Lothrop

public interface BoardView {
    boolean handleBuyProperty(String propertyName, String playerName);
    boolean handlePayRent(String propertyName, String playerName);
    boolean handleNewPlayer(String playerName, String playerIcon);

}
