// Sarah Chow 101143033
// Owen VanDusen 101152022

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
        Scanner scan = new Scanner(System.in);
        System.out.println("How many people will be playing?");
        int numPlayers = scan.nextInt();
        boardModel.setNumPlayers(numPlayers);
    }

    private void initializePlayers(int numPlayers){
        String icon;

        for (int i = 0; i < numPlayers; i++){
            Scanner scan2 = new Scanner(System.in);
            System.out.printf("What's player %d's name?\n", i + 1);
            String name = scan2.nextLine();

            do {
                Scanner scan3 = new Scanner(System.in);
                System.out.printf("Choose an icon: %s\n", getListOfIconsUpper());
                icon = scan2.nextLine().toLowerCase();
            } while (!(remainingIcons.contains(icon)));

            boardModel.addPlayer(new Player(name, icon));
            remainingIcons.remove(icon);
        }

    }

    private String getListOfIconsUpper(){

        String upperCaseIcon = "";

        for (String icon : remainingIcons){
            upperCaseIcon += icon.toUpperCase() + ", ";
        }
        return upperCaseIcon;
    }

    private String getListAvailableOfCommands(ArrayList<BoardModel.Command> commands){
        String availableCommands = "";

        for (BoardModel.Command command: commands){
            if(command == BoardModel.Command.BUY){
                availableCommands += "buy, ";
            } if(command == BoardModel.Command.SELL){
                availableCommands += "sell, ";
            } if(command == BoardModel.Command.STATUS){
                availableCommands += "status, ";
            } if(command == BoardModel.Command.PASS){
                availableCommands += "pass, ";
            } if(command == BoardModel.Command.ROLL_AGAIN){
                availableCommands += "roll, ";
            }
        }
        return availableCommands;
    }

    private void getCommand(Player player, ArrayList<BoardModel.Command> commands){
        System.out.println("Here is a list of the available commands: ");
        String availableCommands = getListAvailableOfCommands(commands);
        System.out.println(availableCommands);

        Scanner scan = new Scanner(System.in);
        String command = scan.nextLine().toLowerCase();

        while (!(availableCommands.contains(command))) {
            System.out.println("Sorry try again!");
            command = scan.nextLine().toLowerCase();
        }

        if (command.equals("buy")){
            boardModel.buyProperty(player.getCurrentProperty(), player);
        } else if (command.equals("sell")){
            boardModel.sellProperty(player.getCurrentProperty(), player);
        } else if (command.equals("pass")){
            boardModel.passTurn();
        } else if (command.equals("status")){
            boardModel.getStatus(player);
        } else if (command.equals("roll")){
            boardModel.roll(player);
        }
    }

    private void getPlayerStatus(Player player){
        System.out.println(player);
    }

}
