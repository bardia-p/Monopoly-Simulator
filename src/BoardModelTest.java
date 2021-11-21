import org.junit.*;
import static org.junit.Assert.*;

/**
 * Group 3
 * SYSC 3110 - Milestone 2 BoardModelTest Class
 *
 * This test class ensures the backing data used to display the Monopoly game is properly
 * stored in the BoardModel class and that various BoardModel methods are working as expected
 * @author Owen VanDusen 101152022
 * @version 1.0
 */
public class BoardModelTest {

    /**
     * A board model used to test methods
     */
    BoardModel boardModel;
    /**
     * A player to be used by the board model
     */
    Player p1;
    /**
     * A player to be used by the board model
     */
    Player p2;

    /**
     * Initializes boardModel and the players to valid states so testing can ensue
     */
    @Before
    public void initialize(){
        boardModel = new BoardModel();
        boardModel.constructBoard();
        p1 = new Player("Owen", BoardModel.Icon.BATTLESHIP);
        p2 = new Player("Also Owen", BoardModel.Icon.THIMBLE);

        boardModel.addPlayer(p1);
        boardModel.addPlayer(p2);
    }

    /**
     * Moves the players to a specific position on the board and checks the player's
     * position has been updated accordingly.
     * @author Owen VanDusen 101152022
     */
    @Test
    public void testMovePlayer(){
        boardModel.move(p1,8);
        boardModel.move(p2,2);

        assertEquals(8, p1.getPosition());
        assertEquals(2,p2.getPosition());
    }

    /**
     * Moves the players to specific positions on the board and check that the cell
     * associated with their position matches the constructed monopoly board in the model.
     * @author Owen VanDusen 101152022
     */
    @Test
    public void testPlayerCurrentCell(){
        boardModel.move(p1,16);
        boardModel.move(p2,4);
        assertEquals("St. James Place", p1.getCurrentCell().getName());
        assertEquals(BoardCell.CellType.PROPERTY, p1.getCurrentCell().getType());
        assertEquals("Income Tax", p2.getCurrentCell().getName());
        assertEquals(BoardCell.CellType.TAX, p2.getCurrentCell().getType());
    }

    /**
     * Moves the players to unowned properties, has them purchase the properties, then checks
     * that the properties have been added to the respective player's arrayList
     * @author Owen VanDusen 101152022
     */
    @Test
    public void testPlayerBuyProperty(){
        boardModel.move(p1,8);
        boardModel.buyLocation((Property) p1.getCurrentCell(), p1);
        boardModel.move(p2, 3);
        boardModel.buyLocation((Property) p2.getCurrentCell(), p2);
        boardModel.move(p2, 3);
        boardModel.buyLocation((Property) p2.getCurrentCell(), p2);
        assertEquals(1,p1.getOwnedLocations(false).size());
        assertEquals(2, p2.getOwnedLocations(false).size());
    }

    /**
     * Moves the player to each of the tax cells on the board. Checks that the player
     * pays the correct amount of cash at each Tax and that the cells are registered as
     * Tax cells.
     * @author Owen VanDusen 101152022
     */
    @Test
    public void testPlayerPayTax(){
        assertEquals(1500, p1.getCash());

        boardModel.move(p1, 4);
        boardModel.payFees(p1.getCurrentCell(), p1);
        assertEquals(1300, p1.getCash());
        assertEquals(BoardCell.CellType.TAX, p1.getCurrentCell().getType());


        boardModel.move(p1,34);
        boardModel.payFees(p1.getCurrentCell(), p1);
        assertEquals(1200, p1.getCash());
        assertEquals(BoardCell.CellType.TAX, p1.getCurrentCell().getType());

    }

