package de.vfh.pressanykey.supertetris;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

/**
 * The Game Controller
 */
public class GameController {

    private static GameController gameController;

    private BoardViewController view;
    private final Board board;

    private BoardPane boardPane;

    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGHT = 20;

    private boolean isPaused = false;

    private Timeline stopwatchTimer;
    private long stopwatchStart = 0;
    private long stopwatchPause = 0;

    /**
     * constructor
     */
    public GameController() {
        this.boardPane = new BoardPane(BOARD_WIDTH, BOARD_HEIGHT);
        this.board = new Board();
        board.setBoardPane(boardPane);


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

    public BoardPane getBoardPane() {
        return boardPane;
    }

    public void setView(BoardViewController view) {
        this.view = view;
    }

    /**
     * start game
     */
    public void start() {
        Platform.runLater(() -> {
            board.start();
            //TODO: scores, audio, other things...

            // start stopwatch
            stopwatchStart = System.currentTimeMillis();
            stopwatchTimer = new Timeline(new KeyFrame(Duration.millis(1000), ae -> {
                long nowTime = System.currentTimeMillis();
                double diffTime = (nowTime - stopwatchStart) / 1000.0;
                String time;
                int minutes;
                int seconds;
                minutes = (int) diffTime/60;
                seconds = (int) diffTime%60;
                time = (minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + seconds;
                /// Variante mit format, soll allerdings langsamer sein:
                //time = String.format("%02d:%02d", minutes, seconds);
                view.setStopwatchText(time);
            }));
            stopwatchTimer.setCycleCount(Animation.INDEFINITE);
            stopwatchTimer.play();

            // register key presses
            boardPane.getScene().setOnKeyPressed(e -> {
                KeyCode key = e.getCode();
                if(key == KeyCode.LEFT || key == KeyCode.RIGHT || key == KeyCode.DOWN || key == KeyCode.UP) {
                    board.moveStone(key);
                }
            });

        });
    }

    public void pause() {
        if(isPaused) {
            stopwatchStart = System.currentTimeMillis() - (stopwatchPause - stopwatchStart);
            stopwatchTimer.play();
            board.play();
        } else {
            stopwatchTimer.pause();
            stopwatchPause = System.currentTimeMillis();
            board.pause();
        }
        isPaused = !isPaused;
    }

    /**
     * stop game
     */
    public void stop() {
        stopwatchTimer.stop();
        board.stop();
    }


}
