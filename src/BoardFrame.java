import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Group 3
 * SYSC 3110 - Milestone 1 BoardFrame Class
 *
 * This document is the BoardFrame. This class has the BoardModel and BoardController. BoardFrame
 * handles outputting information for the user to see and formatting that output.
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 1.0
 */
public class BoardFrame extends JFrame implements BoardView  {
    /**
     * Keeps track of the board model.
     */
    BoardModel model;
    /**
     * Keeps track of the board controller.
     */
    BoardController controller;
    /**
     * Keeps track of all the panels for the individual board cells.
     */
    private List<JPanel> boardCells;
    /**
     * List of command buttons.
     */
    private List<JButton> commandButtons;
    /**
     * Keeps track of all the player JLabels.
     */
    private Map<Player, JLabel> playerLabels;
    /**
     * The main panel that keeps track of all the cells.
     */
    private JPanel mainPanel;
    /**
     * The main pane that includes the cells, the players, the status windows, etc
     */
    private JLayeredPane layeredPane;
    /**
     * Keeps track of the size of the board on each side.
     */
    private static final int SIZE = 11;
    /**
     * Keeps track of the size of the window width.
     */
    private static final int WINDOW_WIDTH = 750;
    /**
     * Keeps track of the window height.
     */
    private static final int WINDOW_HEIGHT = 800;
    /**
     * Keeps track of the size of the board width.
     */
    private static final int BOARD_WIDTH = 750;
    /**
     * Keeps track of the board height.
     */
    private static final int BOARD_HEIGHT = 750;
    /**
     * The background color of the board.
     */
    private static final String BACKGROUND_COLOR = "#cbe4d0";
    /**
     * Keeps track of how much the board should be shifted down based on the buttons above it.
     */
    private static final int BOARD_SHIFT_Y = 60;
    /**
     * The size of the icon.
     */
    private static final int ICON_SIZE = 40;
    /**
     * The size of the die.
     */
    private static final int DIE_SIZE = 50;
    /**
     * The height of the command panel.
     */
    private static final int COMMAND_HEIGHT = 50;
    /**
     * Shifting the command panel.
     */
    private static final int COMMAND_SHIFT_Y = 10;
    /**
     * The maximum number of rolls in the dice animation.
     */
    private static final int MAXRANDOMROLLS = 10;

    /**
     * Constructor for the Board listener, creates the board model, adds the board listener to the board model,
     * creates the board controller and runs the play method.
     * @author Sarah Chow 101143033
     * @author Kyra Lothrop 101145872
     * @author Bardia Parmoun 101143006
     * @author Owen VanDusen 10115202
     */
    public BoardFrame(){
        super("Rich Uncle Pennybags!");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);

        // The layered pane that includes all the components.
        layeredPane = new JLayeredPane();
        layeredPane.setOpaque(true);
        getContentPane().add(layeredPane);
        layeredPane.setPreferredSize(new Dimension(WINDOW_WIDTH,WINDOW_HEIGHT));

