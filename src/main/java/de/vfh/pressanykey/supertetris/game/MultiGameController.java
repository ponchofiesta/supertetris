package de.vfh.pressanykey.supertetris.game;

import de.vfh.pressanykey.supertetris.network.ClientInterface;


public class MultiGameController extends GameController {

    private ClientInterface client;
    protected MultiBoardViewController view;
    // Attributes for opponent's board
    protected final Board oppBoard;
    protected BoardPane oppBoardPane;
    protected static final int BOARD_WIDTH = 10;
    protected static final int BOARD_HEIGHT = 20;

    /**
     * Constructor
     */
    public MultiGameController() {
        super();
        this.client = ViewController.clientInterFace;

        System.out.println("Multigameklasse: " + this.getClass().getName());

        // initialize opponent's board
        this.oppBoardPane = new BoardPane(BOARD_WIDTH, BOARD_HEIGHT);
        this.oppBoard = new Board();
        oppBoard.setBoardPane(oppBoardPane);

        // check what's happening on my own board and send it to the server
        board.addBoardListener(new BoardListener() {

//            Mögliche events:
//            onSpawn (=neuer Stein), onMove, onDropped, onRotate

            @Override
            void onRowDeleted(int count) {
                client.sendDeletedRows(count);
            }

            @Override
            void onGameover() {
                client.sendGameOver();
                stopwatch.stop();
                board.stop();
            }

        });

        // send score changes to the server
        scores.addObserver((o, arg) -> client.sendScore(scores));

    }

    public Board getOppBoard() {
        return oppBoard;
    }
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
     * @param informOppontent  Whether the opponent should be informed of the pause. This is not necessary if the
     *                         opponent has initiated the pause himself.
     */
    public void pause(Boolean informOppontent) {
        if(isPaused) {
            board.play();
        } else {
            board.pause();
        }
        stopwatch.pause();
        isPaused = !isPaused;
        if(informOppontent) {
            client.sendPause(isPaused);
        }
    }

    /**
     * Set the view controller to use
     * @param view
     */
    public void setView(MultiBoardViewController view) {
        this.view = view;
    }

    public void addRows(int rows) {
        board.addRandomRows(rows);
    }
}
