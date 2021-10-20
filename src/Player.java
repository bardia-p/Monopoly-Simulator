// Bardia Parmoun & Kyra Lothrop
import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int cash;
    private List<Property> properties;

    Player(String name) {
        this.name = name;
        this.cash = 1500;
        this.properties = new ArrayList<>();
    }

    public int getCash() {
        return this.cash;
    }

    public void pay (int propertyAmount){
        this.cash -= propertyAmount;
    }

    public String getName(){
        return name;
    }
}
