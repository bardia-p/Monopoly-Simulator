import org.junit.*;
import static org.junit.Assert.*;

/**
 * Group 3
 * SYSC 3110 - Milestone 3 BoardModelTest Class
 *
 * This test class ensures the backing data used to display the Monopoly game is properly
 * stored in the BoardModel class and that various BoardModel methods are working as expected
 * @author Owen VanDusen 101152022
 * @author Sarah Chow 101143033
 * @version 2.0
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
        boardModel.constructBoard("originalBoard.xml");
        p1 = new Player("Owen", BoardModel.Icon.ICON3);
        p2 = new Player("Also Owen", BoardModel.Icon.ICON7);

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
        boardModel.setDice(8, 0);
        boardModel.move(p1);

        boardModel.setDice(2, 0);
        boardModel.move(p2);

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
        boardModel.setDice(16, 0);
        boardModel.move(p1);

        boardModel.setDice(4, 0);
        boardModel.move(p2);
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
        boardModel.setDice(8, 0);
        boardModel.move(p1);
        boardModel.buyLocation(p1.getCurrentCell(), p1);

        boardModel.setDice(3, 0);
        boardModel.move(p2);
        boardModel.buyLocation(p2.getCurrentCell(), p2);

        boardModel.move(p2);
        boardModel.buyLocation(p2.getCurrentCell(), p2);
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

        boardModel.setDice(4, 0);
        boardModel.move(p1);
        boardModel.payFees(p1.getCurrentCell(), p1);
        assertEquals(1300, p1.getCash());
        assertEquals(BoardCell.CellType.TAX, p1.getCurrentCell().getType());

        boardModel.setDice(34, 0);
        boardModel.move(p1);
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

        boardModel.setDice(9, 0);
        boardModel.move(p2);
        boardModel.buyLocation(p2.getCurrentCell(), p2);

        boardModel.move(p1);
        boardModel.payFees(p1.getCurrentCell(),p1);
        assertEquals(1492, p1.getCash());
        assertEquals(Player.StatusEnum.PAID_FEES, p1.getFeesStatus());
    }

    /**
     * Has the player buy properties, ensuring that the properties are correctly added to the
     * player information. Then, has the player sell some of their properties, checking that
     * the number of properties owned by the players has decreased accordingly.
     * @author Owen VanDusen 101152022
     */
    @Test
    public void testPlayerSellProperty(){
        assertEquals(0,p1.getOwnedLocations(false).size());

        boardModel.setDice(3, 0);
        boardModel.move(p1);
        boardModel.buyLocation(p1.getCurrentCell(), p1);
        assertEquals(1,p1.getOwnedLocations(false).size());

        boardModel.setDice(10, 0);
        boardModel.move(p1);
        boardModel.buyLocation(p1.getCurrentCell(), p1);

        boardModel.setDice(5, 0);
        boardModel.move(p1);
        boardModel.buyLocation(p1.getCurrentCell(), p1);
        assertEquals(3,p1.getOwnedLocations(false).size());

        boardModel.sellLocation(p1, p1.getOwnedLocations(false).get(0));
        boardModel.sellLocation(p1, p1.getOwnedLocations(false).get(0));
        assertEquals(1,p1.getOwnedLocations(false).size());
    }

    /**
     * Checks that properties purchased by the player are not sellable on the turn
     * they were purchased.
     * @author Owen VanDusen 101152022
     */
    @Test
    public void testPropertyNotImmediatelySellable(){
        boardModel.setDice(1, 0);
        boardModel.move(p2);
        boardModel.buyLocation(p2.getCurrentCell(), p2);
        assertEquals(0,p2.getOwnedLocations(true).size());

        boardModel.setDice(3, 0);
        boardModel.move(p1);
        boardModel.buyLocation(p1.getCurrentCell(), p1);

        boardModel.setDice(5, 0);
        boardModel.move(p1);
        boardModel.buyLocation(p1.getCurrentCell(), p1);
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
        boardModel.setDice(4, 0);
        boardModel.move(p1);
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
        boardModel.setDice(3, 0);
        boardModel.move(p1);
        boardModel.buyLocation(p1.getCurrentCell(), p1);
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


    /**
     * Test to ensure player 1 has successfully bought a utility.
     * Checks if their cash has decreased and their total owned properties has increased.
     * @author Sarah Chow 101143033
     */
    @Test
    public void testBuyUtility(){
        assertEquals(1500, p1.getCash());

        boardModel.setDice(12, 0);
        boardModel.move(p1); // Electric Company
        boardModel.buyLocation(p1.getCurrentCell(), p1);

        assertEquals(1500 - 150, p1.getCash());
        assertEquals(1, p1.getOwnedLocations(false).size());
    }

    /**
     * Test to ensure player 1 has successfully bought a Railroad.
     * Checks if their cash has decreased and their total owned properties has increased.
     * @author Sarah Chow 101143033
     */
    @Test
    public void testBuyRailroad(){
        assertEquals(1500, p1.getCash());

        boardModel.setDice(5, 0);
        boardModel.move(p1); // Electric Company
        boardModel.buyLocation(p1.getCurrentCell(), p1);

        assertEquals(1500 - 200, p1.getCash());
        assertEquals(1, p1.getOwnedLocations(false).size());
    }

    /**
     * To test when a player buys one and multiple Utilities. Checks and confirms
     * the total fees the player pays depending on the number of Utilities in possession.
     * @author Sarah Chow 101143033
     */
    @Test
    public void testLandOnBoughtUtility(){
        assertEquals(1500, p2.getCash());

        boardModel.setDice(12, 0);
        boardModel.move(p1); // Electric Company
        boardModel.buyLocation(p1.getCurrentCell(), p1);

        // P1 owns one utility
        boardModel.move(p2); // To make way to Electric Company
        boardModel.setDice(5, 0);
        boardModel.payFees(p2.getCurrentCell(), p2);
        assertEquals(1500 - (5 * 4), p2.getCash()); // One utility fee = rolled number * 4
        assertEquals(1500 - 150 + (5 * 4), p1.getCash());

        // P1 owns two utilities
        boardModel.setDice(16, 0);
        boardModel.move(p1); // Water Works
        boardModel.buyLocation(p1.getCurrentCell(), p1);

        boardModel.move(p2);
        boardModel.setDice(5, 3);
        boardModel.payFees(p2.getCurrentCell(), p2);
        assertEquals(1500 - (5 * 4) - (8 * 10), p2.getCash()); // Two utilities fee = rolled number * 10
        assertEquals(1500 - 300 + (5 * 4) + (8 * 10), p1.getCash());
    }

    /**
     * To test when a player buys one and multiple Railroads. Checks and confirms the total fees
     * the player pays depending on the number of Railroads in possession.
     * @author Sarah Chow 101143033
     */
    @Test
    public void testLandOnBoughtRailroad(){
        assertEquals(1500, p1.getCash());
        assertEquals(1500, p2.getCash());

        // P1 owns one railroad
        boardModel.setDice(5, 0);
        boardModel.move(p1); // Reading Railroad
        boardModel.buyLocation(p1.getCurrentCell(), p1);

        boardModel.move(p2);
        boardModel.payFees(p2.getCurrentCell(), p2);
        assertEquals(1500 - 25, p2.getCash());
        assertEquals(1500 - 200 + 25, p1.getCash());


        // P1 owns two railroads
        boardModel.setDice(10, 0);
        boardModel.move(p1); // Reading Railroad
        boardModel.buyLocation(p1.getCurrentCell(), p1);

        boardModel.move(p2);
        boardModel.payFees(p2.getCurrentCell(), p2);
        assertEquals(1500 - 75, p2.getCash());
        assertEquals(1500 - 400 + 75, p1.getCash());


        // P1 owns three railroads
        boardModel.move(p1); // Reading Railroad
        boardModel.buyLocation(p1.getCurrentCell(), p1);

        boardModel.move(p2);
        boardModel.payFees(p2.getCurrentCell(), p2);
        assertEquals(1500 - 175, p2.getCash());
        assertEquals(1500 - 600 + 175, p1.getCash());


        // P1 owns four railroads
        boardModel.move(p1); // Reading Railroad
        boardModel.buyLocation(p1.getCurrentCell(), p1);

        boardModel.move(p2);
        boardModel.payFees(p2.getCurrentCell(), p2);
        assertEquals(1500 - 375, p2.getCash());
        assertEquals(1500 - 800 + 375, p1.getCash());
    }

    /**
     * Test that the player is sent to Jail if they land on Go To Jail.
     * Checks their position on the baord after they are sent to jail.
     * @author Sarah Chow 101143033
     */
    @Test
    public void testLandOnGoToJail(){
        boardModel.setDice(30, 0);
        boardModel.move(p1);

        assertEquals(10, p1.getPosition()); // Sent to jail
    }

    /**
     * Test when a player buys their way out of jail. Checks when the player is in jail and pays the fee.
     * @author Sarah Chow 101143033
     */
    @Test
    public void testBuyOutOfJail(){
        assertEquals(1500, p1.getCash());
        boardModel.setDice(30, 0);
        boardModel.move(p1);
        ((Jail)p1.getCurrentCell()).incrementJailRound(p1); // Increment rounds to 1

        boardModel.payFees(p1.getCurrentCell(), p1);
        assertEquals(1500 - 50, p1.getCash());
    }

    /**
     * Test for player to leave jail once they roll a double.
     * @author Sarah Chow 101143033
     */
    @Test
    public void testRollDoublesJail(){
        boardModel.setDice(30, 0);
        boardModel.move(p1);
        ((Jail)p1.getCurrentCell()).incrementJailRound(p1); // Increment rounds to 1

        boardModel.roll(p1);
        while(boardModel.getDice()[0] != boardModel.getDice()[1]){
            boardModel.roll(p1);
        }

        assertFalse(p1.getResortInJail());
    }

    /**
     * Test for player to go to jail if they roll three doubles in a row.
     * @author Sarah Chow 101143033
     */
    @Test
    public void testRollThreeDoublesJail(){
        for (int i = 0; i < 3; i++){
            boardModel.setDoubleRoll(p1);
        }

        assertEquals(BoardCell.CellType.JAIL, p1.getCurrentCell().getType());
    }


    /**
     * Tests when the player passes GO. Confirms they collected $200.
     * @author Sarah Chow 101143033
     */
    @Test
    public void testGO(){
        assertEquals(1500, p1.getCash());

        boardModel.setDice(45, 0);
        boardModel.move(p1);

        assertEquals(1500 + 200, p1.getCash());
    }

    /**
     * Test when a player lands on free parking.
     * Confirms that player collects all the taxes and jail payments on the board.
     * @author Sarah Chow 101143033
     */
    @Test
    public void testFreeParking(){
        boardModel.setDice(4, 0);
        boardModel.move(p1); // Income Tax - $200
        boardModel.payFees(p1.getCurrentCell(), p1);

        boardModel.setDice(26, 0);
        boardModel.move(p1); // To jail
        ((Jail)p1.getCurrentCell()).incrementJailRound(p1); // Increment rounds to 1
        boardModel.payFees(p1.getCurrentCell(), p1); // Pay out of jail - $50

        boardModel.setDice(28, 0);
        boardModel.move(p1); // Luxury Tax - $100
        boardModel.payFees(p1.getCurrentCell(), p1);

        boardModel.setDice(20, 0);
        boardModel.move(p2);
        assertEquals(1500 + 350, p2.getCash());
    }

    /**
     * Test buying houses and paying house rent. Confirms the player bought a house and is charging the
     * next rental fee value.
     * @author Sarah Chow 101143033
     */
    @Test
    public void testBuyHouse(){
        boardModel.setDice(1, 0);
        boardModel.move(p1);
        boardModel.buyLocation(p1.getCurrentCell(), p1);

        boardModel.setDice(2, 0);
        boardModel.move(p1);
        boardModel.buyLocation(p1.getCurrentCell(), p1);

        boardModel.buildHouse((Property) p1.getCurrentCell(), p1);

        assertEquals(1, (((Property) p1.getCurrentCell()).getNumHouses()));

        boardModel.setDice(3, 0);
        boardModel.move(p2);
        boardModel.payFees(p2.getCurrentCell(), p2);

        assertEquals(1500 - 20, p2.getCash());

    }
}