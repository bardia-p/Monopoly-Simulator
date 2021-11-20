
// This class would only actually be useful if we had 'get out jail cards'


public class GoToJail extends BoardCell{

    public GoToJail(String name, String imgName) {
        super(name, null, CellType.GO_TO_JAIL, imgName);
    }

    @Override
    public String toString() {
        return "\n\tname='" + super.getName() + '\'';
    }
}
