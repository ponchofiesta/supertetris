package de.vfh.pressanykey.supertetris.game;

import de.vfh.pressanykey.supertetris.network.ClientInterface;


public class MultiGameController extends GameController {

    private ClientInterface client;


    protected final Board oppBoard;
    protected BoardPane oppBoardPane;

    protected static final int BOARD_WIDTH = 10;
    protected static final int BOARD_HEIGHT = 20;

    protected Scores oppScores;



    public MultiGameController() {
        super();
        this.client = ViewController.clientInterFace;

        // initialize opponent's board
        this.oppBoardPane = new BoardPane(BOARD_WIDTH, BOARD_HEIGHT);
        this.oppBoard = new Board();
        oppBoard.setBoardPane(oppBoardPane);
        oppScores = new Scores();


        // check what's happening on my own board and send it to the server
        board.addBoardListener(new BoardListener() {

            //TODO: onSpawn muss zunächst noch im Board aufgerufen werden, damit es feuern kann
            @Override
            void onSpawn(Stone stone) {
                System.out.println("new stone");
                client.sendStone(stone);
            }

            @Override
            void onMove() {
                // for testing:
//                byte[] data = "moved".getBytes();
//                client.send(data);
            }

        });
    }

    public Board getOppBoard() {
        return oppBoard;
    }
    public BoardPane getOppBoardPane() {
        return oppBoardPane;
    }


    public void stop() {
        client.sendLogout();
        stopwatch.stop();
        board.stop();
    }

}
