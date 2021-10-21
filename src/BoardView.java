// Bardia Parmoun & Kyra Lothrop

import java.util.List;

public interface BoardView {
    boolean handleBuyProperty(int propertyIndex, String playerName);
    boolean handlePayRent(int propertyIndex, String playerName);
    boolean handleNewPlayer(String playerName, String playerIcon);

    //boolean handlePropertyBoughtCheck(int propertyIndex);

    //String handleGetPropertyStatus(int propertyIndex);
    //String handleGetPlayerStatus(String playerIcon);

    //String handleGetPropertyName(int propertyIndex);
    //int handleGetPropertyRent(int propertyIndex);

    List<String> handleGetPropertyStatus(int propertyIndex);
    List<String> handleGetPlayerStatus(String playerIcon);
}
