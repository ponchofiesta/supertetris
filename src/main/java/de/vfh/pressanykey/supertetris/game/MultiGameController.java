package de.vfh.pressanykey.supertetris.game;

import de.vfh.pressanykey.supertetris.SupertetrisApp;
import de.vfh.pressanykey.supertetris.network.ClientInterface;
import de.vfh.pressanykey.supertetris.network.PlayerClient;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Claudia Kutter, Michael Richter
 */
public class MultiGameController extends GameController {

    private ClientInterface client;
    protected MultiBoardViewController view;
    // Attributes for opponent's board
    private final OpponentBoard oppBoard;
    private BoardPane oppBoardPane;
    List<HashMap<String, Object>> dropStoneMatrix = new ArrayList<>();
    /**
     * Constructor
     */
    public MultiGameController() {
        super();
        this.client = ClientInterface.getInstance(PlayerClient.getInstance());

        // Initialize opponent's board
        this.oppBoardPane = new BoardPane(BOARD_WIDTH, BOARD_HEIGHT);
        this.oppBoard = new OpponentBoard();
        oppBoard.setBoardPane(oppBoardPane);

        // Check what's happening on my own board and send it to the server
        board.addBoardListener(new BoardListener() {

            // Send movements of stone to the server
            @Override
            void onMove() {
                client.sendStonePosition(board.getCurrentStone(), board.getStoneX(), board.getStoneY());
            }

            // Send rotation of stone to the server
            @Override
            void onRotate() {
                client.sendStonePosition(board.getCurrentStone(), board.getStoneX(), board.getStoneY());
            }

            // Send the number of deleted rows and the new dropped stone matrix to the server
            @Override
            void onRowDeleted(int count) {
                client.sendDeletedRows(count);
                client.sendDroppedStones(board.getDroppedStones());
            }

            // Send the dropped stone matrix to the server
            @Override
            void onDropped() {
                client.sendDroppedStones(board.getDroppedStones());
            }

            // Send the new dropped stone matrix to the server after a row has been added on the bottom
            @Override
            void onRowAdded() {
                client.sendDroppedStones(board.getDroppedStones());
            }

            // Send a game over notification to the server
            @Override
            void onGameover() {
                client.sendGameOver();
                stopwatch.stop();
                board.stop();
            }

        });

        // Send score changes to the server
        scores.addObserver((o, arg) -> client.sendScore(scores));

        // Send logout if connection was established and window is closed
        Stage stage = SupertetrisApp.getPrimaryStage();
        Window window = stage.getScene().getWindow();
        window.setOnCloseRequest(event -> client.sendLogout());

    }

    /**
     * Gets the board pane for the opponent's board
     * @return Opponent's board pane
     */
    public BoardPane getOppBoardPane() {
        return oppBoardPane;
    }


    /**
     * Stops the game and sends a logout message to the server.
     */
    public void stop() {
        client.sendLogout();
        stopwatch.stop();
        board.stop();
    }


    /**
     * Pauses the game by pausing the stopwatch and stopping the board. Informs the server of the pause.
     * @param informOpponent  Whether the opponent should be informed of the pause. This is not necessary if the
     *                         opponent has initiated the pause himself.
     */
    public void pause(Boolean informOpponent) {
        if(isPaused) {
            board.play();
        } else {
            board.pause();
        }
        stopwatch.pause();
        isPaused = !isPaused;
        if(informOpponent) {
            client.sendPause(isPaused);
        }
    }


    /**
     * Sets the view controller to use
     * @param view
     */
    public void setView(MultiBoardViewController view) {
        this.view = view;
    }


    /**
     * Adds rows with a random hole at the bottom of the board
     * @param rows Number of rows to add
     */
    public void addRows(int rows) {
        board.addRandomRows(rows);
    }


    /**
     * Redraws the matrix of dropped stones on the opponent's board
     * @param dropStoneMatrix Matrix of all dropped stones
     */
    public void redrawOppStoneMatrix(List<HashMap<String, Object>> dropStoneMatrix) {
        this.dropStoneMatrix = dropStoneMatrix;
        oppBoard.redrawDroppedStones(dropStoneMatrix);
    }


    /**
     * Redraws the currently falling stone on the opponent's board
     * @param fallingStone Currently falling stone
     * @param posX  x-position of the stone
     * @param posY  y-position of the stone
     */
    public void redrawFallingStone(List<HashMap<String, Object>> fallingStone, int posX, int posY) {
        oppBoard.redrawDroppedStones(dropStoneMatrix);
        oppBoard.redrawStone(fallingStone, posX, posY);
    }


}