    /**
     * Has player 2 buy a property and player 1 land on that property, checking if the correct
     * amount of rent is paid and that player1's status is updated correctly.
     * @author Owen VanDusen 101152022
     */
    @Test
    public void testPlayerPayRent(){
        assertEquals(1500, p1.getCash());

        boardModel.move(p2, 9);
        boardModel.buyLocation((Property) p2.getCurrentCell(), p2);

        boardModel.move(p1, 9);
        boardModel.payFees(p1.getCurrentCell(),p1);
        assertEquals(1492,p1.getCash());
        assertEquals(Player.StatusEnum.PAID_FEES, p1.getFeesStatus());
    }

    /**
     * Has the player buy properties, ensuring that the properties are correctly added to the
     * player information. Then, has the player sell some of their properties, checking that
     * the number of properties owned by the players has decreased accordingly.
     * @author Owen VanDusen
     */
    @Test
    public void testPlayerSellProperty(){
        assertEquals(0,p1.getOwnedLocations(false).size());

        boardModel.move(p1,3);
        boardModel.buyLocation((Property)p1.getCurrentCell(), p1);
        assertEquals(1,p1.getOwnedLocations(false).size());

        boardModel.move(p1, 10);
        boardModel.buyLocation((Property)p1.getCurrentCell(), p1);
        boardModel.move(p1,5);
        boardModel.buyLocation((Property)p1.getCurrentCell(), p1);
        assertEquals(3,p1.getOwnedLocations(false).size());

        boardModel.sellProperty(p1, p1.getOwnedLocations(false).get(0));
        boardModel.sellProperty(p1, p1.getOwnedLocations(false).get(0));
        assertEquals(1,p1.getOwnedLocations(false).size());
    }

    /**
     * Checks that properties purchased by the player are not sellable on the turn
     * they were purchased.
     * @author Owen VanDusen 101152022
     */
    @Test
    public void testPropertyNotImmediatelySellable(){
        boardModel.move(p2,1);
        boardModel.buyLocation((Property) p2.getCurrentCell(), p2);
        assertEquals(0,p2.getOwnedLocations(true).size());

        boardModel.move(p1,3);
        boardModel.buyLocation((Property) p1.getCurrentCell(), p1);
        boardModel.move(p1, 5);
        boardModel.buyLocation((Property)p1.getCurrentCell(), p1);
        assertEquals(0,p1.getOwnedLocations(true).size());
    }

    /**
     * Sets the player's fees below the required amount to pay tax and checks
     * that their enum updates to the correct status
     * @author Owen VanDusen 101152022
     */
    @Test
    public void testPlayerCannotAffordFees(){
        p1.setCash(100);
        boardModel.move(p1,4);
        boardModel.payFees(p1.getCurrentCell(), p1);

        assertEquals(Player.StatusEnum.UNPAID_FEES, p1.getFeesStatus());
    }

    /**
     * Has player 1 purchase a property, checks that the property cannot be sold on the turn.
     * Then, passes the turn to the next player and checks that the property can be sold.
     * @author Owen VanDusen 101152022
     */
    @Test
    public void testPropertiesBecomeSellableAfterTurnPass(){
        boardModel.move(p1,3);
        boardModel.buyLocation((Property) p1.getCurrentCell(), p1);
        assertTrue(((Property) p1.getCurrentCell()).getRecentlyChanged());
        assertEquals(0, p1.getOwnedLocations(true).size());
        assertEquals(1, p1.getOwnedLocations(false).size());

        boardModel.passTurn(p1);
        assertFalse(((Property) p1.getCurrentCell()).getRecentlyChanged());
        assertEquals(1, p1.getOwnedLocations(true).size());
        assertEquals(1, p1.getOwnedLocations(false).size());
    }

    /**
     * Waits until the player rolls a double then checks if their number of doubles
     * count increases as it should.
     * @author Owen VanDusen 101152022
     */
    @Test
    public void testRollingDoublesGivesAnotherRoll(){
        boardModel.roll(p1);
        while(boardModel.getDice()[0] != boardModel.getDice()[1]){
            boardModel.roll(p1);
        }
        assertEquals(1, p1.getNumDoubles());
    }
}