import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Group 3
 * SYSC 3110 - Milestone 3 BoardFrame Class
 *
 * This document is the BoardFrame. This class has the BoardModel and BoardController. BoardFrame
 * handles outputting information for the user to see and formatting that output.
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 3.0
 */
public class BoardFrame extends JFrame implements BoardView  {
    /**
     * Keeps track of the board model.
     */
    private final BoardModel model;
    /**
     * Keeps track of the board controller.
     */
    private final BoardController controller;
    /**
     * Keeps track of all the panels for the individual board cells.
     */
    private final List<JPanel> boardCells;
    /**
     * List of command buttons.
     */
    private final List<JButton> commandButtons;
    /**
     * Keeps track of all the house labels
     */
    private Map<BoardCell,List<JLabel>> houseLabels;
    /**
     * Keeps track of all the player JLabels.
     */
    private final Map<Player, JLabel> playerLabels;
    /**
     * Keeps track of all the player status panels.
     */
    private final Map<Player, JPanel> playerStatusPanels;
    /**
     * The main panel that keeps track of all the cells.
     */
    private JPanel mainPanel, logoPanel;
    /**
     * The main pane that includes the cells, the players, the status windows, etc
     */
    private JLayeredPane layeredPane;
    /**
     * Keeps track of the dice panels.
     */
    private JPanel[] dicePanels;
    /**
     * Keeps track of the turn label.
     */
    private JLabel statusLabel;

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem newGame, loadGame, saveGame;

    /**
     * Keeps track of the size of the board on each side.
     */
    private static final int SIZE = 11;
    /**
     * Keeps track of the size of the window width.
     */
    private static final int WINDOW_WIDTH = 1100;
    /**
     * Keeps track of the window height.
     */
    private static final int WINDOW_HEIGHT = 810;
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
     * The size of the house and hotels.
     */
    private static final int HOUSE_SIZE = 20;
    /**
     * The size of the die.
     */
    private static final int DIE_SIZE = 50;
    /**
     * The height of the command panel.
     */
    private static final int COMMAND_HEIGHT = 50;
    /**
     * The gap between the buttons in the command panel.
     */
    private static final int PANEL_GAP = 10;
    /**
     * Shifting the command panel in Y.
     */
    private static final int COMMAND_SHIFT_Y = 10;
    /**
     * The maximum number of rolls in the dice animation.
     */
    private static final int MAX_RANDOM_ROLLS = 10;
    /**
     * Figures out how much to offset the players on the cell.
     */
    private static final int ICON_SHIFT_ON_CELL = 30;
    /**
     * Figures out how much to offset the houses on the cell.
     */
    private static final int HOUSE_SHIFT_ON_CELL = 10;
    /**
     * Figures out how much to offset the players on the cell based on each player.
     */
    private static final int ICON_SHIFT_ON_CELL_PER_PLAYER = 5;
    /**
     * Keeps track of the location of the die row.
     */
    private static final int DICE_LOCATION_ROW = 8;
    /**
     * Keeps track of the location of the die column.
     */
    private static final int DICE_LOCATION_COLUMN = 8;
    /**
     * The width of the logo.
     */
    private static final int LOGO_WIDTH = 320;
    /**
     * The height of the logo.
     */
    private static final int LOGO_HEIGHT = 100;
    /**
     * The width of the cash icon.
     */
    private static final int CASH_WIDTH = 176;
    /**
     * The height of the cash icon.
     */
    private static final int CASH_HEIGHT = 100;
    /**
     * The height of the cell.
     */
    private static final int CELL_HEIGHT = 112;
    /**
     * The width of the cell.
     */
    private static final int CELL_WIDTH = 56;
    /**
     * Keeps track of location of Jail square.
     */
    public static final int JAIL_LOCATION = 10;
    /**
     * Keeps track of the location of free parking.
     */
    public static final int FREE_PARKING_LOCATION = 20;
    /**
     * Keeps track of location of Jail square.
     */
    public static final int GOTOJAIL_LOCATION = 30;

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

        // Keeps track of the player labels and cell panels.
        playerLabels = new HashMap<>();
        playerStatusPanels = new HashMap<>();
        boardCells = new ArrayList<>();
        commandButtons = new ArrayList<>();

        // Adding the frame to the model.
        model = new BoardModel();
        model.addBoardView(this);

        controller = new BoardController(model);

        createGUI();

