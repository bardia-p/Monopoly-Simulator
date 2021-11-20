import java.util.*;
import java.util.List;

/**
 * Group 3
 * SYSC 3110 - Milestone 2 Player Class
 *
 * This document is the Player. This class has the name, icon, cash value,
 * position on board, list of properties, the current property they are on, their rent status (paid rent, unpaid rent,
 * no rent to pay), number of doubles rolled, if they roll again, if they are bankrupt, and their player rank.
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 1.0
 */
public class Player {
    /**
     * Keeps track of player name.
     */
    private final String name;
    /**
     * Keeps track of player icon.
     */
    private final BoardModel.Icon icon;
    /**
     * Keeps track of player available money.
     */
    private int cash;
    /**
     * Keeps track of player position on the board.
     */
    private int position;
    /**
     * Keeps track of properties owned by the player.
     */
    private final List<Property> properties;
    /**
     * Keeps track of the property the play is currently standing on.
     */
    private BoardCell currentCell;
    /**
     * Keeps track of whether the player needs to pay rent, and whether they have.
     */
    private StatusEnum feesStatus;
    /**
     * Keeps track of how many doubles the player has rolled on their current turn.
     */
    private int numDoubles;
    /**
     * Keeps track of whether player should roll again on their turn.
     */
    private boolean rollAgain;
    /**
     * Keeps track of whether play is bankrupt.
     */
    private boolean bankrupt;
    /**
     * Keeps track of player rank after going bankrupt.
     */
    private int rank;

    /**
     * Keeps track of whether the player would like to forfeit.
     */
    private boolean request_forfeit;

    /**
     * Property the player would like to sell
     */
    private Property propertyToSell;

    /**
     * Keeps track of whether the player would like to sell a property.
     */
    private boolean confirmSell;

    /**
     * Possible values of player debt status.
     */
    public enum StatusEnum {NO_FEES, UNPAID_FEES, PAID_FEES}

    /**
     * Keeps track if the player is in jail.
     */
    private boolean resortInJail;

    /**
     * Keeps track if this is the player's final round in jail.
     */
    private boolean lastRoundJail;

    /**
     * Constructor for Player, sets all values.
     * @author Sarah Chow 101143033
     * @author Kyra Lothrop 101145872
     * @author Bardia Parmoun 101143006
     * @author Owen VanDusen 101152022
     * @param name Player name, String
     * @param icon Player icon, String
     */
    Player(String name, BoardModel.Icon icon) {
        this.bankrupt = false;
        this.name = name;
        this.icon = icon;
        this.cash = 1500;
        this.position = 0;
        this.properties = new ArrayList<>();
        this.numDoubles = 0;
        this.rollAgain = false;
        this.feesStatus = StatusEnum.NO_FEES;
        this.rank = 0;
        this.request_forfeit = false;
        this.propertyToSell = null;
        this.confirmSell = false;
        this.resortInJail = false;
        this.lastRoundJail = false;
    }

    /**
     * Method to toggle last round jail boolean value.
     * @author Sarah Chow 101143033
     */
    public void toggleLastRoundJail(){
        this.lastRoundJail = !this.lastRoundJail;
    }

    /**
     * Accessor to get the last round jail value.
     * @author Sarah Chow 101143033
     * @return if the player is in jail, boolean
     */
    public boolean getLastRoundJail(){
        return this.lastRoundJail;
    }

    /**
     * Method to toggle resort in jail boolean value.
     * @author Sarah Chow 101143033
     */
    public void toggleResortInJail() {
        this.resortInJail = !this.resortInJail;
    }

    /**
     * Accessor to get the resort in jail value.
     * @author Sarah Chow 101143033
     * @return if the player resorts in jail, boolean
     */
    public boolean getResortInJail(){
        return this.resortInJail;
    }


    /**
     * Method to deduct the amount the player must pay.
     * @author Bardia Parmoun 101143006
     * @param amount amount to pay, int
     */
    public void pay(int amount){
        this.cash -= amount;
    }

    /**
     * Method to purchase a property.
     * @author Sarah Chow 101143033
     * @param property property object to be purchased, Property
     */
    public void buyProperty(Property property){
        this.properties.add(property);
        this.cash -= property.getPrice();
    }

    /**
     * Method to sell a property.
     * @author Sarah Chow 101143033
     * @param property property object to be sold, Property
     */
    public void sellProperty(Property property){
        this.properties.remove(property);
        this.cash += property.getPrice();
        this.toggleConfirmSell();
    }

    /**
     * Accessor to toggle the confirm sell boolean value.
     * @author Sarah Chow 101143033
     */
    public void toggleConfirmSell(){
        this.confirmSell = !this.confirmSell;
    }

    /**
     * Accessor to get the confirm sell boolean value.
     * @author Sarah Chow 101143033
     * @return the confirm sell value, boolean
     */
    public boolean getConfirmSell(){
        return this.confirmSell;
    }

    /**
     * Method to set a player to be bankrupt.
     * @author Owen VanDusen 101152022
     */
    public void setBankrupt(){
        this.bankrupt = true;
        this.cash = 0;
        for (Property property: properties){
            property.setOwner(null);
        }
    }

    /**
     * Method to check if a player is bankrupt
     * @author Owen VanDusen 101152022
     * @return whether the player is bankrupt, boolean
     */
    public boolean isBankrupt(){
        return this.bankrupt;
    }

    /**
     * Method to receive rental payment.
     * @author bardia Parmoun 101143006
     * @param rentMoney amount to be paid for rent, int
     */
    public void getMoney(int rentMoney){
        this.cash += rentMoney;
    }

