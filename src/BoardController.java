import java.util.*;

public class BoardController  {
    private BoardFrame boardFrame;
    private BoardModel boardModel;
    private boolean gameFinish;

    private int turnsPassed;
    private int numPlayers;

    private ArrayList<String> remainingIcons;

    //temporary stuff until GUI
    private ArrayList<String> activeIcons;
    private HashMap<String, Integer> iconPosition;

    public BoardController(){
        boardFrame = new BoardFrame();
        boardModel = new BoardModel();
        boardModel.addBoardView(boardFrame);

        turnsPassed = 0;

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

        activeIcons = new ArrayList<>();
        iconPosition = new HashMap<>();
    }

    private void initiatePlayers(){
        String icon;

        Scanner scan1 = new Scanner(System.in);
        System.out.println("How many people will be playing?");
        numPlayers = scan1.nextInt();

        for (int i = 0; i < numPlayers; i++){
            Scanner scan2 = new Scanner(System.in);
            System.out.printf("What's player %d's name?\n", i + 1);
            String name = scan2.nextLine();

            do {
                Scanner scan3 = new Scanner(System.in);
                System.out.printf("Choose an icon: %s\n", getListOfIconsUpper());
                icon = scan2.nextLine().toLowerCase();
            } while (!(remainingIcons.contains(icon)));

            boardModel.addPlayer(name, icon);
            activeIcons.add(icon);
            iconPosition.put(icon, 0); //All players start on the GO tile of the board
            remainingIcons.remove(icon);
        }

    }

    public String getListOfIconsUpper(){

        String upperCaseIcon = "";

        for (String icon : remainingIcons){
            upperCaseIcon += icon.toUpperCase() + " ";
        }
        return upperCaseIcon;
    }

    private void play(){
        initiatePlayers(); //TODO
        int turn;


        while(!gameFinish){
            //which player
            turn = turnsPassed % numPlayers;
            String currentIcon = activeIcons.get(turn);

            System.out.printf("It is currently the turn of: %s\n they are on tile %d\n\n",currentIcon,iconPosition.get(currentIcon));

            turnsPassed++;
        }
    }

    public static void main(String[] args) {
        BoardController boardC;
        String startGame;

        do {
            Scanner scan1 = new Scanner(System.in);
            System.out.println("Would you like to start a game? start / quit");
            startGame = scan1.nextLine().toLowerCase();
        } while(!(startGame.equals("start") || (startGame.equals("quit"))));

        if(startGame.equals("start")){
            boardC = new BoardController();
            boardC.play();
        }

        System.out.println("See you later");
        System.exit(0);
    }
}
