public class BoardController implements BoardView{
    @java.lang.Override
    public void handleBuyProperty(Property property, Player player) {
        property.setOwner(player);
        player.pay(property.getPrice());
    }
}
