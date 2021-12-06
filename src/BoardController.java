import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;

/**
 * Group 3
 * SYSC 3110 - Milestone 3 BoardController Class
 *
 * This document is the BoardController, as per the MVC model, the BoardController handles user input and passes off
 * the information to the BoardModel class. This class has a boardModel and an array list of remaining icons.
 *
 * @author Sarah Chow 101143033
 * @author Kyra Lothrop 101145872
 * @author Bardia Parmoun 101143006
 * @author Owen VanDusen 101152022
 * @version 2.0
 */

public class BoardController extends WindowAdapter implements ActionListener, Serializable {
    /**
     * Keeps track of the board model.
     */
    private final BoardModel boardModel;

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
     * Listens for the user command buttons in the BoardFrame
     * @author Kyra Lothrop 101145872
     * @param e action event e for using the buttons, ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        boardModel.sendCommand(command);
    }

    /**
     * Listens for all the window event of minimizing the window.
     * @author Bardia Parmoun 101143006
     * @param we a window event for minimizing the window.
     */
    @Override
    public void windowDeiconified(WindowEvent we) {
        boardModel.sendCommand(BoardModel.Command.CHANGE_WINDOW.getStringCommand());
    }
}