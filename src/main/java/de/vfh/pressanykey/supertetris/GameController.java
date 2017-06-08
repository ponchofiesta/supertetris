package de.vfh.pressanykey.supertetris;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

import javax.swing.event.ChangeListener;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

/**
 * The Game Controller
 */
public class GameController {

    //private static GameController gameController;

    private BoardViewController view;
    private final Board board;

    private BoardPane boardPane;

    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGHT = 20;

    private boolean isPaused = false;

    private Stopwatch stopwatch;

    private Scores scores;


    /**
     * constructor
     */
    public GameController() {
        this.boardPane = new BoardPane(BOARD_WIDTH, BOARD_HEIGHT);
        this.board = new Board();
        board.setBoardPane(boardPane);
        stopwatch = new Stopwatch();
        scores = new Scores();

        board.addBoardListener(new BoardListener() {
            @Override
            public void onGameover() {
                stop();
                //TODO: show points or something
            }

            @Override
            public void onRowDeleted(int count) {
                scores.rowsDeleted(count);
            }
        });

        scores.addObserver((o, arg) -> {
            //TODO: implement score and level update in BoardViewController
        });

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
//    public Board getBoard() {
//        return board;
//    }

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
            stopwatch.start();

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
            stopwatch.pause();
            board.play();
        } else {
            stopwatch.pause();
            board.pause();
        }
        isPaused = !isPaused;
    }

    /**
     * stop game
     */
    public void stop() {
        stopwatch.stop();
        board.stop();
    }


    public void addStopwatchListener(Observer sl) {
        stopwatch.addObserver(sl);
    }


}
