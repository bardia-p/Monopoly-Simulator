// Sarah Chow 101143033
// Owen VanDusen 101152022

import java.sql.SQLOutput;
import java.util.*;

public class BoardController  {
    private BoardModel boardModel;
    private ArrayList<String>  remainingIcons;

    public BoardController(){
        remainingIcons = new ArrayList<>(Arrays.asList(
                "boot",
                "iron",
                "scottie dog",
                "battleship",
                "racing car",
                "top hat",
                "wheelbarrow",
                "thimble"
        ));
    }

    public void eventListener(BoardEvent e){
        boardModel = (BoardModel) e.getSource();
        if (e.getType() == BoardModel.Status.GET_NUM_PLAYERS){
            getNumPlayers();
        } else if (e.getType() == BoardModel.Status.INITIALIZE_PLAYERS){
            initializePlayers(e.getValue());
        } else if (e.getType() == BoardModel.Status.GET_COMMAND){
            getCommand(e.getPlayer(), e.getCommands());
        }
    }

    private void getNumPlayers(){
        System.out.println("INITIALIZE GAME DATA");
        Scanner scan = new Scanner(System.in);
        System.out.print("How many people will be playing? ");
        int numPlayers = 0;

        String findNumPlayers = scan.nextLine();

        int flag = 1;
        while (flag == 1) {
            try{
                numPlayers = Integer.parseInt(findNumPlayers);
                flag = 0;
            }catch(NumberFormatException e){
                System.out.println("Sorry try again!");
                System.out.print("How many people will be playing? ");
                findNumPlayers = scan.nextLine();
            }

        }

        boardModel.setNumPlayers(numPlayers);
    }

    private void initializePlayers(int numPlayers){
        String icon;

        for (int i = 0; i < numPlayers; i++){
            Scanner scan2 = new Scanner(System.in);
            System.out.printf("What's player %d's name? ", i + 1);
            String name = scan2.nextLine();

            do {
                System.out.printf("Choose an icon <%s>: ", getListOfIconsUpper());
                icon = scan2.nextLine().toLowerCase();
            } while (!(remainingIcons.contains(icon)));

            boardModel.addPlayer(new Player(name, icon));
            remainingIcons.remove(icon);
        }
        System.out.println("-------------------------------------------------------------");
        System.out.println("LETS BEGIN!");

    }

    private String getListOfIconsUpper(){

        String upperCaseIcon = "";

        for (String icon : remainingIcons){
            upperCaseIcon += icon.toUpperCase() + ", ";
        }

        return upperCaseIcon.substring(0, upperCaseIcon.length() - 2);
    }

    private String getListAvailableOfCommands(ArrayList<BoardModel.Command> commands){
        String availableCommands = "";

        for (BoardModel.Command command: commands){
            switch(command) {
                case BUY:
                    availableCommands += "buy, ";
                    break;
                case SELL:
                    availableCommands += "sell, ";
                    break;
                case STATUS:
                    availableCommands += "status, ";
                    break;
                case BOARD_STATUS:
                    availableCommands += "board status, ";
                    break;
                case PASS:
                    availableCommands += "pass, ";
                    break;
                case ROLL_AGAIN:
                    availableCommands += "roll, ";
                    break;
                case PAY_RENT:
                    availableCommands += "pay rent, ";
            }
        }

        return availableCommands.substring(0, availableCommands.length() - 2);
    }

    private void getCommand(Player player, ArrayList<BoardModel.Command> commands){
        System.out.print("Choose one of the following commands: ");
        String availableCommands = getListAvailableOfCommands(commands);
        System.out.println(availableCommands);
        System.out.print("Enter your command: ");

        Scanner scan = new Scanner(System.in);
        String command = scan.nextLine().toLowerCase();

        while (!(availableCommands.contains(command))) {
            System.out.println("Sorry try again!");
            System.out.print("Enter your command: ");
            command = scan.nextLine().toLowerCase();
        }

        if (command.equals("buy")){
            boardModel.buyProperty(player.getCurrentProperty(), player);
        } else if (command.equals("sell")){
            loadSellPropertiesUI(player);
        } else if (command.equals("pass")){
            boardModel.passTurn(player);
        } else if (command.equals("status")){
            boardModel.getStatus(player);
        } else if (command.equals("board status")){
            boardModel.getBoardStatus();
        } else if (command.equals("roll")){
            boardModel.roll(player);
        } else if (command.equals("pay rent")){
            boardModel.payRent(player.getCurrentProperty(), player);
        }
    }

    private void loadSellPropertiesUI(Player player) {
        System.out.println("Here are the list of the properties that you can sell: ");
        String sellableProperties = "";

        for (Property property : player.getSellableProperties()){
            sellableProperties += property.getName() + ", ";
        }

        sellableProperties += "cancel";
        System.out.println(sellableProperties);

        System.out.print("Enter the name of the property: ");

        Scanner scan = new Scanner(System.in);
        String command = scan.nextLine();

        while (!(sellableProperties.contains(command))) {
            System.out.println("Sorry try again!");
            System.out.print("Enter the name of the property: ");
            command = scan.nextLine();
        }

        if (!command.equals("cancel")){
            for (Property property: player.getSellableProperties()){
                if (property.getName().equals(command)){
                    boardModel.sellProperty(property, player);
                    return;
                }
            }
        }
    }
}
