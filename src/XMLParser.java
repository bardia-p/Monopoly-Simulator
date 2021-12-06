import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Group 3
 * SYSC 3110 - Milestone 3 XMLParser
 *
 * This document is the XMLParser. This class handles parsing the XML config files of the cells
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 3.0
 *
 */
public class XMLParser extends DefaultHandler {
    /**
     * The name of the object.
     */
    private String name;
    /**
     * The type of the cell.
     */
    private String cellType;
    /**
     * The price of the cell.
     */
    private int price;
    /**
     * The image path of the cell.
     */
    private String imgPath;
    /**
     * The neighborhood of the cell.
     */
    private String neighborhood;
    /**
     * The fees of the cell.
     */
    private ArrayList<Integer> fees;
    /**
     * The model of the cell.
     */
    private final BoardModel model;
    /**
     * The bank player in the model.
     */
    private final Player bank;
    /**
     * Keeps track of the tag that was selected.
     */
    private int attributeSelector;

    /**
     * The constructor for the parser.
     * @param model the board model, BoardModel.
     * @param bank the bank player, Player.
     */
    public XMLParser(BoardModel model, Player bank){
        super();
        this.model = model;
        this.bank = bank;
        this.name = "";
        this.attributeSelector = -1;
    }

    /**
     * Handles the event for reaching the starting tag.
     * @param qName the tag name, String.
     */
    public void startElement(String u, String ln, String qName, Attributes a){
        switch (qName) {
            case "cell" -> {
                this.cellType = "";
                this.price = 0;
                this.imgPath = "";
                this.neighborhood = "";
                this.fees = new ArrayList<>();
            }
            case "type" -> attributeSelector = 0;
            case "name" -> attributeSelector = 1;
            case "price" -> attributeSelector = 2;
            case "fee" -> attributeSelector = 3;
            case "neighbourhood" -> attributeSelector = 4;
            case "image" -> attributeSelector = 5;
            default -> attributeSelector = -1; // The other tags are ignored.
        }
    }

    /**
     * Handles the event for reaching the end tag.
     * @param qName the tag name, String
     */
    public void endElement(String uri, String localname, String qName){
        if (qName.equals("cell")) {
            switch (cellType) {
                case "Go" -> model.addCell(new Go(fees.get(0), imgPath));

                case "Jail" -> model.addCell(new Jail(name, bank, imgPath));

                case "Free Parking" -> model.addCell(new FreeParking(imgPath));

                case "Go To Jail" -> model.addCell(new GoToJail(name, imgPath));

                case "EmptyCell" -> {
                    EmptyCell emptyCell = !imgPath.equals("") ?
                            new EmptyCell(name, imgPath) :
                            new EmptyCell(name);
                    model.addCell(emptyCell);
                }

                case "Utility" -> {
                    Integer[] feeArr = new Integer[fees.size()];
                    feeArr = fees.toArray(feeArr);
                    Utility utility = !imgPath.equals("") ?
                            new Utility(name, price, feeArr, imgPath) :
                            new Utility(name, price, feeArr);
                    model.addCell(utility);
                }

                case "Railroad" -> {
                    Integer[] feeArr = new Integer[fees.size()];
                    feeArr = fees.toArray(feeArr);

                    Railroad railroad = !imgPath.equals("") ?
                            new Railroad(name, price, feeArr, imgPath) :
                            new Railroad(name, price, feeArr);
                    model.addCell(railroad);
                }

                case "Tax" -> {
                    Tax tax = !imgPath.equals("") ? new Tax(name, fees.get(0), bank,  imgPath) :
                            new Tax(name, fees.get(0), bank);
                    model.addCell(tax);
                }

                case "Property" -> {
                    Property.NeighborhoodEnum neighborhoodEnum;

                    switch (neighborhood){
                        case "brown" -> neighborhoodEnum = Property.NeighborhoodEnum.BROWN;
                        case "sky" -> neighborhoodEnum = Property.NeighborhoodEnum.SKY;
                        case "magenta" -> neighborhoodEnum = Property.NeighborhoodEnum.MAGENTA;
                        case "orange" -> neighborhoodEnum = Property.NeighborhoodEnum.ORANGE;
                        case "red" -> neighborhoodEnum = Property.NeighborhoodEnum.RED;
                        case "yellow" -> neighborhoodEnum = Property.NeighborhoodEnum.YELLOW;
                        case "green" -> neighborhoodEnum = Property.NeighborhoodEnum.GREEN;
                        default -> neighborhoodEnum = Property.NeighborhoodEnum.INDIGO;
                    }

                    Integer[] feeArr = new Integer[fees.size()];
                    feeArr = fees.toArray(feeArr);

                    Property property = !imgPath.equals("") ?
                            new Property(name, price, feeArr, neighborhoodEnum, imgPath) :
                            new Property(name, price, feeArr, neighborhoodEnum);
                    model.addCell(property);
                }
            }
        }
    }

    /**
     * Handles reading the data between the tags.
     * @param ch the content of the tag, char[]
     * @param start the beginning index of the array, int
     * @param length the last index of the array, int
     */
    public void characters(char[] ch, int start, int length){
        switch (attributeSelector){
            case 0 -> cellType = new String(ch, start, length);
            case 1 -> name = new String(ch, start, length);
            case 2 -> price = Integer.parseInt(new String(ch, start, length));
            case 3 -> fees.add(Integer.parseInt(new String(ch, start, length)));
            case 4 -> neighborhood = new String(ch, start, length);
            case 5 -> imgPath = new String(ch, start, length);
        }

        attributeSelector = -1;
    }
}
