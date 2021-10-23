/**
 * SYSC 3110 - Milestone 1 Player Class
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
 *
 */

import java.util.ArrayList;
import java.util.List;

public class Player {
    /**
     * Keeps track of player name.
     */
    private String name;
    /**
     * Keeps trach of player icon.
     */
    private String icon;
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
    private List<Property> properties;
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
     * keeps track of player rank after going bankrupt.
     */
    private int rank;

    /**
     * Possible values of player debt status.
     */
    public enum StatusEnum {NO_FEES, UNPAID_FEES, PAID_FEES};

    /**
     * Constructor for Player, sets all values.
     * @param name Player name, String
     * @param icon Player icon, String
     */
    Player(String name, String icon) {
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
    }


    /**
     * Method to deduct the amount the player must pay.
     * @param amount amount to pay, int
     */
    public void pay(int amount){
        this.cash -= amount;
    }

    /**
     * Method to purchase a property.
     * @param property property object to be purchased, Property
     */
    public void buyProperty(Property property){
        this.properties.add(property);
        this.cash -= property.getPrice();
    }

    /**
     * Method to sell a property.
     * @param property property object to be sold, Property
     */
    public void sellProperty(Property property){
        this.properties.remove(property);
        this.cash += property.getPrice();
    }

    /**
     * Method to set a player to be bankrupt.
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
     * @return whether the player is bankrupt, boolean
     */
    public boolean isBankrupt(){
        return this.bankrupt;
    }

    /**
     * Method to receive rental payment.
     * @param rentMoney amount to be paid for rent, int
     */
    public void getMoney(int rentMoney){
        this.cash += rentMoney;
    }

    /**
     * Accessor to get the icon of the player.
     * @return icon of player, String
     */
    public String getIcon(){
        return icon;
    }

    /**
     * Accessor to set the position of the player on the board.
     * @param newPosition the new position of the player, int
     */
    public void setPosition(int newPosition){
        this.position = newPosition;
    }

    /**
     * Accessor to get the position of the player on the board.
     * @return the position of the player, int
     */
    public int getPosition(){
        return position;
    }

    /**
     * Accessor to set the current property of the player on the board.
     * @param currentProperty the property the player is currently on, Property
     */
    public void setCurrentCell(BoardCell currentProperty){
        this.currentCell = currentProperty;
    }

    /**
     * Accessor to get the current property of the player on the board.
     * @return the property the player is on, Property
     */
    public BoardCell getCurrentCell(){
        return currentCell;
    }

    /**
     * Accessor for cash attribute.
     * @return cash value, int
     */
    public int getCash() {
        return this.cash;
    }

    /**
     * Accessor to get the number of doubles.
     * @return number of doubles the player has rolled, int
     */
    public int getNumDoubles(){
        return numDoubles;
    }

    /**
     * Method to add a value to the number of doubles.
     */
    public void addNumDoubles(){
        this.numDoubles += 1;
    }

    /**
     * Method to reset number of doubles to 0.
     */
    public void resetNumDoubles(){
        this.numDoubles = 0;
    }

    /**
     * Accessor to determine if the player has another roll.
     * @return if the player has another roll, booleam
     */
    public boolean hasAnotherRoll(){
        return rollAgain;
    }

    /**
     * Accessor to set if the user rolls again.
     * @param rollAgain if the user rolls again, boolean
     */
    public void setRollAgain(boolean rollAgain){
        this.rollAgain = rollAgain;
    }

    /**
     * Accessor to set the rent status of the player (no rent, paid, unpaid)
     * @param feesStatus status to be set, StatusEnum
     */
    public void setFeesStatus(StatusEnum feesStatus) {
        this.feesStatus = feesStatus;
    }

    /**
     * Accessor to get the rent status of the player
     * @return the rent status of the player, StatusEnum
     */
    public StatusEnum getFeesStatus() {
        return feesStatus;
    }

    /**
     * Accessor to get all the player's properties.
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

    /**
     * Method to get all the player's proterties that they are currently allowed to sell.
     * @return
     */
//    public List<Property> getSellableProperties(){
//        ArrayList<Property> sellableProperties = new ArrayList<>();
//        for (Property property: properties){
//            if (!property.getRecentlyChanged()){
//                sellableProperties.add(property);
//            }
//        }
//
//        return sellableProperties;
//    }

    public void setRank (int rank){
        this.rank = rank;
    }

    public int getRank(){
        return rank;
    }

    @Override
    public String toString() {
        String playerInfo;

        playerInfo = "\tbankrupt=" + bankrupt +
                "\n\tname='" + name + '\'' +
                "\n\ticon='" + icon + '\'' +
                "\n\tcash='" + cash + '\'' +
                "\n\tposition='" + position + '\'' +
                "\n\tproperties= { ";

        for (Property property: properties)
        {
            playerInfo += property.toString() + "\n\t";
        }

        playerInfo += " }";

        return playerInfo;
    }
}