import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;

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

    private List<JPanel> boardCells;
    private Map<Player, JLabel> playerLabels;


    public static final int SIZE = 11;

    public static final String BACKGROUND_COLOR = "#cbe4d0";

    private JPanel mainPanel;

    private JLayeredPane layeredPane;


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
        //this.setResizable(false);
        setState(JFrame.NORMAL);

        layeredPane = new JLayeredPane();
        layeredPane.setOpaque(true);
        getContentPane().add(layeredPane);
        addWindowListener(getWindowAdapter());
        layeredPane.setPreferredSize(new Dimension(1000,750));
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.decode(BACKGROUND_COLOR));
        mainPanel.setBounds(0, 0, 750, 750);

        layeredPane.add(mainPanel,0);

        playerLabels = new HashMap<>();

        boardCells = new ArrayList<JPanel>();

        model = new BoardModel();
        model.addBoardView(this);
        controller = new BoardController();

        this.setVisible(true);

        model.play();

    }

    private WindowAdapter getWindowAdapter() {
        return new WindowAdapter() {
            @Override
            public void windowDeiconified(WindowEvent we) {
                for (JLabel label: playerLabels.values())
                layeredPane.moveToFront(label);
            }
        };
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
            case PLAYER_MOVE -> showCurrentCell(e.getPlayer());
            case BUY -> handleBuyProperty(e.getPlayer(), (Property) e.getBoardCell(), e.getResult());
            case SELL -> handleSellProperty(e.getPlayer(), (Property) e.getBoardCell(), e.getResult());
            case PLAYER_STATUS -> handleGetPlayerStatus(e.getPlayer());
            case BOARD_STATUS -> handleGetBoardStatus(e.getPlayers());
            case CELL_STATUS -> handleGetCellStatus(e.getPlayer().getCurrentCell());
            case PLAYER_DOUBLE_ROLL -> handleRollingDoubles(e.getPlayer());
            case INITIALIZE_MONOPOLY -> handleWelcomeMonopoly();
            case PAY_FEES -> handlePayFees(e.getPlayer().getCurrentCell(), e.getPlayer(), e.getValue(), e.getResult());
            case PASS_TURN -> handleCurrentPlayerChange();
            case PLAYER_FORFEIT -> handleForfeitedPlayer(e.getPlayer());
            case GAME_OVER -> handleWinner(e.getPlayers());
            case INITIALIZE_BOARD -> constructBoard(e.getCells());
            case CREATE_PLAYER_ICONS -> createPlayerLabels((ArrayList<Player>) e.getPlayers());
            default -> controller.eventListener(e);
        }
    }

    private void createPlayerLabels(ArrayList<Player> players) {
        for (Player p: players){
            JLabel playerLabel = new JLabel();
            playerLabels.put(p, playerLabel);
            layeredPane.add(playerLabel);
        }
    }

    private void constructBoard(List<BoardCell> cells) {
        // Constructing the panels
        int row = SIZE - 1;
        int col = SIZE - 1;
        int row_step = 0;
        int col_step = -1;
        int direction = GridBagConstraints.WEST;

        for (BoardCell cell: cells) {
            try{
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

                BufferedImage image = ImageIO.read(getClass().getResource(cell.getImgName()));
                JLabel label = new JLabel(new ImageIcon(image));
                GridBagConstraints c = new GridBagConstraints();
                c.gridx = col;
                c.gridy = row;
                c.fill = GridBagConstraints.HORIZONTAL;
                c.anchor = direction;
                c.insets = new Insets(0,0,0,0);

                JPanel panel = new JPanel(new BorderLayout());
                panel.setBackground(Color.decode(BACKGROUND_COLOR));

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

        JScrollPane productPanelWithScroll = new JScrollPane(layeredPane,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
               JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // adding all the components to the frame
        this.getContentPane().add(productPanelWithScroll,BorderLayout.CENTER);
        this.pack();
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
        System.out.println("\nRolling dice for: " + player.getIconName().toUpperCase());
        System.out.printf("---> You rolled a %d and a %d\n", die1, die2);
        System.out.printf("---> Total: %d\n\n", die1 + die2);
    }

    /**
     * Displays the name of the property the current player is standing on in the terminal.
     * @author Sarah Chow 101143033
     * @param player player performing actions, Player
     */
    private void showCurrentCell(Player player){
        if (!player.getIconImgPath().equals("")) {
            try {
                BufferedImage image = ImageIO.read(getClass().getResource(player.getIconImgPath()));
                Image dimg = image.getScaledInstance(40,40, Image.SCALE_SMOOTH);
                playerLabels.get(player).setIcon(new ImageIcon(dimg));
                //playerLabel.setOpaque(true);
                BoardCell currCell = player.getCurrentCell();

                JPanel currentCell = boardCells.get(currCell.getIndex());

                Rectangle cellPosition = currentCell.getBounds();

                int index = (int) new ArrayList(playerLabels.keySet()).indexOf(player);
                int x = cellPosition.x + 3*cellPosition.width/4 - 10 * (2 +index);
                int y = cellPosition.y + 1*cellPosition.height/2 - 10 * (2 +index);
                playerLabels.get(player).setBounds(x, y, 50, 50);
                layeredPane.moveToFront(playerLabels.get(player));

                this.pack();
            } catch (IOException e) {
                System.out.println("Could not load the player icon image");
            }
        }

        System.out.printf("Player %s is currently at: %s\n",
                player.getIconName().toUpperCase(),
                player.getCurrentCell().getName());
    }

    /**
     * Displays whether the current player can afford the property they attempted to buy or not.
     * @author Owen VanDusen 101152022
     * @param player player performing actions, Player
     * @param property property that is in contention for purchasing, Property
     * @param result if the player can afford the property, boolean
     */
    private void handleBuyProperty(Player player, Property property, boolean result) {
        if (result){
            System.out.printf("\nPlayer %s bought %s\n", player.getIconName().toUpperCase(), property.getName());
        } else {
            System.out.printf("\nPlayer %s cannot afford %s\n", player.getIconName().toUpperCase(), property.getName());
        }
    }

    /**
     * Displays whether the current player can sell the property they attempted to sell.
     * @author Kyra Lothrop 101145872
     * @param player player performing actions, Player
     * @param property property that is in contention for selling, Property
     * @param result if the player can sell the property, boolean
     */
    private void handleSellProperty(Player player, Property property, boolean result) {
         if (result){
            System.out.printf("\nPlayer %s sold %s\n", player.getIconName().toUpperCase(), property.getName());
        } else {
            System.out.printf("\nPlayer %s cannot sell %s\n", player.getIconName().toUpperCase(), property.getName());
        }
    }

    /**
     * Displays the status of the current player.
     * @author Owen VanDusen 101152022
     * @param player player performing actions, Player
     */
    private void handleGetPlayerStatus(Player player) {
        System.out.printf("\nDisplaying the status of player: %s\n", player.getIconName().toUpperCase());
        System.out.println(player + "\n");
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
     * Displays the information of the property the player is currently on.
     * @author Bardia Parmoun 101143006
     * @param currentCell property the player is on, BoardCell
     */
    private void handleGetCellStatus(BoardCell currentCell){
        System.out.printf("\nDisplaying the status of the current cell: %s\n", currentCell.getName());
        System.out.println(currentCell + "\n");
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
     * Displays the introduction message.
     * @author Sarah Chow 101143033
     */
    private void handleWelcomeMonopoly() {
        System.out.println("WELCOME TO MONOPOLY");
        System.out.println("-------------------------------------------------------------");
        System.out.println("In this interactive business game you will try to outwit your\n" +
                "opponents by making them go bankrupt while purchasing properties\n" +
                "around the board. Spend wisely and aim for a TOTAL MONOPOLY.");
        System.out.println("-------------------------------------------------------------");
    }

    /**
     * Displays the status of a player trying to pay their fees.
     * @author Kyra Lothrop 101145872
     * @param boardCell property being rented, BoardCell
     * @param player player paying rent, Player
     * @param result if the player can afford rent, boolean
     */
    private void handlePayFees(BoardCell boardCell, Player player, int fees,  boolean result) {
        if (result){
            System.out.printf("You have successfully paid %d$ to %s\n",
                    fees,
                    boardCell.getOwner().getIconName().toUpperCase());
        }
        else{
            System.out.printf("You cannot currently pay fees to %s. You must pay %d$ and you only have %d$ remaining\n",
                    boardCell.getOwner().getIconName(),
                    fees,
                    player.getCash());
        }
    }

    /**
     * Displays a breaker to indicate change of turn.
     * @author Owen VanDusen 101152022
     */
    private void handleCurrentPlayerChange() {
        System.out.println("\n+++++++++Passing Dice To Next Player+++++++++");
    }

    /**
     * Displays a message informing that a player chose to forfeit the game.
     * @author Sarah Chow 101143033
     * @param player player forfeiting, Player
     */
    private void handleForfeitedPlayer(Player player) {
        System.out.printf("Player %s has forfeited the game!\n", player.getIconName().toUpperCase());
    }

    /**
     * Displays the end of game statistics and the ordered rank of all players.
     * @author Bardia Parmoun 101143006
     * @param players list of players in the order they forfeit the game, List<Player>
     */
    private void handleWinner(List<Player> players) {
        System.out.println("-------------------------------------------------------------");
        System.out.println("GAME OVER");
        players.sort(Comparator.comparingInt(Player::getRank));

        System.out.printf("Player %s won the game!\n", players.get(0).getIconName().toUpperCase());
        for (Player player: players){
            if (player.isBankrupt()) {
                System.out.printf("Player %s has the rank %d\n", player.getIconName().toUpperCase(), player.getRank());
            }
        }
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