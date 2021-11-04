public class EmptyCell extends BoardCell {
    public EmptyCell(String name, CellType type, String imgName){
        super(name, null, type, imgName);
    }

    @Override
    public String toString() {
        return "\n\tname='" + super.getName() + "'";
    }
}