    /**
     * Accessor to get the name of the icon of the player.
     * @author Bardia Parmoun 101143006
     * @return icon of player, String
     */
    public String getIconName(){
        return icon.getName();
    }

    /**
     * Accessor to get the image path of the icon of the player.
     * @author Bardia Parmoun 101143006
     * @return image path of player icon, String
     */
    public String getIconImgPath(){
        return icon.getImgPath();
    }

    /**
     * Accessor to set the position of the player on the board.
     * @author Bardia Parmoun 101143006
     * @param newPosition the new position of the player, int
     */
    public void setPosition(int newPosition){
        this.position = newPosition;
    }

    /**
     * Accessor to get the position of the player on the board.
     * @author Sarah Chow 101143033
     * @return the position of the player, int
     */
    public int getPosition(){
        return position;
    }

    /**
     * Accessor to set the current property of the player on the board.
     * @param currentCell the property the player is currently on, Property
     * @author Kyra Lothrop 101145872
     */
    public void setCurrentCell(BoardCell currentCell){
        this.currentCell = currentCell;
    }

    /**
     * Accessor to get the current cell of the player on the board.
     * @author Kyra Lothrop 101145872
     * @return the cell the player is on, BoardCell
     */
    public BoardCell getCurrentCell(){
        return currentCell;
    }

    /**
     * Accessor to set the current amount of cash a player has. Used exclusively for test cases.
     * @param cash the amount of cash you want the player to have
     * @author Owen VanDusen 101155022
     */
    public void setCash(int cash){
        this.cash = cash;
    }

    /**
     * Accessor for cash attribute.
     * @author Bardia Parmoun 101143006
     * @return cash value, int
     */
    public int getCash() {
        return this.cash;
    }

    //This method will be needed when Jail is implemented :)
    /**
     * Accessor to get the number of doubles.
     * @author Owen VanDusen 101152022
     * @return number of doubles the player has rolled, int
     */
    public int getNumDoubles(){
        return numDoubles;
    }

    /**
     * Method to add a value to the number of doubles.
     * @author Owen VanDusen 101152022
     */
    public void addNumDoubles(){
        this.numDoubles += 1;
    }

    /**
     * Method to reset number of doubles to 0.
     * @author Owen VanDusen 101152022
     */
    public void resetNumDoubles(){
        this.numDoubles = 0;
    }

    /**
     * Accessor to determine if the player has another roll.
     * @author Owen VanDusen 101152022
     * @return if the player has another roll, boolean
     */
    public boolean hasAnotherRoll(){
        return rollAgain;
    }

    /**
     * Accessor to set if the user rolls again.
     * @author Owen VanDusen 101152022
     * @param rollAgain if the user rolls again, boolean
     */
    public void setRollAgain(boolean rollAgain){
        this.rollAgain = rollAgain;
    }

    /**
     * Accessor to set the rent status of the player (no rent, paid, unpaid)
     * @author Kyra Lothrop 101145872
     * @param feesStatus status to be set, StatusEnum
     */
    public void setFeesStatus(StatusEnum feesStatus) {
        this.feesStatus = feesStatus;
    }

    /**
     * Accessor to get the rent status of the player
     * @author Kyra Lothrop 101145872
     * @return the rent status of the player, StatusEnum
     */
    public StatusEnum getFeesStatus() {
        return feesStatus;
    }

    /**
     * Accessor to get all the player's properties.
     * @author Sarah Chow 101143033
     * @return all the user's properties, List<Property>
     */
    public List<Property> getProperties(boolean sellable) {
        if(sellable){
            ArrayList<Property> sellableProperties = new ArrayList<>();
            for (Property property: properties){
                if (!property.getRecentlyChanged()){
                    sellableProperties.add(property);
                }
            }

            return sellableProperties;
        }
        return properties;
    }

    public void setPropertyToSell(Property propertyToSell) {
        this.propertyToSell = propertyToSell;
    }

    public Property getPropertyToSell() {
        return propertyToSell;
    }

    /**
     * Accessor to set the rank of the player when they go bankrupt.
     * @author Kyra Lothrop 101145872
     * @param rank status to be set, int
     */
    public void setRank (int rank){
        this.rank = rank;
    }

    /**
     * Accessor method to get the player's rank.
     * @author Kyra Lothrop 101145872
     * @return the final rank of the player, int
     */
    public int getRank(){
        return rank;
    }

    /**
     * Accessor to toggle the request forfeit boolean variable.
     * @author Sarah Chow 101143033
     */
    public void toggleRequest_forfeit() {
        this.request_forfeit = !this.request_forfeit;
    }

    /**
     * Accessor to get whether the player has requested to forfeit.
     * @author Sarah Chow 101143033
     * @return whether the player would like to forfeit, boolean
     */
    public boolean getRequest_forfeit(){
        return request_forfeit;
    }


    /**
     * Accessor method to package relevant information into a string.
     * @author Bardia Parmoun 101143006
     * @return the player's current information, String
     */
    @Override
    public String toString() {
        String playerInfo;

        playerInfo = "\tname='" + name + '\'' +
                "\n\ticon='" + icon + '\'' +
                "\n\tcash='" + cash + '\'' +
                "\n\tposition='" + position + '\'' +
                "\n\tbankrupt=" + bankrupt +
                "\n\tproperties= { \n\t\t";

        for (Property property: properties)
        {
            playerInfo += property.getName() + "\n\t\t";
        }

        playerInfo += "}";

        return playerInfo;
    }
}