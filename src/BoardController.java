import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Group 3
 * SYSC 3110 - Milestone 1 BoardController Class
 *
 * This document is the BoardController, as per the MVC model, the BoardController handles user input and passes off
 * the information to the BoardModel class. This class has a boardModel and an array list of remaining icons.
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 1.0
 */
public class BoardController  implements ActionListener {
    /**
     * Keeps track of the board model.
     */
    private BoardModel boardModel;

    public static final int NUM_ICONS = 8;

    /**
     * Constructor for the BoardController, populates the array list with all possible icons.
     * @author Sarah Chow 101143033
     * @author Kyra Lothrop 101145872
     * @author Bardia Parmoun 101143006
     * @author Owen VanDusen 101152022
     */
    public BoardController(BoardModel bm){
        boardModel = bm;
    }

    /**
     * Calls a different method depending on which board event was passed to the method
     * @author Bardia Parmoun 101143006
     * @param e a board event for GET_NUM_PLAYERS, INITIALIZE_PLAYERS, or GET_COMMAND, BoardEvent
     */
    public void eventListener(BoardEvent e){
        boardModel = (BoardModel) e.getSource();
        switch (e.getType()) {
            case GET_NUM_PLAYERS -> getNumPlayers();
            case INITIALIZE_PLAYERS -> initializePlayers(e.getValue());
            case GET_COMMAND -> getCommand(e.getPlayer(), (ArrayList<BoardModel.Command>) e.getCommands());
        }
    }

    /**
     * UI method to prompt for number of users playing the game, stores value as int to be used
     * throughout the game.
     * @author Owen VanDusen 101152022
     */
    private void getNumPlayers(){
        System.out.println("INITIALIZE GAME DATA");
        Scanner scan = new Scanner(System.in);
        System.out.print("How many people will be playing? ");
        int numPlayers = 0;

        String findNumPlayers = scan.nextLine();

        int flag = 1;
        while (flag == 1) {
            try {
                numPlayers = Integer.parseInt(findNumPlayers);
                if (numPlayers > 1 && numPlayers < NUM_ICONS + 1){
                    flag = 0;
                }
            } catch(NumberFormatException e){
                flag = 1;
            }

            if (flag == 1){
                System.out.println("Sorry try again!");
                System.out.printf("The number of players should be an integer between 1 and less than %d\n",
                        NUM_ICONS + 1);
                System.out.print("How many people will be playing? ");
                findNumPlayers = scan.nextLine();
            }
        }

        boardModel.setNumPlayers(numPlayers);
    }

    /**
     * UI method to initialize players with their respective name and icon.
     * @author Kyra Lothrop 101145872
     * @param numPlayers number of players participating, int
     */
    private void initializePlayers(int numPlayers){
        String icon;

        for (int i = 0; i < numPlayers; i++){
            Scanner scan2 = new Scanner(System.in);
            System.out.printf("What's player %d's name? ", i + 1);
            String name = scan2.nextLine();

            String remainingIcons = getListOfIconsUpper();

            do {
                System.out.printf("Choose an icon <%s>: ", remainingIcons);
                icon = scan2.nextLine().toUpperCase();
            } while (!(remainingIcons.contains(icon)));

            BoardModel.Icon playerIcon = findPlayerIcon(icon.toLowerCase());
            boardModel.addPlayer(new Player(name, playerIcon));
        }
        System.out.println("-------------------------------------------------------------");
        System.out.println("LETS BEGIN!");

    }

    private BoardModel.Icon findPlayerIcon(String icon) {
        for (BoardModel.Icon ic: BoardModel.Icon.values()){
            if (ic.getName().equals(icon)) {
                ic.setUsed();
                return ic;
            }
        }
        return null;
    }

    /**
     * Displays list of available icons in uppercase letters, looks nice on UI.
     * @author Sarah Chow 101143033
     * @return the remaining icons in uppercase form, String
     */
    private String getListOfIconsUpper(){

        String upperCaseIcon = "";

        for (BoardModel.Icon icon: BoardModel.Icon.values()){
            if (!icon.getUsed()) {
                upperCaseIcon += icon.getName().toUpperCase() + ", ";
            }
        }

        return upperCaseIcon.substring(0, upperCaseIcon.length() - 2);
    }

    /**
     * Displays a list of available commands to the players
     * @author Sarah Chow 101143033
     * @param commands possible actions the user can take in their current position, ArrayList<BoardModel.Command>
     * @return the list of commands in a string format for displaying
     */
    private String getListAvailableOfCommands(ArrayList<BoardModel.Command> commands){
        String availableCommands = "";

        for (BoardModel.Command command: commands){
            availableCommands += command.getStringCommand() + ", ";
        }

        return availableCommands.substring(0, availableCommands.length() - 2);
    }

    /**
     * Takes the desired command from the player as a text input.
     * @author Sarah Chow 101143033
     * @param player the current player, Player
     * @param commands possible actions the user can take in their current position, ArrayList<BoardModel.Command>
     */
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

        if (command.equals(BoardModel.Command.BUY.getStringCommand())){
            boardModel.buyProperty((Property) player.getCurrentCell(), player);
        } else if (command.equals(BoardModel.Command.SELL.getStringCommand())){
            loadSellPropertiesUI(player);
        } else if (command.equals(BoardModel.Command.PASS.getStringCommand())){
            boardModel.passTurn(player);
        } else if (command.equals(BoardModel.Command.STATUS.getStringCommand())){
            boardModel.getStatus(player);
        } else if (command.equals(BoardModel.Command.BOARD_STATUS.getStringCommand())) {
            boardModel.getBoardStatus();
        } else if (command.equals(BoardModel.Command.CELL_STATUS.getStringCommand())){
            boardModel.getCellStatus();
        } else if (command.equals(BoardModel.Command.ROLL_AGAIN.getStringCommand())){
            boardModel.roll(player);
        } else if (command.equals(BoardModel.Command.PAY_RENT.getStringCommand())) {
            boardModel.payFees(player.getCurrentCell(), player);
        } else if (command.equals(BoardModel.Command.PAY_TAX.getStringCommand())){
            boardModel.payFees(player.getCurrentCell(), player);
        } else if (command.equals(BoardModel.Command.FORFEIT.getStringCommand())){
            boardModel.forfeit(player);
        }
    }

    /**
     * Create a UI to display owned properties to the owner and present the chance to sell them.
     * @author Bardia Parmoun 101143006
     * @param player the player who is giving inputs, Player
     */
    private void loadSellPropertiesUI(Player player) {
        System.out.println("Here are the list of the properties that you can sell: ");
        System.out.println("Note: if you would no longer like to sell, use 'cancel'");
        String sellableProperties = "";

        for (Property property : player.getProperties(true)){
            sellableProperties += property.getName() + ", ";
        }

        sellableProperties += "cancel";
        System.out.println(sellableProperties);

        System.out.print("Enter the name of the property: ");

        Scanner scan = new Scanner(System.in);
        String command = scan.nextLine().toLowerCase();

        while (!(sellableProperties.toLowerCase().contains(command))) {
            System.out.println("Sorry try again!");
            System.out.print("Enter the name of the property: ");
            command = scan.nextLine().toLowerCase();
        }

        if (!command.equals("cancel")){
            for (Property property: player.getProperties(true)){
                if (property.getName().toLowerCase().equals(command)){
                    boardModel.sellProperty(property, player);
                    return;
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        boardModel.sendCommand(command);
    }
}