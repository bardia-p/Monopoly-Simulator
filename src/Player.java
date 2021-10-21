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

    Player(String name, String icon) {
        this.bankrupt = false;
        this.name = name;
        this.icon = icon;
        this.cash = 1500;
        this.position = 0;
        this.properties = new ArrayList<>();
    }

    public int getCash() {
        return this.cash;
    }

    public void pay(int propertyAmount){
        this.cash -= propertyAmount;
    }

    public void setBankrupt(boolean isBankrupt){
        this.bankrupt = isBankrupt;
    }

    public boolean getBankrupt(){
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
