package de.vfh.pressanykey.supertetris.game;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

/**
 * The Game Controller
 * @author Michael Richter, Ute Mayer
 */
public class GameController {

    //private static GameController gameController;

    protected BoardViewController view;

    protected final Board board;

    protected BoardPane boardPane;

    protected static final int BOARD_WIDTH = 10;
    protected static final int BOARD_HEIGHT = 20;

    protected boolean isPaused = false;

    protected Stopwatch stopwatch;

    protected Scores scores;

    private String className;
    private MusicPlayer musicPlayer;

    /**
     * constructor
     */
    public GameController() {
        this.boardPane = new BoardPane(BOARD_WIDTH, BOARD_HEIGHT);
        this.board = new Board();
        board.setBoardPane(boardPane);
        stopwatch = new Stopwatch();
        scores = new Scores();
        className = this.getClass().getSimpleName();
        musicPlayer = new MusicPlayer();

        board.addBoardListener(new BoardListener() {
            @Override
            void onGameover() {
                // Due to inheritance we must handle gameover different for normal and multiplayer mode
                if(className.equals("GameController")) {
                    stop();
                    view.showGameOver(scores);
                }
                
                musicPlayer.startMusic("sounds/music.mp3", 15);
            }

            @Override
            void onDropped() {
                musicPlayer.startMusic("sounds/sfx_sounds_powerup6.wav", 1);
            }

            @Override
            void onRotate() {
                musicPlayer.startMusic("sounds/sfx_sounds_interaction16.wav", 1);
            }

            @Override
            void onRowDeleted(int count) {
                scores.rowsDeleted(count);
                musicPlayer.startMusic("sounds/sfx_sounds_powerup6.wav", 1);
            }
        });

        scores.addObserver((o, arg) -> {
            board.setDownTimer(scores.getSpeed());
        });

    }

    /**
     * get the game board
     * @return game board
     */
    public Board getBoard() {
        return board;
    }

    public Scores getScores() {
        return scores;
    }

    public Stopwatch getStopwatch() {
        return stopwatch;
    }

//    public static GameController getInstance() {
//        if(gameController == null) {
//            gameController = new GameController();
//        }
//        return gameController;
//    }

    /**
     * Get BoardPane (view)
     * @return
     */
    public BoardPane getBoardPane() {
        return boardPane;
    }

    /**
     * Set the view controller to use
     * @param view
     */
    public void setView(BoardViewController view) {
        this.view = view;
    }


    /**
     * Start game
     */
    public void start() {
        Platform.runLater(() -> {
            board.start();

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

    /**
     * Pause game
     */
    public void pause() {
        if(isPaused) {
            board.play();
        } else {
            board.pause();
        }
        stopwatch.pause();
        isPaused = !isPaused;
    }

    /**
     * Stop game
     */
    public void stop() {
        stopwatch.stop();
        board.stop();
    }

}
