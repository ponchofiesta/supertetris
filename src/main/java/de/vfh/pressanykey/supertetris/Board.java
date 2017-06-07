package de.vfh.pressanykey.supertetris;

import com.sun.javafx.scene.traversal.Direction;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.awt.event.ActionListener;
import java.util.*;

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

    private List<BoardListener> boardListeners = new ArrayList<>();

    private final static int[] levelSpeed = new int[] {
            1000,
            800,
            700,
            600,
            500,
            400,
            300
    };
    private final static int[] levelPointFactor = new int[] {
            1,
            2,
            4,
            8,
            16,
            32,
            64
    };
    private int points = 0;

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
     * moveStone currentStone down
     */
    private void moveDown() {
        y++;
        if(isCrossing()) {
            y--;
            if(y <= 1) {
                // game over
                stop();
                notifyGameover();
                return;
            }
            mergeStone();
            spawnStone();
        } else {
            move();
            notifyMove();
        }
    }

    public void moveStone(KeyCode key) {
        int oldX = x;
        int oldY = y;
        if(key == KeyCode.LEFT) {
            x--;
        } else if(key == KeyCode.RIGHT) {
            x++;
        } else if(key == KeyCode.UP) {
            if(tryRotate()) {
                currentStone.rotate();
            }
        } else if(key == KeyCode.DOWN) {
            moveDown();
        }
        if(isCrossing()) {
            x = oldX;
            y = oldY;
        } else {
            move();
        }
    }

    private boolean tryRotate() {
        int[][] oldMatrix = currentStone.getMatrix();
        int[][] newMatrix = Stone.rotateMatrix(oldMatrix);
        if(isCrossing(newMatrix)) {
            return false;
        }
        return true;
    }

    private void move() {
        currentStone.setTranslateX(x * boardPane.getBlockSize().get());
        currentStone.setTranslateY((y - BOARD_HIDDEN) * boardPane.getBlockSize().get());
    }

    /**
     * Check if the currentStone is crossing other stones or the border
     * @return is crossing or not
     */
    private boolean isCrossing(int[][] newMatrix) {
        for(int i = 0; i < newMatrix.length; i++) {
            for(int j = 0; j < newMatrix[0].length; j++) {
                if(newMatrix[i][j] == 1) {
                    final int y = i + this.y - BOARD_HIDDEN;
                    final int x = j + this.x;
                    if(y >= matrix.length // bottom border
                            || x < 0 // left border
                            || x >= matrix[0].length // right border
                            || y >= 0 && matrix[y][x] != null // dropped block
                            ) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isCrossing() {
        return isCrossing(currentStone.getMatrix());
    }

    /**
     * merge stone with board matrix
     */
    private void mergeStone() {

        // merge stone with board and check if rows are completed
        // walk through all blocks of this stone
        Map<Integer, Boolean> rowsCompleted = new HashMap();
        for(int i = 0; i < currentStone.getMatrix().length; i++) {
            for (int j = 0; j < currentStone.getMatrix()[0].length; j++) {
                if(currentStone.getMatrix()[i][j] == 1) {
                    final int x = this.x + j;
                    final int y = this.y + i - BOARD_HIDDEN;

                    // create new rectangle for this block
                    final Rectangle rectangle = new Rectangle();
                    rectangle.setWidth(boardPane.getBlockSize().doubleValue());
                    rectangle.setHeight(boardPane.getBlockSize().doubleValue());
                    rectangle.setTranslateY(boardPane.getBlockSize().doubleValue() * y);
                    rectangle.setTranslateX(boardPane.getBlockSize().doubleValue() * x);
                    rectangle.setFill(currentStone.getColor());
                    rectangle.setArcHeight(7);
                    rectangle.setArcWidth(7);

                    // add rectangle to the matrix and to the boardPane
                    matrix[y][x] = rectangle;
                    boardPane.getChildren().add(rectangle);

                    // check if row is completed
                    if(!rowsCompleted.containsKey(y)) {
                        rowsCompleted.put(y, true);
                        for(int k = 0; k < matrix[y].length; k++) {
                            if(matrix[y][k] != null) {
                                rowsCompleted.put(y, false);
                                break;
                            }
                        }
                    }

                }
            }
        }

        // remove currentStone
        boardPane.getChildren().remove(currentStone);
        currentStone = null;
        notifyDropped();

        removeRows(rowsCompleted.keySet().toArray(new Integer[0]));

    }

    private void removeRows(Integer[] rows) {
        //TODO: implement removeRows (not ready)
        if(rows.length == 0) {
            return;
        }
        for(int i = rows[rows.length-1]; i > 0; i--) {
            for(int j = 0; j < matrix[0].length; j++) {

            }
        }
        notifyRowDeleted(rows.length);
    }


    /**
     * run on every timer tick
     */
    private void doTick() {
        moveDown();
    }

    /**
     * start game
     */
    public void start() {

        spawnStone();

        // start the timer for down moving
        downTimer = new Timeline(new KeyFrame(Duration.millis(300), ae -> {
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

    public void addBoardListener(BoardListener boardListener) {
        boardListeners.add(boardListener);
    }

    public void removeBoardListener(BoardListener boardListener) {
        boardListeners.remove(boardListener);
    }

    private void notifyGameover() {
        for (BoardListener boardListener : boardListeners) {
            boardListener.onGameover();
        }
    }
    private void notifyDropped() {
        for (BoardListener boardListener : boardListeners) {
            boardListener.onDropped();
        }
    }
    private void notifyRowDeleted(int count) {
        for (BoardListener boardListener : boardListeners) {
            boardListener.onRowDeleted(count);
        }
    }
    private void notifyMove() {
        for (BoardListener boardListener : boardListeners) {
            boardListener.onMove();
        }
    }
    private void notifyRotate() {
        for (BoardListener boardListener : boardListeners) {
            boardListener.onRotate();
        }
    }
    private void notifySpawn() {
        for (BoardListener boardListener : boardListeners) {
            boardListener.onSpawn();
        }
    }

}
