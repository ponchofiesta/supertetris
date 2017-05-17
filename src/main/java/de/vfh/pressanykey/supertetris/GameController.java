package de.vfh.pressanykey.supertetris;

import javafx.application.Platform;

/**
 * The Game Controller
 */
public class GameController {

    private static GameController gameController;

    private final Board board;

    /**
     * constructor
     */
    public GameController() {
        this.board = new Board();
    }

//    public static GameController getInstance() {
//        if(gameController == null) {
//            gameController = new GameController();
//        }
//        return gameController;
//    }

    /**
     * get the game board
     * @return game board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * start game
     */
    public void start() {
        Platform.runLater(() -> {
            board.start();
            //TODO: scores, audio, other things...
        });
    }

    /**
     * stop game
     */
    public void stop() {
        board.stop();
    }
}