        model.start();
    }

    /**
     * Creating the basic GUI of the monopoly.
     */
    private void createGUI(){
        // Creating JMenu
        this.menuBar = new JMenuBar();

        this.fileMenu = new JMenu("File");

        this.newGame = new JMenuItem("New Board");
        this.saveGame = new JMenuItem("Save Board");
        this.loadGame = new JMenuItem("Load Board");

        this.fileMenu.add(newGame);
        this.fileMenu.add(saveGame);
        this.fileMenu.add(loadGame);

        menuBar.add(fileMenu);

        this.add(menuBar);
        this.setJMenuBar(menuBar);

        // JMenu
        newGame.setEnabled(true);
        newGame.setActionCommand(BoardModel.Command.NEW_BOARD.getStringCommand());
        newGame.addActionListener(controller);

        loadGame.setEnabled(true);
        loadGame.setActionCommand(BoardModel.Command.LOAD_BOARD.getStringCommand());
        loadGame.addActionListener(controller);

        saveGame.setEnabled(true); //change this or kyra will be angry ;(
        saveGame.setActionCommand(BoardModel.Command.SAVE_BOARD.getStringCommand());
        saveGame.addActionListener(controller);

        // Setting the frame settings.
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);

        // The layered pane that includes all the components.
        layeredPane = new JLayeredPane();
        layeredPane.setOpaque(true);
        layeredPane.setPreferredSize(new Dimension(WINDOW_WIDTH,WINDOW_HEIGHT + 100));
        layeredPane.setBackground(Color.decode(BACKGROUND_COLOR));

        // The main panel that keeps track of all the cells
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.decode(BACKGROUND_COLOR));
        mainPanel.setBounds(0, BOARD_SHIFT_Y + PANEL_GAP, BOARD_WIDTH, BOARD_HEIGHT);

        layeredPane.add(mainPanel,0);

        houseLabels = new HashMap<>();

        // Creating the turn label.
        statusLabel = new JLabel();
        statusLabel.setBackground(Color.decode(BACKGROUND_COLOR));
        statusLabel.setBounds(BOARD_WIDTH, PANEL_GAP,
                WINDOW_WIDTH - BOARD_WIDTH, BOARD_SHIFT_Y);

        layeredPane.add(statusLabel,0);

        // Creating the dice panel.
        createDiceLayout();

        // Creating the logo panel.
        buildLogoDisplay();

        // Creating the cash panel.
        buildCashDisplay();

        // Adding the panels to board.
        layeredPane.add(logoPanel,1);
        layeredPane.moveToFront(logoPanel);

        add(layeredPane);

        pack();
        //updateFrame();
        // Making the frame visible.
        this.setVisible(true);
    }

    /**
     * Passes a BoardEvent to the BoardController to be interpreted.
     * @author Bardia Parmoun 101143006
     * @param e the board event, BoardEvent
     */
    @Override
    public void handleMonopolyUpdate(MonopolyEvent e) {
        BoardModel source = (BoardModel) e.getSource();

        // If the event is a player input event. 
        if (e.getStatus() == BoardModel.Status.PLAYER_INPUT){
            handlePlayerInput((PlayerEvent) e);
            return;
        }

        BoardEvent be = (BoardEvent) e;
        Player player = be.getPlayer();
        switch (e.getStatus()) {
            case CHOOSE_BOARD -> handleChooseBoard();
            case INITIALIZE_MONOPOLY -> handleWelcomeMonopoly();
            case GAME_OVER -> handleWinner(source.getPlayers());
            case INITIALIZE_BOARD -> constructBoard(source.getCells());
            case CREATE_PLAYER_ICONS -> createPlayers(source.getPlayers());
            case GET_NUM_PLAYERS -> getNumPlayers();
            case INITIALIZE_PLAYERS -> initializePlayers(source.getPlayerCount());
            case GET_COMMAND -> updateAvailableCommands(player, be.getCommands());
            //case FILE_MENU_OPTIONS -> updateAvailableMenuOptions();
            case PASS_GO -> handlePassGo(player);
            case FREE_PARKING -> handleFreeParking(player);
            case GO_TO_JAIL -> handleGoToJail(player);
            case EXIT_JAIL -> handleExitJail(player);
            case FORCE_PAY_JAIL -> handleForceExitJail(player);
            case UPDATE_MODEL -> updateBoard();
            case NEW_GAME -> makeNewBoard();
        }
    }

    /**
     * Selects which method to use based on playerEvent
     * @author Bardia Parmoun 101143006
     * @param pe, playerEvent
     */
    private void handlePlayerInput (PlayerEvent pe){
        BoardModel source = (BoardModel) pe.getSource();
        Player player = pe.getPlayer();
        BoardModel.Command command = pe.getCommand();
        switch (command) {
            case BUY -> handleBuyLocation(player, pe.getCell(), pe.getResult());
            case REQUEST_SELL -> handleRequestSell(player);
            case SELL -> handleSell(player, pe.getCell());
            case CELL_STATUS -> handleGetCellStatus(pe.getCell());
            case PAY_FEES -> handlePayFees(pe.getCell(), player, pe.getValue(), pe.getResult());
            case PASS -> handlePassingTurn();
            case MOVE -> handlePlayerGUIMove(pe.getPlayer(), pe.getValue(), player.getPosition());
            case REQUEST_FORFEIT -> handleRequestForfeit(player);
            case FORFEIT -> handleForfeitedPlayer(player);
            case CHANGE_WINDOW -> handleRepaintBoard();
            case ROLL_AGAIN -> handleRoll(source.getDice());
            case BUILD -> handlePropertyToBuildOn(player, player.getBuildableProperties());
            case PAINT_HOUSE -> paintHouse(pe.getCell());
        }
    }

    /**
     * Handles passing the turn update.
     * @author Bardia Parmoun, 101143006
     */
    private void handlePassingTurn() {
        disableButtons();
    }

    /**
     * Displays an event in the game either through pop up or label.
     * @param msg the message to display to the user, String
     * @param textMode shows in the label if true, false shows on pop up.
     * @author Bardia Parmoun 101143006
     */
    private void displayStatus(String msg, boolean textMode){
        if (textMode){
            statusLabel.setText(msg);
        } else {
            JOptionPane.showMessageDialog(null, msg);
        }
    }

    /**
     * Repaints the board when the window has changed to make sure everything is visible.
     * @author Bardia Parmoun 101143006
     */
    private void handleRepaintBoard() {
        for (Player player: playerLabels.keySet()) {
            if (!player.isBankrupt()){
                layeredPane.moveToFront(playerLabels.get(player));
            }
        }
    }

    /**
     * Handles moving players cell by cell.
     * @author Sarah Chow 101143033
     * @param player the player that is moving, Player
     * @param originalPos the original position of the player, int
     */
    private void handlePlayerGUIMove(Player player, int amountToMove, int originalPos){
        disableButtons();
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
     * Creates the player labels, icons, and panels.
     * @author Bardia Parmoun 101143006
     * @param players the list of the players to make icons for, List<Player>
     */
    private void createPlayers(List<Player> players) {
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new GridLayout(players.size(), 1));
        statusPanel.setBackground(Color.decode(BACKGROUND_COLOR));

        for (Player p: players){
            JLabel playerLabel = new JLabel();
            JPanel playerPanel = new JPanel();
            TitledBorder title = BorderFactory.createTitledBorder(p.getIconName().toUpperCase());
            playerPanel.setBorder(title);
            playerLabels.put(p, playerLabel);
            playerStatusPanels.put(p, playerPanel);

            updatePlayerStatusPanel(p);

            statusPanel.add(playerPanel);

            layeredPane.add(playerLabel);
        }

        // adding scroll bars to the status panel
        JScrollPane statusPanelWithScroll = new JScrollPane(statusPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        statusPanelWithScroll.setBounds(BOARD_WIDTH, BOARD_SHIFT_Y + PANEL_GAP,
                WINDOW_WIDTH - BOARD_WIDTH, BOARD_HEIGHT);
        layeredPane.add(statusPanelWithScroll, 0);
    }

    /**
     * Updates the player panel.
     * @author Bardia Parmoun 101143006
     * @param p the player panel to update, Player
     */
    private void updatePlayerStatusPanel(Player p){
        JPanel panel = playerStatusPanels.get(p);

        panel.removeAll();
        panel.revalidate();
        panel.repaint();
        String playerStatus = p.toString();
        StringBuilder newStatus = new StringBuilder("<html>");
        for (int i=0; i < playerStatus.length(); i++){
            if (playerStatus.charAt(i)!='\n'){
                newStatus.append(playerStatus.charAt(i));
            } else {
                newStatus.append("<br/>");
            }
        }
        newStatus.append("</html>");

        JLabel statusLabel = new JLabel(newStatus.toString());
        panel.setBackground(Color.decode(BACKGROUND_COLOR));
        panel.add(statusLabel);
    }

    /**
     * Build logo display.
     * @author Bardia Parmoun, 101143006
     */
    private void buildLogoDisplay(){
        logoPanel = new JPanel();
        logoPanel.setBounds(BOARD_WIDTH/2 - LOGO_WIDTH/2, BOARD_HEIGHT/2, LOGO_WIDTH, LOGO_HEIGHT);
        logoPanel.setBackground(Color.decode(BACKGROUND_COLOR));

        try {
            BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getResource
                    ("images/central_logo.png")));


            Image newImage = image.getScaledInstance(LOGO_WIDTH, LOGO_HEIGHT, Image.SCALE_DEFAULT);
            JLabel label = new JLabel(new ImageIcon(newImage));
            logoPanel.removeAll();
            logoPanel.add(label);
        }  catch(Exception e){
            System.out.println("build logo display failed");
        }
    }

    /**
     * Build cash display.
     * @author Bardia Parmoun, 101143006
     */
    private void buildCashDisplay(){
        JPanel cashPanel = new JPanel(new BorderLayout());
        cashPanel.setBounds(CELL_HEIGHT + 2 * PANEL_GAP, BOARD_SHIFT_Y + CELL_HEIGHT + 3 * PANEL_GAP,
                CASH_WIDTH/4 + CASH_WIDTH, CASH_HEIGHT);

        cashPanel.setBackground(Color.decode(BACKGROUND_COLOR));
        try {
            BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getResource("images/money.png")));


            Image newImage = image.getScaledInstance(CASH_WIDTH, CASH_HEIGHT, Image.SCALE_DEFAULT);
            JLabel label = new JLabel(new ImageIcon(newImage));
            JLabel cashAmount = new JLabel("$" + model.getBankMoney());
            cashPanel.removeAll();
            cashPanel.add(label, BorderLayout.WEST);
            cashPanel.add(cashAmount, BorderLayout.EAST);
            layeredPane.add(cashPanel,1);
            layeredPane.moveToFront(cashPanel);

        }  catch(Exception e){
            System.out.println("build cash display failed");
        }
    }

    /**
     * Create dice panels
     * @author Sarah Chow 101143033
     */
    private void createDiceLayout(){
        GridBagConstraints die1_constraint = new GridBagConstraints();
        die1_constraint.gridx = DICE_LOCATION_ROW;
        die1_constraint.gridy = DICE_LOCATION_COLUMN;
        die1_constraint.fill = GridBagConstraints.HORIZONTAL;
        die1_constraint.anchor = GridBagConstraints.NORTH;

        GridBagConstraints die2_constraint = new GridBagConstraints();
        die2_constraint.gridx = DICE_LOCATION_ROW - 1;
        die2_constraint.gridy = DICE_LOCATION_COLUMN;
        die2_constraint.fill = GridBagConstraints.HORIZONTAL;
        die2_constraint.anchor = GridBagConstraints.NORTH;

        // Creating the dice panels.
        dicePanels = new JPanel[2];
        dicePanels[0] = new JPanel(new BorderLayout());
        dicePanels[0].setBackground(Color.decode(BACKGROUND_COLOR));

        dicePanels[1] = new JPanel(new BorderLayout());
        dicePanels[1].setBackground(Color.decode(BACKGROUND_COLOR));

        mainPanel.add(dicePanels[0], die1_constraint);
        mainPanel.add(dicePanels[1], die2_constraint);
    }

    /**
     * Method to animate the rolling of random dice numbers and to visually land on a value.
     * @author Sarah Chow 101143033
     * @param randomRoll value to land on, int
     * @param dieIndex keeps track of the die index, int
     */
    private void buildDiceDisplay(int randomRoll, int dieIndex){
        try {
            BufferedImage dieImage1 = ImageIO.read(Objects.requireNonNull(getClass().getResource
                    ("images/dice/dice1.png")));
            BufferedImage dieImage2 = ImageIO.read(Objects.requireNonNull(getClass().getResource
                    ("images/dice/dice2.png")));
            BufferedImage dieImage3 = ImageIO.read(Objects.requireNonNull(getClass().getResource
                    ("images/dice/dice3.png")));
            BufferedImage dieImage4 = ImageIO.read(Objects.requireNonNull(getClass().getResource
                    ("images/dice/dice4.png")));
            BufferedImage dieImage5 = ImageIO.read(Objects.requireNonNull(getClass().getResource
                    ("images/dice/dice5.png")));
            BufferedImage dieImage6 = ImageIO.read(Objects.requireNonNull(getClass().getResource
                    ("images/dice/dice6.png")));

            BufferedImage currImage;

            switch (randomRoll) {
                case (2) -> currImage = dieImage2;
                case (3) -> currImage = dieImage3;
                case (4) -> currImage = dieImage4;
                case (5) -> currImage = dieImage5;
                case (6) -> currImage = dieImage6;
                default -> currImage = dieImage1;
            }

            dicePanels[dieIndex].removeAll();

            Image newImage = currImage.getScaledInstance(DIE_SIZE, DIE_SIZE, Image.SCALE_DEFAULT);
            JLabel label = new JLabel(new ImageIcon(newImage));
            dicePanels[dieIndex].add(label);
        }
        catch(Exception e){
            System.out.println("build dice display failed");
        }
    }

    /**
     * Passes the values of both dice and the player to the BoardController to be interpreted.
     * @author Sarah Chow 101143033
     * @param dice value of the dice, int[]
     */
    private void handleRoll(int[] dice) {
        disableButtons();
        int die1 = dice[0];
        int die2 = dice[1];


        for (int i = 0; i < MAX_RANDOM_ROLLS + 1; i++) {
            Random rand = new Random();
            int randomRoll1;
            int randomRoll2;
            randomRoll1 = rand.nextInt((6 - 1) + 1) + 1;
            randomRoll2 = rand.nextInt((6 - 1) + 1) + 1;

            if (i == MAX_RANDOM_ROLLS) {
                randomRoll1 = die1;
                randomRoll2 = die2;
            }

            buildDiceDisplay(randomRoll1, 0);
            buildDiceDisplay(randomRoll2, 1);


            this.revalidate();

            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (Exception e) {
                System.out.println("wait failed");
            }
        }

        buildDiceDisplay(die1, 0);
        buildDiceDisplay(die2, 1);

        this.revalidate();
    }

    /**
     * Enables the command buttons based on what the user is allowed to do
     * @author Kyra Lothrop 101145872
     * @param commands keeps track of the list of the commands, List<BoardModel.Command>
     */
    private void updateAvailableCommands(Player player, List<BoardModel.Command> commands){
        StringBuilder availableCommands = new StringBuilder();

        for (BoardModel.Command command: commands){
            availableCommands.append(command.getStringCommand()).append(", ");
        }

        availableCommands = new StringBuilder(availableCommands.substring(0, availableCommands.length() - 2));

        for(JButton b: commandButtons){
            b.setEnabled(availableCommands.toString().contains(b.getText()));
        }

        TitledBorder title = BorderFactory.createTitledBorder("Current Turn: " + player.getIconName().toUpperCase());
        statusLabel.setBorder(title);

        updatePlayerStatusPanel(player);
    }

    private void updateAvailableMenuOptions(){
        //here1

    }

    /**
     * Method to cancel the player initialization process.
     * @author Sarah Chow 101143033
     */
    private void initializationCancel(){
        JOptionPane.showMessageDialog(null, "Game initialization is cancelled!");
        System.exit(0);
    }

    private void handleChooseBoard(){
        List<File> boardFileOptions = new ArrayList<>();
        try{
            boardFileOptions = Files.walk(Paths.get("board_config"))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());

            List<String> stringFileNames = new ArrayList<>();
            for(File f: boardFileOptions){
                stringFileNames.add(f.getName());
            }

            String selectedBoard = (String) JOptionPane.showInputDialog(null,
                    "Select the Monopoly board", "SELECT BOARD",
                    JOptionPane.QUESTION_MESSAGE, null, stringFileNames.toArray(), stringFileNames.get(0));

            model.startNewGame(true);
        }catch(IOException e){
            System.out.println(e);
        }

    }

    /**
     * Get the number of players from the user
     * @author Kyra Lothrop 101145872
     */
    private void getNumPlayers(){
        try{
            Integer[] numPlayerOptions = {2,3,4,5,6,7,8};
            int numPlayers = (Integer)JOptionPane.showInputDialog(null,
                    "How many players in total (AI + users)?", "INITIALIZE GAME DATA",
                    JOptionPane.QUESTION_MESSAGE, null, numPlayerOptions, numPlayerOptions[0]);

            ArrayList<Integer> numAIOptions = new ArrayList<>();
            for(int i = 0; i<= numPlayers; i++){
                numAIOptions.add(i);
            }
            int numAIPlayers = (Integer)JOptionPane.showInputDialog(null,
                    "How many AI players will be playing?", "INITIALIZE GAME DATA",
                    JOptionPane.QUESTION_MESSAGE, null, numAIOptions.toArray(), numAIOptions.get(0));

            model.setNumAIPlayer(numAIPlayers);
            model.setNumPlayers(numPlayers);
        }
        catch(Exception e){
            initializationCancel();
        }
    }

    /**
     * Initialize the player names and icons based on user input
     * @author Kyra Lothrop 101145872
     * @param numPlayers keeps track of the number of player, int
     */
    private void initializePlayers(int numPlayers){
        String[] namesAI = {"GOOGLE", "SIRI", "ALEXA", "BIXBY", "BAYMAX", "WALL-E", "EVA", "TOBIO"};

        for (int i = 0; i < numPlayers; i++){
            JTextField playerName = new JTextField();
            String playerIconMessage;

            if(i+1 <= (numPlayers-model.getNumAIPlayer())){
                Object[] message = {
                        "What's player "+ (i+1) + "'s name?", playerName,
                };

                int ans = JOptionPane.showConfirmDialog(null, message, "Initialize Player"+ (i+1),
                        JOptionPane.OK_CANCEL_OPTION);

                if (ans != JOptionPane.OK_OPTION){
                    initializationCancel();
                }

                playerIconMessage = "Select player " + (i+1) + "'s icon";
            } else {

                JOptionPane.showMessageDialog(null, "AI player " + namesAI[i] +" initialized",
                        "Initialize AI player", JOptionPane.PLAIN_MESSAGE);
                playerName.setText(namesAI[i]);

                playerIconMessage = "Select " + namesAI[i] + "'s icon";
            }

            Object[] iconOptions = getListOfIconsUpper();

            String playerIcon = (String) JOptionPane.showInputDialog(null, playerIconMessage ,
                    "Select Icon", JOptionPane.QUESTION_MESSAGE, null, iconOptions, iconOptions[0]);

            if (playerIcon == null){
                initializationCancel();
            } else if (i+1 <= (numPlayers-model.getNumAIPlayer())){
                model.addPlayer(new Player(playerName.getText(), findPlayerIcon(playerIcon.toLowerCase())));
            }
            else {
                model.addPlayer(new AIPlayer(model, playerName.getText(), findPlayerIcon(playerIcon.toLowerCase())));
            }
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
            if (!icon.getUsed() && !icon.getName().equals("bank")) {
                iconOptions.add(icon.getName().toUpperCase());
            }
        }

        return iconOptions.toArray();
    }

    /**
     * Finds the player icons used for the drop down menu.
     * @param icon the string of icon to find.
     * @return the Icon object of the player.
     */
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
        JPanel commandsPanel = new JPanel(new GridLayout(2,4, PANEL_GAP, PANEL_GAP));
        commandsPanel.setBounds(PANEL_GAP, COMMAND_SHIFT_Y,BOARD_WIDTH - 2 * PANEL_GAP,
                COMMAND_HEIGHT + PANEL_GAP);

        BoardModel.Command[] buttons = {BoardModel.Command.ROLL_AGAIN, BoardModel.Command.PASS,
                BoardModel.Command.FORFEIT, BoardModel.Command.BUY, BoardModel.Command.SELL,
                BoardModel.Command.PAY_FEES, BoardModel.Command.BUILD, BoardModel.Command.CELL_STATUS};

        commandsPanel.setBackground(Color.decode(BACKGROUND_COLOR));

        for (BoardModel.Command c : buttons) {
            String s = c.getStringCommand();
            JButton commandButton = new JButton(s);
            commandButton.setActionCommand(s);
            commandButton.addActionListener(controller);
            commandButtons.add(commandButton);
            commandsPanel.add(commandButton);

            commandButton.setEnabled(false);
        }
        layeredPane.add(commandsPanel);

        // Constructing the panels
        int row = SIZE - 1;
        int col = SIZE - 1;
        int row_step = 0;
        int col_step = -1;
        int direction = GridBagConstraints.WEST;

        for (int i = 0; i < cells.size(); i++) {
            BoardCell cell = cells.get(i);
            // Changes the row and col if it hits corners.
            switch (i) {
                case JAIL_LOCATION -> {
                    row = SIZE - 1;
                    col = 0;
                    row_step = -1;
                    col_step = 0;
                    direction = GridBagConstraints.NORTH;
                }
                case FREE_PARKING_LOCATION -> {
                    row = 0;
                    col = 0;
                    row_step = 0;
                    col_step = 1;
                    direction = GridBagConstraints.EAST;
                }
                case GOTOJAIL_LOCATION -> {
                    row = 0;
                    col = SIZE - 1;
                    row_step = 1;
                    col_step = 0;
                    direction = GridBagConstraints.SOUTH;
                }
            }

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

            constructCell(cell, panel, direction);

            LineBorder border = (LineBorder) BorderFactory.createLineBorder(Color.black);
            panel.setBorder(border);

            boardCells.add(panel);

            mainPanel.add(panel, c);

            row += row_step;
            col += col_step;
        }

        // adding all the components to the frame
        this.pack();
    }

    /**
     * Creates a cell panel with images or labels
     * @param cell the cell, BoardCell
     * @param panel the panel, JPanel
     * @param direction the direction of the cell, int
     */
    private void constructCell(BoardCell cell, JPanel panel, int direction){
        // Loads the cell image.
        String imgPath = cell.getImgPath();
        if (!imgPath.equals("")){ //if the cell has an image load it.
            BufferedImage image;
            try {
                image = ImageIO.read(Objects.requireNonNull(getClass().getResource(imgPath)));
                JLabel label = new JLabel(new ImageIcon(image));
                // Adds the panel to the board and list of panels.
                panel.add(label);
            } catch (IOException e) {
                System.out.println("could not load cell images.");
            }
        } else { // If not image construct the board manually.
            createCellManually(cell, panel, direction);
        }
    }

    /**
     * Creating the cell with labels instead  of images.
     * @param cell the cell, BoardCell
     * @param panel the panel, JPanel
     * @param direction the direction of the cell, int
     */
    private void createCellManually(BoardCell cell, JPanel panel, int direction){
        JLabel nameLabel; // Displays the name of the cell
        JLabel neighbourhoodLabel = new JLabel(); // Displays the neighbourhood color.
        JLabel priceLabel = new JLabel(); // Displays the price of the cell.
        JLabel emptyLabel = new JLabel(); //Keeps a gap between the labels.

        // Making the neighbourhood label.
        if (cell.getType() == BoardCell.CellType.PROPERTY){
            neighbourhoodLabel = new JLabel("");
            // if true the component paints every pixel within its bounds
            neighbourhoodLabel.setOpaque(true);
            Property p = (Property) cell;
            neighbourhoodLabel.setBackground(Color.decode(p.getNeighborhood().getColor()));
        }

        // Making the price label.
        if (cell.getType() == BoardCell.CellType.PROPERTY ||
                cell.getType() == BoardCell.CellType.RAILROAD ||
                cell.getType() == BoardCell.CellType.UTILITY){

            int price = ((Buyable)cell).getPrice();
            priceLabel = new JLabel("$" + price);
        } else if (cell.getType() == BoardCell.CellType.TAX) {
            int price = ((Tax)cell).getTax();
            priceLabel = new JLabel("$" + price);
        }

        priceLabel.setFont(new Font("Verdana", Font.PLAIN, 8));

        // Creating the name label and panel layout.
        if (direction == GridBagConstraints.WEST || direction == GridBagConstraints.EAST){
            nameLabel = new JLabel("<html><p style =\"width:40px\">"+cell.getName()+"</p></html>");
            nameLabel.setFont(new Font("Verdana", Font.PLAIN, 8));

            panel.setLayout(new GridLayout(4, 1));
            panel.setPreferredSize(new Dimension(CELL_WIDTH, CELL_HEIGHT));
        } else {
            nameLabel = new JLabel("<html><p style =\"width:23px\">"+cell.getName()+"</p></html>");
            nameLabel.setFont(new Font("Verdana", Font.PLAIN, 8));

            panel.setLayout(new GridLayout(1, 4));
            panel.setPreferredSize(new Dimension(CELL_HEIGHT, CELL_WIDTH));
        }

        // Adding the labels to the cells.
        if (direction == GridBagConstraints.WEST || direction == GridBagConstraints.SOUTH){
            panel.add(neighbourhoodLabel);
            panel.add(nameLabel);
            panel.add(emptyLabel);
            panel.add(priceLabel);
        } else {
            panel.add(priceLabel);
            panel.add(nameLabel);
            panel.add(emptyLabel);
            panel.add(neighbourhoodLabel);
        }
    }

    /**
     * Displays the name of the property the current player is standing on in the terminal.
     * @author Sarah Chow 101143033
     * @param player player performing actions, Player
     */
    private void showCurrentCell(Player player, int cellIndex){
        if (!player.getIconImgPath().equals("")) {
            try {
                BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getResource
                        (player.getIconImgPath())));
                Image dimg = image.getScaledInstance(ICON_SIZE,ICON_SIZE, Image.SCALE_SMOOTH);
                playerLabels.get(player).setIcon(new ImageIcon(dimg));

                JPanel currentCell = boardCells.get(cellIndex);

                Rectangle cellPosition = currentCell.getBounds();

                int index =  new ArrayList<>(playerLabels.keySet()).indexOf(player);
                int x = cellPosition.x + 3*cellPosition.width/4 - ICON_SHIFT_ON_CELL -
                        ICON_SHIFT_ON_CELL_PER_PLAYER * index;
                int y = cellPosition.y + cellPosition.height/2 - ICON_SHIFT_ON_CELL -
                        ICON_SHIFT_ON_CELL_PER_PLAYER * index + BOARD_SHIFT_Y;

                playerLabels.get(player).setBounds(x, y, ICON_SIZE, ICON_SIZE);
                layeredPane.moveToFront(playerLabels.get(player));
            } catch (IOException e) {
                System.out.println("Could not load the player icon image");
            }
        }
    }


    /**
     * Displays whether the current player can afford the property they attempted to buy or not.
     * @author Owen VanDusen 101152022
     * @param player player performing actions, Player
     * @param location property that is in contention for purchasing, Property
     * @param result if the player can afford the property, boolean
     */
    private void handleBuyLocation(Player player, BoardCell location, boolean result) {
        String buyMessage;
        if (result){
            buyMessage = "Player " + player.getIconName().toUpperCase() + " bought " + location.getName();
        } else {
            buyMessage = "Player " + player.getIconName().toUpperCase() + " cannot afford " + location.getName();
        }

        displayStatus(buyMessage, player.isPlayerAI());
    }

    /**
     * Displays whether the current player can sell the property they attempted to sell.
     * @author Sarah Chow 101143033
     * @param player player performing actions, Player
     */
    private void handleRequestSell(Player player) {
        JPanel panel = new JPanel(new GridLayout(player.getOwnedLocations(true).size(), 2));
        ButtonGroup group = new ButtonGroup();

        for (BoardCell l : player.getOwnedLocations(true)) {

            JRadioButton button = new JRadioButton(l.getName().toUpperCase());

            String message = "<html>" + l + "</html>";

            JLabel des = new JLabel(message);

            button.setActionCommand(l.getName());
            group.add(button);

            des.setVisible(true);
            panel.add(button);
            panel.add(des);
        }

        panel.setPreferredSize(new Dimension(400, 400));


        int ans = JOptionPane.showConfirmDialog(null, panel,
                "SELL PROPERTY", JOptionPane.OK_CANCEL_OPTION);
        if (ans == JOptionPane.OK_OPTION){
            if (group.getSelection() != null) {
                for (BoardCell l : player.getOwnedLocations(true)){
                    if (group.getSelection().getActionCommand().equals(l.getName())){
                        model.sellLocation(player, l);
                    }
                }
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "Sell cancelled!");
        }
    }

    /**
     * Handles the message for selling a cell
     * @param player the player to sell a cell, Player
     * @param cell the cell being sold, BoardCell
     */
    private void handleSell(Player player, BoardCell cell){
        String message = "Player " + player.getIconName().toUpperCase() + " sold " + cell.getName().toUpperCase();
        displayStatus(message, player.isPlayerAI());
    }

    /**
     * Displays the player going to Jail.
     * @author Sarah Chow 101143033
     * @param player player to go to jail, Player
     */
    private void handleGoToJail(Player player){
        String message = String.format("Player %s has been sent to JAIL!", player.getIconName().toUpperCase());
        displayStatus(message, player.isPlayerAI());
        handlePlayerGUIMove(player, GOTOJAIL_LOCATION - JAIL_LOCATION, GOTOJAIL_LOCATION);
    }

    /**
     * Displays prompts if player would like to pay their way out of jail.
     * @author Sarah Chow 101143033
     * @param player the player in jail, Player
     */
    private void handleExitJail(Player player){
        String message = String.format("Player %s left JAIL!", player.getIconName().toUpperCase());
        displayStatus(message, player.isPlayerAI());
    }

    /**
     * Displays prompts if the player has been in jail for 3 rounds, they must pay or forfeit the game.
     * @author Sarah Chow 101143033
     */
    private void handleForceExitJail(Player player){
        statusLabel.setText("You've exceeded three turns in JAIL. Please pay $50 to the bank!");
        String message = "Player " + player.getIconName().toUpperCase() +
                " exceeded three turns in JAIL. Please pay $50 to the bank!";
        displayStatus(message, player.isPlayerAI());

    }

    /**
     * Displays the information of the property the player is currently on.
     * @author Sarah Chow 101143033
     * @param currentCell property the player is on, BoardCell
     */
    private void handleGetCellStatus(BoardCell currentCell){
        JOptionPane.showMessageDialog(null, currentCell, "CELL STATUS", JOptionPane.PLAIN_MESSAGE);
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
        String feesMessage;
        if (result){
            feesMessage = "Player " + player.getIconName().toUpperCase() + " successfully paid $" + fees + " to " +
                    boardCell.getOwner().getIconName().toUpperCase();

            Player owner = boardCell.getOwner();

            // Updating the status of panel of the owner that gets the money.
            if (!owner.getIconName().equals(BoardModel.Icon.BANK.getName())){
                updatePlayerStatusPanel(owner);
            } else {
                buildCashDisplay();
            }
        }
        else{
            feesMessage = "Player " + player.getIconName().toUpperCase() + " cannot currently pay fees to " +
                    boardCell.getOwner().getIconName().toUpperCase();
        }

        displayStatus(feesMessage, player.isPlayerAI());
    }

    /**
     * Displays a breaker to indicate change of turn.
     * @author Owen VanDusen 101152022
     */
    private void disableButtons() {
        for(JButton b: commandButtons){
            b.setEnabled(false);
        }
    }

    /**
     * Confirmation message that the player would like to forfeit the game.
     * @author Sarah Chow 101143033
     * @param player player forfeiting, Player
     */
    private void handleRequestForfeit(Player player) {
        int ans = JOptionPane.showConfirmDialog(null,
                "Are you sure you would like to forfeit the game?");
        if (ans == JOptionPane.YES_OPTION){
            model.forfeit(player);
        }
        else{
            JOptionPane.showMessageDialog(null, "Forfeit request cancelled!");
        }
    }

    /**
     * Generates a pop-up GUI for the player to select which property to build a house on.
     * @author Owen VanDusen 101152022
     * @param player the player who is paying for the house.
     * @param buildable List of the colours of neighborhoods the player can build houses on
     */
    private void handlePropertyToBuildOn(Player player, List<Property.NeighborhoodEnum> buildable){
        int buildableHouses = 0;
        for(Property.NeighborhoodEnum e: buildable){
            buildableHouses += e.getNumProperties();
        }

        JPanel panel = new JPanel(new GridLayout(1, buildableHouses));
        ButtonGroup group = new ButtonGroup();

        for(BoardCell bc: player.getOwnedLocations(false)){
            if (bc.getType() == BoardCell.CellType.PROPERTY) {
                Property p = (Property) bc;
                if (buildable.contains(p.getNeighborhood())) {
                    JPanel subPanel = new JPanel(new GridLayout(3, 1));

                    JRadioButton addButton = new JRadioButton("Add House");
                    addButton.setActionCommand(p.getName());
                    group.add(addButton);

                    JLabel propertyName = new JLabel(p.getName());
                    JLabel houseCost = new JLabel("Cost of a House: " + p.getNeighborhood().getHouseCost());

                    subPanel.add(propertyName);
                    subPanel.add(addButton);
                    subPanel.add(houseCost);
                    subPanel.setPreferredSize(new Dimension(100, 200));
                    panel.add(subPanel);
                }
            }
        }

        panel.setPreferredSize(new Dimension(200*buildableHouses,200));

        int ans = JOptionPane.showConfirmDialog(null,panel,"BUILD ON PROPERTY",
                JOptionPane.OK_CANCEL_OPTION);
        if(ans == JOptionPane.OK_OPTION) {
            if (group.getSelection()!=null){
                for(BoardCell bc: player.getOwnedLocations(false)){
                    if (bc.getType() == BoardCell.CellType.PROPERTY){
                        Property p = (Property) bc;
                        if(group.getSelection().getActionCommand().equals(p.getName())){
                            model.buildHouse(p, player);
                        }
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(null,"Building Cancelled");
        }
    }

    /**
     * Generates a house image and places it on the property where the house has been built.
     * Offsets in the case where multiple houses have been built.
     * Replaces 4 houses with a hotel when building the 5th house.
     * @author Owen VanDusen 101152022
     * @param cell the board cell where the house needs to be placed.
     */
    private void paintHouse(BoardCell cell){
        int x , y;
        boolean isHotel = false;
        int i = model.findCellIndex(cell);

        JPanel currentCell = boardCells.get(i);

        Rectangle cellPosition = currentCell.getBounds();

        Property property = (Property) cell;
        if(property.getNumHouses() == BoardModel.MAX_HOUSE_VAL){
            //need to remove all houses and put a hotel
            for (JLabel h: houseLabels.get(cell)){
                layeredPane.setLayer(h, -1);
            }

            isHotel = true;
        }

        int offsetCount = isHotel? 1 : property.getNumHouses();
        //Bottom row of the board
        if(i < 10){
            x = cellPosition.x + HOUSE_SHIFT_ON_CELL * offsetCount;
            y = cellPosition.y + HOUSE_SHIFT_ON_CELL + BOARD_SHIFT_Y;
        }
        //Left column of the board
        else if(i < 20){
            x = cellPosition.width - HOUSE_SHIFT_ON_CELL;
            y = cellPosition.y + HOUSE_SHIFT_ON_CELL * offsetCount + BOARD_SHIFT_Y;
        }
        //Top row of the board
        else if(i < 30){
            x = cellPosition.x + HOUSE_SHIFT_ON_CELL *  offsetCount;
            y = cellPosition.height - HOUSE_SHIFT_ON_CELL + BOARD_SHIFT_Y;
        }
        //Right column of the board
        else {
            x = cellPosition.x + HOUSE_SHIFT_ON_CELL;
            y = cellPosition.y + HOUSE_SHIFT_ON_CELL * offsetCount + BOARD_SHIFT_Y;
        }

        try {
            String imgPath = isHotel? "images/houses/Hotel.png" : "images/houses/monopoly house.png";
            BufferedImage houseImg = ImageIO.read(Objects.requireNonNull(getClass().getResource(imgPath)));
            Image dimg = houseImg.getScaledInstance(HOUSE_SIZE,HOUSE_SIZE, Image.SCALE_SMOOTH);

            JLabel house = new JLabel(new ImageIcon(dimg));
            house.setBounds(x,y,HOUSE_SIZE,HOUSE_SIZE);

            if (!houseLabels.containsKey(cell)){
                houseLabels.put(cell, new ArrayList<>());
            }

            houseLabels.get(cell).add(house);
            layeredPane.add(house,1);

        } catch(IOException e) {
            System.out.println("Could not load the house image");
        }
    }

    /**
     * Displays a message informing that a player chose to forfeit the game.
     * @author Sarah Chow 101143033
     * @param player player forfeiting, Player
     */
    private void handleForfeitedPlayer(Player player){
        String message = "Player: " + player.getIconName().toUpperCase() + " has forfeited the game!";
        displayStatus(message, player.isPlayerAI());
        layeredPane.setLayer(playerLabels.get(player), -1);

        updatePlayerStatusPanel(player);
    }

    /**
     * Displays the end of game statistics and the ordered rank of all players.
     * @author Bardia Parmoun 101143006
     * @param players list of players in the order they forfeit the game, List<Player>
     */
    private void handleWinner(List<Player> players) {
        for(JButton b: commandButtons){
            b.setEnabled(false);
        }

        StringBuilder gameOverMessage = new StringBuilder();
        players.sort(Comparator.comparingInt(Player::getRank));

        gameOverMessage.append("Player ").append(players.get(0).getIconName().toUpperCase()).append(" won the game!\n");
        for (Player player: players){
            if (player.isBankrupt()) {
                gameOverMessage.append("Player ").append(player.getIconName().toUpperCase()).append(" has the rank ")
                        .append(player.getRank()).append("\n");
            }
        }
        JOptionPane.showMessageDialog(null, gameOverMessage.toString(), "GAME OVER!",
                JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Handles landing on free parking.
     * @param player landed on parking, Player
     */
    private void handleFreeParking(Player player) {
        String message = "Player " + player.getIconName().toUpperCase() + " landed on free parking and collected " +
                "central money!";
        displayStatus(message, player.isPlayerAI());
        buildCashDisplay();
    }

    /**
     * Display that the user has passed GO
     * @author Kyra Lothrop 101145872
     * @param player the player that passed GO
     */
    private void handlePassGo(Player player) {
        String message = "Player " + player.getIconName().toUpperCase() +  " passed go!";
        displayStatus(message, player.isPlayerAI());
    }

    public void updateBoard(){
        clearBoard();

        this.constructBoard(model.getCells());
        this.createPlayers(model.getPlayers());

        for (Player p: model.getPlayers()){
            this.showCurrentCell(p, p.getPosition());
        }

        this.revalidate();
    }

    private void clearBoard(){
        getContentPane().removeAll();
        layeredPane.removeAll();
        playerLabels.clear();
        playerStatusPanels.clear();

        createGUI();
    }

    private void makeNewBoard(){
        clearBoard();

        model.constructBoard("originalBoard.xml");
        model.getNumPlayers();
        model.initiatePlayers();
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