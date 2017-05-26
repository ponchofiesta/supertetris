package de.vfh.pressanykey.supertetris;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Observable;

/**
 * game logic
 */
public class Board {

    private static final int BOARD_HIDDEN = 2;

    private int boardWidth = 10;
    private int boardHeight = 20;

    /**
     * board matrix holding all the blocks of the stones
     */
    private final Rectangle[][] matrix = new Rectangle[boardHeight][boardWidth];

    /**
     * X position of the matrix of the currentStone
     */
    private int x = 0;

    /**
     * Y position of the matrix of the currentStone
     */
    private int y = 0;

    /**
     * the current stone
     */
    private Stone currentStone;

    /**
     * heartbeat ticker
     */
    private Timeline downTimer;

    private BoardPane boardPane;

    public void setBoardPane(BoardPane boardPane) {
        this.boardPane = boardPane;
    }

    /**
     * spawn a new stone on top
     */
    public void spawnStone() {

        currentStone = Stone.getRandom(boardPane.getBlockSize());

        boardPane.getChildren().add(currentStone);

        x = (matrix[0].length - currentStone.getMatrix().length) / 2;
        y = 0;

        move();

        //moveDown();
    }

    /**
     * spawn some stones for testing
     * TODO: remove finally
     */
    public void spawnRandomStones() {
        int i = -10;
        int x;
        int y;
        while(i++ < 10) {
            Stone stone = Stone.getRandom(boardPane.getBlockSize());
            boardPane.getChildren().add(stone);
            x = (int) (i * boardPane.getBlockSize().get());
            y = (int) ((i - Board.BOARD_HIDDEN) * boardPane.getBlockSize().get());
            stone.setTranslateX(x);
            stone.setTranslateY(y);
            System.out.println(x+" = "+i+"*"+boardPane.getBlockSize().get());
            System.out.println(y+" = ("+i+"-"+Board.BOARD_HIDDEN+")*"+boardPane.getBlockSize().get());
        }
    }

    /**
     * moveStone currentStone down
     */
    private void moveDown() {
        y++;
        move();
        onStoneMoved(currentStone);
    }

    public void moveStone(KeyCode key) {
        if(key == KeyCode.LEFT) {
            x--;
        } else if(key == KeyCode.RIGHT) {
            x++;
        } else if(key == KeyCode.UP) {
            //TODO: turn stone
        } else if(key == KeyCode.DOWN) {
            //TODO: moveStone down stone fast
        }
        move();
    }

    private void move() {
        currentStone.setTranslateX(x * boardPane.getBlockSize().get());
        currentStone.setTranslateY((y - Board.BOARD_HIDDEN) * boardPane.getBlockSize().get());
    }

    /**
     * Check if the currentStone is crossing other stones or the border
     * @return is crossing or not
     */
    private boolean isCrossing() {
        //TODO: implement isCrossing()
        return false;
    }

    /**
     * merge stone with board matrix
     */
    private void mergeStone() {
        //TODO: implement mergeStone()
    }

    /**
     * stone moved event
     */
    private void onStoneMoved(Stone stone) {
        //TODO: notify listeners
    }

    /**
     * stone dropped event
     */
    private void onStoneDropped(Stone stone) {
        //TODO: notify listeners
    }

    /**
     * run on every timer tick
     */
    private void doTick() {
        moveDown();
        if(isCrossing()) {
            // drop stone
            mergeStone();
            // next stone
            spawnStone();
        }
    }

    /**
     * start game
     */
    public void start() {

        spawnStone();
        //spawnRandomStones();

        // start the timer for down moving
        downTimer = new Timeline(new KeyFrame(Duration.millis(1000), ae -> {
            doTick();
        }));
        downTimer.setCycleCount(Animation.INDEFINITE);
        downTimer.play();
    }

    /**
     * stop game
     */
    public void stop() {
        downTimer.stop();
    }

    public void pause() {
        downTimer.pause();
    }
    public void play() {
        downTimer.play();
    }
}
