package de.vfh.pressanykey.supertetris;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * The play board
 */
public class Board extends StackPane {

    private static final int BOARD_HIDDEN = 2;
    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGHT = 20;

    /**
     * size of one single block
     */
    private DoubleProperty blockSize = new SimpleDoubleProperty();

    /**
     * board matrix holding all the blocks of the stones
     */
    private final Rectangle[][] matrix = new Rectangle[BOARD_HEIGHT][BOARD_WIDTH];

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

    /**
     * constuctor
     */
    public Board() {

        // bind width changes to blocksize object
        blockSize.bind(new DoubleBinding() {
            {
                super.bind(widthProperty());
            }

            @Override
            protected double computeValue() {
                return getWidth() / BOARD_WIDTH;
            }
        });
    }

    /**
     * get size of one single block
     * @return
     */
    public double getBlockSize() {
        return blockSize.get();
    }

    /**
     * spawn a new stone on top
     */
    public void spawnStone() {

        currentStone = Stone.getRandom(blockSize);

        getChildren().add(currentStone);

        int x = (matrix[0].length - currentStone.getMatrix().length) / 2;
        int y = 0;

        currentStone.setTranslateY((y - Board.BOARD_HIDDEN) * getBlockSize());
        currentStone.setTranslateX(x * getBlockSize());

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
            Stone stone = Stone.getRandom(blockSize);
            getChildren().add(stone);
            x = (int) (i * getBlockSize());
            y = (int) ((i - Board.BOARD_HIDDEN) * getBlockSize());
            stone.setTranslateY(y);
            stone.setTranslateX(x);
            System.out.println(x+" = "+i+"*"+getBlockSize());
            System.out.println(y+" = ("+i+"-"+Board.BOARD_HIDDEN+")*"+getBlockSize());
        }
    }

    /**
     * move currentStone down
     */
    private void moveDown() {
        x++;
        currentStone.setTranslateY(x * getBlockSize() + getBlockSize());
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

}
