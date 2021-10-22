// Bardia Parmoun & Kyra Lothrop

import java.util.ArrayList;
import java.util.List;

public class Player {
    private boolean bankrupt;
    private String name;
    private String icon;
    private int cash;
    private int position;
    private Property currentProperty;
    private List<Property> properties;
    private int numDoubles;
    private boolean rollAgain;
    private boolean hasToPayRent;

    Player(String name, String icon) {
        this.bankrupt = false;
        this.name = name;
        this.icon = icon;
        this.cash = 1500;
        this.position = 0;
        this.properties = new ArrayList<>();
        this.numDoubles = 0;
        this.rollAgain = false;
        this.hasToPayRent = false;
    }

    public int getCash() {
        return this.cash;
    }

    public void pay(int amount){
        this.cash -= amount;
    }

    public void buyProperty(Property property){
        this.properties.add(property);
        this.cash -= property.getPrice();
    }

    public void sellProperty(Property property){
        this.properties.remove(property);
        this.cash += property.getPrice();
    }

    public void setBankrupt(boolean isBankrupt){
        this.bankrupt = isBankrupt;
    }

    public boolean isBankrupt(){
        return this.bankrupt;
    }

    public void getMoney(int rentMoney){
        this.cash += rentMoney;
    }

    public String getName(){
        return name;
    }

    public String getIcon(){
        return icon;
    }

    public void setPosition(int newPosition){
        this.position = newPosition;
    }

    public int getPosition(){
        return position;
    }

    public void setCurrentProperty(Property currentProperty){
        this.currentProperty = currentProperty;
    }

    public Property getCurrentProperty(){
        return currentProperty;
    }

    public int getNumDubbles(){
        return numDoubles;
    }

    public void setNumDubbles(int newNumDubbles){
        this.numDoubles = newNumDubbles;
    }

    public boolean hasAnotherRoll(){
        return rollAgain;
    }

    public void setRollAgain(boolean rollAgain){
        this.rollAgain = rollAgain;
    }

    public void setHasToPayRent(boolean debt){
        this.hasToPayRent = debt;
    }

    public boolean gethasToPayRent(){
        return this.hasToPayRent;
    }

    @Override
    public String toString() {
        return "Player{" +
                "bankrupt=" + bankrupt +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", cash=" + cash + '\'' +
                ", position=" + position + '\'' +
                ", currentProperty=" + currentProperty.getName() + '\'' +
                ", properties=" + properties +
                '}';
    }
}