        // The main panel that keeps track of all the cells
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.decode(BACKGROUND_COLOR));
        mainPanel.setBounds(0, BOARD_SHIFT_Y, BOARD_WIDTH, BOARD_HEIGHT);

        layeredPane.add(mainPanel,0);

        // Keeps track of the player labels and cell panels.
        playerLabels = new HashMap<>();
        boardCells = new ArrayList<JPanel>();

        // Adding the frame to the model.
        model = new BoardModel();
        model.addBoardView(this);

        controller = new BoardController(model);

        commandButtons = new ArrayList<>();

        this.pack();
        this.setVisible(true);

        model.play();

    }

    /**
     * Passes a BoardEvent to the BoardController to be interpreted.
     * @author Bardia Parmoun 101143006
     * @param e the board event, BoardEvent
     */
    @Override
    public void handleBoardUpdate(BoardEvent e) {
        switch (e.getType()) {
            case PLAYER_ROLL -> handleRoll(e.getDice(), e.getPlayer());
            case BUY -> handleBuyProperty(e.getPlayer(), (Property) e.getBoardCell(), e.getResult());
            case SELL -> handleSellProperty(e.getPlayer());
            case PLAYER_STATUS -> handleGetPlayerStatus(e.getPlayer());
            case BOARD_STATUS -> handleGetBoardStatus(e.getPlayers());
            case CELL_STATUS -> handleGetCellStatus(e.getPlayer().getCurrentCell());
            case PLAYER_DOUBLE_ROLL -> handleRollingDoubles(e.getPlayer());
            case INITIALIZE_MONOPOLY -> handleWelcomeMonopoly();
            case PAY_FEES -> handlePayFees(e.getPlayer().getCurrentCell(), e.getPlayer(), e.getValue(), e.getResult());
            case PASS_TURN -> handleCurrentPlayerChange();
            case GAME_OVER -> handleWinner(e.getPlayers());
            case INITIALIZE_BOARD -> constructBoard(e.getCells());
            case CREATE_PLAYER_ICONS -> createPlayerLabels((ArrayList<Player>) e.getPlayers());
            case GET_NUM_PLAYERS -> getNumPlayers();        //new
            case INITIALIZE_PLAYERS -> initializePlayers(e.getValue());     //new
            case GET_COMMAND -> updateAvailableCommands(e.getPlayer(), (ArrayList<BoardModel.Command>) e.getCommands());     //new
            case PLAYER_MOVE -> handlePlayerGUIMove(e.getPlayer(), e.getValue(), e.getValue2());
            case REPAINT_BOARD -> handleRepaintBoard();
            case PLAYER_FORFEIT -> handleForfeitedPlayer(e.getPlayer());
            case PLAYER_REQUEST_FORFEIT -> handleRequestForfeit(e.getPlayer());
        }
    }

    /**
     * Repaints the board when the window has changed to make sure everything is visible.
     * @author Bardia Parmoun 101143006
     */
    private void handleRepaintBoard() {
        for (JLabel label: playerLabels.values()) {
            layeredPane.moveToFront(label);
        }
    }

    /**
     * Handles moving players cell by cell.
     * @author Sarah Chow 101143033
     * @param player the player that is moving, Player
     * @param amountToMove the amount that a player should move, int
     * @param originalPos the original position of the player, int
     */
    private void handlePlayerGUIMove(Player player, int amountToMove, int originalPos){
        for (int i = originalPos; i < originalPos + amountToMove + 1; i++){
            int newPlayerPosition =  i % BoardModel.SIZE_OF_BOARD;
            showCurrentCell(player, newPlayerPosition);
            try{
                TimeUnit.MILLISECONDS.sleep(50);
            }
            catch(Exception e){
                System.out.println("wait failed");
            }
        }
    }

    /**
     * Creates the player labels and icons.
     * @author Bardia Parmoun 101143006
     * @param players the list of the players to make icons for, List<Player>
     */
    private void createPlayerLabels(ArrayList<Player> players) {
        for (Player p: players){
            JLabel playerLabel = new JLabel();
            playerLabels.put(p, playerLabel);
            layeredPane.add(playerLabel);
        }
    }

    /**
     * Method to animate the rolling of random dice numbers and to visually land on a value.
     * @author Sarah Chow 101143033
     * @param randomRoll value to land on, int
     * @param dice1 whether the die should be offset, boolean
     */
    private void buildDiceDisplay(int randomRoll, boolean dice1){
        int setLocationX = 8;
        int setLocationY = 8;

        if (dice1){
            setLocationX -= 1; // Make second dice off center
        }

        try {
            BufferedImage dieImage1 = ImageIO.read(getClass().getResource("images/dice/dice1.png"));
            BufferedImage dieImage2 = ImageIO.read(getClass().getResource("images/dice/dice2.png"));
            BufferedImage dieImage3 = ImageIO.read(getClass().getResource("images/dice/dice3.png"));
            BufferedImage dieImage4 = ImageIO.read(getClass().getResource("images/dice/dice4.png"));
            BufferedImage dieImage5 = ImageIO.read(getClass().getResource("images/dice/dice5.png"));
            BufferedImage dieImage6 = ImageIO.read(getClass().getResource("images/dice/dice6.png"));

            BufferedImage currImage = dieImage1;

            switch (randomRoll) {
                case (1):
                    currImage = dieImage1;
                    break;
                case (2):
                    currImage = dieImage2;
                    break;
                case (3):
                    currImage = dieImage3;
                    break;
                case (4):
                    currImage = dieImage4;
                    break;
                case (5):
                    currImage = dieImage5;
                    break;
                case (6):
                    currImage = dieImage6;
                    break;
                default:
                    break;
            }

            Image newImage = currImage.getScaledInstance(DIE_SIZE, DIE_SIZE, Image.SCALE_DEFAULT);
            JLabel label = new JLabel(new ImageIcon(newImage));

            GridBagConstraints c = new GridBagConstraints();
            c.gridx = setLocationX;
            c.gridy = setLocationY;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.anchor = GridBagConstraints.NORTH;

            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(Color.decode(BACKGROUND_COLOR));

            panel.add(label);
            boardCells.add(panel);

            mainPanel.add(panel, c);
        }
        catch(Exception e){
            System.out.println("build dice display failed");
        }
    }

    /**
     * Passes the values of both dice and the player to the BoardController to be interpreted.
     * @author Sarah Chow 101143033
     * @param dice value of the dice, int[]
     * @param player player performing actions, Player
     */
    private void handleRoll(int[] dice, Player player) {
        int die1 = dice[0];
        int die2 = dice[1];

        for (int i = 0; i < MAXRANDOMROLLS + 1; i++) {
            Random rand = new Random();
            int randomRoll1;
            int randomRoll2;
            randomRoll1 = rand.nextInt((6 - 1) + 1) + 1;
            randomRoll2 = rand.nextInt((6 - 1) + 1) + 1;

            if (i == MAXRANDOMROLLS) {
                randomRoll1 = die1;
                randomRoll2 = die2;
            }

            buildDiceDisplay(randomRoll1, true);
            buildDiceDisplay(randomRoll2, false);

            this.pack();

            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (Exception e) {
                System.out.println("wait failed");
            }

        }

    }

    /**
     * Displays a prompt whenever a player rolls the same number on both dice.
     * @author Bardia Parmoun 101143006
     * @param player player performing actions, Player
     */
    private void handleRollingDoubles(Player player){
        System.out.printf("Player %s rolled a double\n", player.getIconName());
    }

    /**
     * Enables the command buttons based on what the user is allowed to do
     * @author Kyra Lothrop 101145872
     * @param player
     * @param commands
     */
    private void updateAvailableCommands(Player player, ArrayList<BoardModel.Command> commands){
        String availableCommands = "";

        for (BoardModel.Command command: commands){
            availableCommands += command.getStringCommand() + ", ";
        }

        availableCommands = availableCommands.substring(0, availableCommands.length() - 2);

        for(JButton b: commandButtons){
            if(availableCommands.contains(b.getText().toLowerCase())){
                b.setEnabled(true);
            }else{
                b.setEnabled(false);
            }
        }
    }

    /**
     * Get the number of players from the user
     * @author Kyra Lothrop 101145872
     */
    private void getNumPlayers(){
        Integer[] numPlayerOptions = {2,3,4,5,6,7,8};
        int numPlayers = (Integer)JOptionPane.showInputDialog(null,
                "How many people will be playing?", "INITIALIZE GAME DATA",
                JOptionPane.QUESTION_MESSAGE, null, numPlayerOptions, numPlayerOptions[0]);
        model.setNumPlayers(numPlayers);
    }

    /**
     * Initialize the player names and icons based on user input
     * @author Kyra Lothrop 101145872
     * @param numPlayers
     */
    private void initializePlayers(int numPlayers){
        for (int i = 0; i < numPlayers; i++){
            JTextField playerName = new JTextField();
            Object[] message = {
                    "What's player "+ (i+1) + "'s name?", playerName,
            };
            JOptionPane.showConfirmDialog(null, message, "Initialize player "+ (i+1), JOptionPane.OK_CANCEL_OPTION);

            Object[] iconOptions = getListOfIconsUpper();
            String playerIcon = (String) JOptionPane.showInputDialog(null, "Select player " + (i+1) + " icon" , "Select Icon",
                    JOptionPane.QUESTION_MESSAGE, null, iconOptions, iconOptions[0]);
            model.setNumPlayers(numPlayers);

            model.addPlayer(new Player(playerName.getText(), findPlayerIcon(playerIcon.toLowerCase())));

        }
    }

    /**
     * Displays list of available icons in uppercase letters, looks nice on UI.
     * @author Sarah Chow 101143033
     * @return the remaining icons in uppercase form, String
     */
    private Object[] getListOfIconsUpper(){
        List<String> iconOptions = new ArrayList<>();

        for (BoardModel.Icon icon: BoardModel.Icon.values()){
            if (!icon.getUsed()) {
                iconOptions.add(icon.getName().toUpperCase());
            }
        }

        return iconOptions.toArray();
    }

    // moved from BoardFrame to BoardController+++++++++++++++++
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
     * Constructs the board from the list of the board cells.
     * @author Bardia Parmoun 101143006
     * @author Kyra Lothrop 101145872
     * @param cells the list of the board cells, List<BoardCell>
     */
    private void constructBoard(List<BoardCell> cells) {
        // Command buttons
        JPanel commandsPanel = new JPanel(new GridLayout(2,4, 10, 10));
        commandsPanel.setBounds(0, COMMAND_SHIFT_Y,BOARD_WIDTH, COMMAND_HEIGHT);

        String[] buttonsText = {"Roll", "Pass", "Forfeit", "Buy", "Sell", "Pay Rent", "Pay Tax", "Player Status", "Cell Status"};

        for(int i = 0; i<buttonsText.length; i++){
            JButton commandButton = new JButton(buttonsText[i]);
            commandButton.addActionListener(controller);
            commandButtons.add(commandButton);
            commandsPanel.add(commandButton);

            if (buttonsText[i].equals("Roll")){
                commandButton.setActionCommand(BoardModel.Command.ROLL_AGAIN.getStringCommand());
            }
            else if(buttonsText[i].equals("Pass")){
                commandButton.setActionCommand(BoardModel.Command.PASS.getStringCommand());
            }
            else if(buttonsText[i].equals("Forfeit")){
                commandButton.setActionCommand(BoardModel.Command.FORFEIT.getStringCommand());
            }
            else if(buttonsText[i].equals("Buy")){
                commandButton.setActionCommand(BoardModel.Command.BUY.getStringCommand());
            }
            else if(buttonsText[i].equals("Sell")){
                commandButton.setActionCommand(BoardModel.Command.SELL.getStringCommand());
            }
            else if(buttonsText[i].equals("Pay Rent")){
                commandButton.setActionCommand(BoardModel.Command.PAY_RENT.getStringCommand());
            }
            else if(buttonsText[i].equals("Pay Tax")){
                commandButton.setActionCommand(BoardModel.Command.PAY_TAX.getStringCommand());
            }
            else if (buttonsText[i].equals("Player Status")){
                commandButton.setActionCommand(BoardModel.Command.PLAYER_STATUS.getStringCommand());
            }
            else if (buttonsText[i].equals("Cell Status")){
                commandButton.setActionCommand(BoardModel.Command.CELL_STATUS.getStringCommand());
            }

            commandButton.setEnabled(false);
        }
        layeredPane.add(commandsPanel);

        // Constructing the panels
        int row = SIZE - 1;
        int col = SIZE - 1;
        int row_step = 0;
        int col_step = -1;
        int direction = GridBagConstraints.WEST;

        for (BoardCell cell: cells) {
            try{
                // Changes the row and col if it hits corners.
                if (cell.getName().equals("JAIL")){
                    row = SIZE- 1;
                    col = 0;
                    row_step = -1;
                    col_step = 0;
                    direction = GridBagConstraints.NORTH;
                } else if (cell.getName().equals("FREE PARKING")) {
                    row = 0;
                    col = 0;
                    row_step = 0;
                    col_step = 1;
                    direction = GridBagConstraints.EAST;
                } else if (cell.getName().equals("GO TO JAIL")) {
                    row = 0;
                    col = SIZE - 1;
                    row_step = 1;
                    col_step = 0;
                    direction = GridBagConstraints.SOUTH;
                }
                // Loads the cell image.
                BufferedImage image = ImageIO.read(getClass().getResource(cell.getImgPath()));
                JLabel label = new JLabel(new ImageIcon(image));
                // Finds the position of the cell.
                GridBagConstraints c = new GridBagConstraints();
                c.gridx = col;
                c.gridy = row;
                c.fill = GridBagConstraints.HORIZONTAL;
                c.anchor = direction;
                c.insets = new Insets(0,0,0,0);
                // Places the cell in a panel.
                JPanel panel = new JPanel(new BorderLayout());
                panel.setBackground(Color.decode(BACKGROUND_COLOR));
                // Adds the panel to the board and list of panels.
                panel.add(label);
                boardCells.add(panel);

                mainPanel.add(panel, c);

                row += row_step;
                col += col_step;
            } catch (IOException e) {
                System.out.println(e);
                System.out.println("Could not find the image!");;
            }
        }

        // adding all the components to the frame
        this.pack();
    }


    /**
     * Displays the name of the property the current player is standing on in the terminal.
     * @author Sarah Chow 101143033
     * @param player player performing actions, Player
     */
    private void showCurrentCell(Player player, int cellIndex){
        if (!player.getIconImgPath().equals("")) {
            try {
                BufferedImage image = ImageIO.read(getClass().getResource(player.getIconImgPath()));
                Image dimg = image.getScaledInstance(ICON_SIZE,ICON_SIZE, Image.SCALE_SMOOTH);
                playerLabels.get(player).setIcon(new ImageIcon(dimg));

                JPanel currentCell = boardCells.get(cellIndex);

                Rectangle cellPosition = currentCell.getBounds();

                int index = (int) new ArrayList(playerLabels.keySet()).indexOf(player);
                int x = cellPosition.x + 3*cellPosition.width/4 - 30 - 5*index;
                int y = cellPosition.y + 1*cellPosition.height/2 - 30 - 5*index + BOARD_SHIFT_Y;

                playerLabels.get(player).setBounds(x, y, 50, 50);
                layeredPane.moveToFront(playerLabels.get(player));

                this.pack();
            } catch (IOException e) {
                System.out.println("Could not load the player icon image");
            }
        }
    }

    /**
     * Displays whether the current player can afford the property they attempted to buy or not.
     * @author Owen VanDusen 101152022
     * @param player player performing actions, Player
     * @param property property that is in contention for purchasing, Property
     * @param result if the player can afford the property, boolean
     */
    private void handleBuyProperty(Player player, Property property, boolean result) {
        String buyMessage = "";
        if (result){
            buyMessage = "Player " + player.getIconName().toUpperCase() + " bought " + property.getName();
        } else {
            buyMessage = "Player " + player.getIconName().toUpperCase() + " cannot afford " + property.getName();
        }
        JOptionPane.showMessageDialog(null, buyMessage, "BUY PROPERTY", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Displays whether the current player can sell the property they attempted to sell.
     * @author Sarah Chow 101143033
     * @param player player performing actions, Player
     */
    private void handleSellProperty(Player player) {
        JPanel panel = new JPanel();
        ButtonGroup group = new ButtonGroup();

        for (Property p : player.getProperties(true)) {

            JRadioButton button = new JRadioButton(p.getName().toUpperCase());

            String message = "<html>";

            for (String key : p.getAttributes().keySet()) {
                message += key + p.getAttributes().get(key) + "<br/>";
            }

            message += "</html>";
            JLabel des = new JLabel(message);

            button.setActionCommand(p.getName());
            group.add(button);

            des.setVisible(true);
            panel.add(button);
            panel.add(des, BorderLayout.EAST);
        }

        panel.setPreferredSize(new Dimension(400, 400));


        int ans = JOptionPane.showConfirmDialog(null, panel,
                "SELL PROPERTY", JOptionPane.CANCEL_OPTION);
        if (ans == JOptionPane.OK_OPTION){
            for (Property p : player.getProperties(true)){ //************* theres definitely a way to do this without iterating over the list lol
                if (group.getSelection().getActionCommand().equals(p.getName())){
                    player.toggleConfirmSell();
                    player.setPropertyToSell(p);
                    JOptionPane.showMessageDialog(null, "Player " +
                            player.getIconName().toUpperCase() + " sold " + p.getName().toUpperCase());
                }
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "Sell cancelled!");
        }
    }

    /**
     * Displays the attributes of the object.
     * @author Sarah Chow 101143033
     * @param title JPanel title, String
     * @param attributes attributes of the object, Map
     */
    private void displayObjectAttributes(String title, Map<String, String> attributes, boolean player){
        int count = 0;
        String message = "";

        for (String key : attributes.keySet()){
            if (player && count > 4){
                message += "\t";
            }
            message += key + attributes.get(key) + "\n";
            count++;
        }
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Displays the status of the current player.
     * @author Sarah Chow 101143033
     * @param player player performing actions, Player
     */
    private void handleGetPlayerStatus(Player player) {
        displayObjectAttributes("PLAYER STATUS", player.getAttributes(), true);
    }

    /**
     * Displays the information of the property the player is currently on.
     * @author Sarah Chow 101143033
     * @param currentCell property the player is on, BoardCell
     */
    private void handleGetCellStatus(BoardCell currentCell){
        displayObjectAttributes("CELL STATUS", currentCell.getAttributes(), false);
    }


    /**
     * Displays the status of the current board.
     * @author Kyra Lothrop 101145872
     * @param players player performing actions, Player
     */
    private void handleGetBoardStatus(List<Player> players) {
        System.out.println("\nDisplaying the status of the board:");

        List<Player> bankruptPlayers = new ArrayList<>();
        List<Player> nonBankruptPlayers = new ArrayList<>();

        for (Player player: players){
            if(player.isBankrupt()){
                bankruptPlayers.add(player);
            }
            else if (!player.isBankrupt()){
                nonBankruptPlayers.add(player);
            }
        }

        System.out.println("\nBankrupt Players:");
        for (Player bankruptPlayer: bankruptPlayers){
            System.out.printf("\tPlayer %s, $%d\n", bankruptPlayer.getIconName().toUpperCase(), bankruptPlayer.getCash());
        }

        System.out.println("\nNon Bankrupt Players:");
        for (Player nonBankruptPlayer: nonBankruptPlayers){
            System.out.printf("\tPlayer %s, $%d\n", nonBankruptPlayer.getIconName().toUpperCase(),
                    nonBankruptPlayer.getCash());
        }
        System.out.println("\n");
    }


    /**
     * Displays the introduction message.
     * @author Sarah Chow 101143033
     */
    private void handleWelcomeMonopoly() {
        String welcomeMessage = "In this interactive business game you will try to outwit your\n" +
                "opponents by making them go bankrupt while purchasing properties\n" +
                "around the board. Spend wisely and aim for a TOTAL MONOPOLY.";
        JOptionPane.showMessageDialog(null, welcomeMessage, "WELCOME TO MONOPOLY",
                JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Displays the status of a player trying to pay their fees.
     * @author Kyra Lothrop 101145872
     * @param boardCell property being rented, BoardCell
     * @param player player paying rent, Player
     * @param result if the player can afford rent, boolean
     */
    private void handlePayFees(BoardCell boardCell, Player player, int fees,  boolean result) {
        String feesMessage = "";
        if (result){
            feesMessage = "You have successfully paid $" + fees + " to " +
                    boardCell.getOwner().getIconName().toUpperCase();
        }
        else{
            feesMessage = "You cannot currently pay fees to " + boardCell.getOwner().getIconName()
                    + ". You must pay $" + fees + " and you only have $" + player.getCash() + " remaining";
        }
        JOptionPane.showMessageDialog(null, feesMessage, "PAY FEES", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Displays a breaker to indicate change of turn.
     * @author Owen VanDusen 101152022
     */
    private void handleCurrentPlayerChange() {
        for(JButton b: commandButtons){
            b.setEnabled(false);
        }
        System.out.println("\n+++++++++Passing Dice To Next Player+++++++++");
    }

    /**
     * Confirmation message that the player would like to forfeit the game.
     * @author Sarah Chow 101143033
     * @param player player forfeiting, Player
     */
    private void handleRequestForfeit(Player player) {
        int ans = JOptionPane.showConfirmDialog(null, "Are you sure you would like to forfeit the game?");
        if (ans == JOptionPane.YES_OPTION){
            player.toggleRequest_forfeit();
        }
        else{
            JOptionPane.showMessageDialog(null, "Forfeit request cancelled!");
        }
    }

    /**
     * Displays a message informing that a player chose to forfeit the game.
     * @author Sarah Chow 101143033
     * @param player player forfeiting, Player
     */
    private void handleForfeitedPlayer(Player player){
        String message = "Player: " + player.getIconName().toUpperCase() + " has forfeited the game!";
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Displays the end of game statistics and the ordered rank of all players.
     * @author Bardia Parmoun 101143006
     * @param players list of players in the order they forfeit the game, List<Player>
     */
    private void handleWinner(List<Player> players) {
        String gameOverMessage = "";
        players.sort(Comparator.comparingInt(Player::getRank));

        gameOverMessage += "Player " + players.get(0).getIconName().toUpperCase() + " won the game!\n";
        for (Player player: players){
            if (player.isBankrupt()) {
                gameOverMessage += "Player " + player.getIconName().toUpperCase() +
                        " has the rank " + player.getRank() + "\n";
            }
        }
        JOptionPane.showMessageDialog(null, gameOverMessage, "GAME OVER!", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Start of the program
     * @author Kyra Lothrop 101145872
     * @param args args
     */
    public static void main(String[] args) {
        new BoardFrame();
    }
}