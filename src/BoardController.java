// Sarah Chow 101143033
// Owen VanDusen 101152022

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

    private List<Integer> roll(){
        Random rand = new Random();
        int die1 = rand.nextInt((6 - 1) + 1) + 1;
        int die2 = rand.nextInt((6 - 1) + 1) + 1;

        List<Integer> dice = new ArrayList<Integer>(Arrays.asList(die1, die2));

        return dice;
    }

    private void playerTurn(String currentIcon){
        List<Integer> dice = roll();
        int currentPosition;


        System.out.printf("You rolled a %d and a %d\n", dice.get(0), dice.get(0));

        if (dice.get(0)== dice.get(1)){
            System.out.println("You got a double!!");
        }

        currentPosition = iconPosition.get(currentIcon);
        iconPosition.replace(currentIcon, currentPosition + dice.get(0) + dice.get(1));
        currentPosition = iconPosition.get(currentIcon);

        System.out.printf("You landed on %s!\n", boardModel.getPropertyName(currentPosition));

        if (boardModel.isPropertyBought(currentPosition)){
            System.out.printf("It is owned by %s! You need to pay rent :( \n",
                    boardModel.getPropertyName(currentPosition),
                    Integer.valueOf(boardModel.getPropertyRent(currentPosition))
                    );
            if (!boardModel.payRent(currentPosition, currentIcon)){ // Rental payment did not go through
                System.out.println("You do not have enough money to pay rent! You're BOOTED!\n");
            }
        }
        else{
            Scanner scan1 = new Scanner(System.in);
            System.out.printf("Would you like to buy %s for %d?\n",
                    boardModel.getPropertyName(currentPosition),
                    boardModel.getPropertyRent(currentPosition));
            String answer = scan1.nextLine().toLowerCase();

            if (answer.equals("yes")){
                if(!boardModel.buyProperty(currentPosition, currentIcon)){
                    System.out.printf("You do not have enough money to buy this property!\n" +
                            "You have $%d left!\n", boardModel.getPlayerCash(currentIcon));
                }
            }
        }


    }

    private void play(){
        initiatePlayers();
        int turn;


        while(!gameFinish){
            //which player
            turn = turnsPassed % numPlayers;
            String currentIcon = activeIcons.get(turn);

            playerTurn(currentIcon);

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
