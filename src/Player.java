// Bardia Parmoun & Kyra Lothrop
import java.util.ArrayList;
import java.util.List;

public class Player {
    private int cash;
    private List<Property> properties;

    Player(){
        this.cash = 1500;
        this.properties = new ArrayList<>();
    }

    public int getCash() {
        return this.cash;
    }

    public void pay(int propertyAmount){
        this.cash -= propertyAmount;
    }
}
