package de.vfh.pressanykey.supertetris.game;

import com.sun.deploy.nativesandbox.NativeSandboxBroker;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Dialog;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.util.*;

/**
 * Game logic
 */
public class Board {

    /**
     * Hidden rows over the board (for new spawning stones)
     */
    private static final int BOARD_HIDDEN = 2;

    /**
     * Width of board matrix
     */
    private int boardWidth = 10;

    /**
     * Height of board matrix
     */
    private int boardHeight = 20;

    /**
     * Board matrix holding all the blocks of the stones
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
     * Current stone
     */
    private Stone currentStone;

    /**
     * Next stone
     */
    private Stone nextStone;

    /**
     * Delay for timer for down moving in ms
     */
    private int downTimerDelay = 0;

    /**
     * Timer for down moving
     */
    private Timeline downTimer;

    /**
     * Reference to the BoardPane (view)
     */
    private BoardPane boardPane;

    /**
     * Listeners for Board events
     */
    private List<BoardListener> boardListeners = new ArrayList<>();

    /**
     * How many rows to add to the bottom (queue)
     */
    private Integer rowsToAdd;

    /**
     * Constructor
     */
    public Board() {
        downTimer = new Timeline();
        downTimer.setCycleCount(Animation.INDEFINITE);
        rowsToAdd = 0;
    }

    /**
     * Set new BoardPane (view)
     * @param boardPane
     */
    public void setBoardPane(BoardPane boardPane) {
        this.boardPane = boardPane;
    }

    /**
     * Spawn a new stone on top
     */
    public void spawnStone() {

        if(nextStone == null) {
            nextStone = Stone.getRandom(boardPane.getBlockSize());
        }
        currentStone = nextStone;
        nextStone = Stone.getRandom(boardPane.getBlockSize());
        notifyNext(nextStone);

        boardPane.getChildren().add(currentStone);

        x = (matrix[0].length - currentStone.getMatrix().length) / 2;
        y = 0;

        move();

        notifySpawn(currentStone);
    }

    /**
     * Move current stone down
     */
    private void moveDown() {
        y++;
        if(isCrossing()) {
            y--;
            if(y <= 1) {
                // game over
                gameOver();
                return;
            }
            mergeStone();
            spawnStone();
        } else {
            move();
            notifyMove();
        }
    }

    /**
     * Move the stone
     * @param key Represents the direction: left/right/down or up for rotate
     */
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

    /**
     * Check if there is enough space for the stone to rotate
     * @return true, if ok / false, if not
     */
    private boolean tryRotate() {
        int[][] oldMatrix = currentStone.getMatrix();
        int[][] newMatrix = Stone.rotateMatrix(oldMatrix);
        if(isCrossing(newMatrix)) {
            return false;
        }
        return true;
    }

    /**
     * Move the stone in the view
     */
    private void move() {
        currentStone.setTranslateX(x * boardPane.getBlockSize().get());
        currentStone.setTranslateY((y - BOARD_HIDDEN) * boardPane.getBlockSize().get());
    }

    /**
     * Check if the currentStone is crossing other stones or the border
     * @return is crossing or not
     */
    private boolean isCrossing(int[][] newMatrix) {
        int x;
        int y;
        for(int i = 0; i < newMatrix.length; i++) {
            y = i + this.y - BOARD_HIDDEN;
            for(int j = 0; j < newMatrix[0].length; j++) {
                x = j + this.x;
                if(newMatrix[i][j] == 1) {
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

    /**
     * Check if the currentStone is crossing other stones or the border
     * @return is crossing or not
     */
    private boolean isCrossing() {
        return isCrossing(currentStone.getMatrix());
    }

    /**
     * Merge stone with board matrix
     */
    private void mergeStone() {

        Map<Integer, Boolean> rowsCompleted = new HashMap();
        boolean hasBlockInRow;
        int x;
        int y;

        // walk through all blocks of this stone
        for(int i = 0; i < currentStone.getMatrix().length; i++) {

            hasBlockInRow = false;
            y = this.y + i - BOARD_HIDDEN;

            for (int j = 0; j < currentStone.getMatrix()[0].length; j++) {

                if(currentStone.getMatrix()[i][j] == 1) {
                    hasBlockInRow = true;
                    x = this.x + j;

                    // create new rectangle for this block
                    Rectangle rectangle = Stone.createBlock(x, y, boardPane.getBlockSize(), currentStone.getColor());

                    // add rectangle to the matrix and to the boardPane
                    matrix[y][x] = rectangle;
                    boardPane.getChildren().add(rectangle);
                }
            }

            // check if row is completed
            if(hasBlockInRow) {
                rowsCompleted.put(y, true);
                for(int k = 0; k < matrix[y].length; k++) {
                    if(matrix[y][k] == null) {
                        rowsCompleted.put(y, false);
                        break;
                    }
                }
            }

        }

        // remove currentStone
        boardPane.getChildren().remove(currentStone);
        currentStone = null;
        notifyDropped();

        // remove completed rows
        removeRows(rowsCompleted.entrySet()
                .stream()
                .filter(map -> map.getValue()) // get only where value==true
                .map(e -> e.getKey()) // get the keys only
                .mapToInt(e -> (int)e) // cast to int
                .toArray()
        );

    }

    /**
     * Remove rows from board
     * @param rows row numbers to remove
     */
    private void removeRows(int[] rows) {
        if(rows.length == 0) {
            return;
        }
        boolean isRowEmpty;
        for(int r = 0; r < rows.length; r++) {
            for(int i = rows[r]; i > 0; i--) {
                isRowEmpty = true;
                for(int j = 0; j < matrix[0].length; j++) {
                    if(i == rows[r] && matrix[i][j] != null) {
                        boardPane.getChildren().remove(matrix[i][j]);
                    }
                    matrix[i][j] = matrix[i-1][j];
                    if(matrix[i][j] != null) {
                        isRowEmpty = false;
                        matrix[i][j].setTranslateY(boardPane.getBlockSize().doubleValue() * i);
                    }
                }
                if(isRowEmpty) {
                    break;
                }
            }
        }

        notifyRowDeleted(rows.length);
    }

    /**
     * Adds rows to the bottom of the board with random hole
     * @param count number of rows to add
     */
    public void addRandomRows(int count) {
        synchronized(rowsToAdd) {
            rowsToAdd += count;
        }
    }

    /**
     * Adds row to the bottom of the board with random hole
     */
    private void addRandomRow() {

        boolean isCrossingTop = false;
        int count = 1;

        // move every row [count] rows up
        for(int i = 0; i < matrix.length - count; i++) {
            for(int j = 0; j < matrix[0].length; j++) {

                // if there are blocks in the [count] most top rows we will touch top border
                if(i <= count && matrix[i][j] != null) {
                    isCrossingTop = true;
                    boardPane.getChildren().remove(matrix[i][j]);
                }

                matrix[i][j] = matrix[i+count][j];
                if(matrix[i][j] != null) {
                    matrix[i][j].setTranslateY(boardPane.getBlockSize().doubleValue() * i);
                }
            }
        }

        // add [count] random rows to the bottom
        int holePos;
        Random rand = new Random();
        for(int i = matrix.length - count; i < matrix.length; i++) {
            holePos = rand.nextInt(matrix[0].length);
            for(int j = 0; j < matrix[0].length; j++) {
                if(j == holePos) {
                    continue;
                }
                Rectangle rectangle = Stone.createBlock(j, i, boardPane.getBlockSize());
                matrix[i][j] = rectangle;
                boardPane.getChildren().add(rectangle);
            }
        }

        // if we touch top border the game is lost
        if(isCrossingTop) {
            gameOver();
        }
    }

    /**
     * Set new timer delay for falling blocks
     * @param delay Delay in ms
     */
    public void setDownTimer(int delay) {
        // start the timer for down moving
        if(downTimerDelay == delay) {
            return;
        }
        downTimerDelay = delay;
        downTimer.stop();
        downTimer.getKeyFrames().setAll(new KeyFrame(Duration.millis(delay), ae -> {
            doTick();
        }));
        downTimer.play();
    }

    /**
     * Run on every timer tick
     */
    private void doTick() {
        moveDown();
        if(rowsToAdd > 0) {
            addRandomRow();
            rowsToAdd--;
        }
    }

    private void gameOver() {
        stop();
        notifyGameover();
    }

    /**
     * Start game
     */
    public void start() {
        spawnStone();
        setDownTimer(Scores.getSpeed(1));
    }

    /**
     * Stop game
     */
    public void stop() {
        downTimer.stop();
    }

    /**
     * Pause game
     */
    public void pause() {
        downTimer.pause();
    }

    /**
     * Resume game
     */
    public void play() {
        downTimer.play();
    }

    /**
     * Add listener to Board events
     * @param boardListener
     */
    public void addBoardListener(BoardListener boardListener) {
        boardListeners.add(boardListener);
    }

    /**
     * Remove listener from Board events
     * @param boardListener
     */
    public void removeBoardListener(BoardListener boardListener) {
        boardListeners.remove(boardListener);
    }

    /**
     * Notify all listeners on game over
     */
    private void notifyGameover() {
        for (BoardListener boardListener : boardListeners) {
            boardListener.onGameover();
        }
    }

    /**
     * Notify all listeners on stone dropped
     */
    private void notifyDropped() {
        for (BoardListener boardListener : boardListeners) {
            boardListener.onDropped();
        }
    }

    /**
     * Notify all listeners on row deleted
     */
    private void notifyRowDeleted(int count) {
        for (BoardListener boardListener : boardListeners) {
            boardListener.onRowDeleted(count);
        }
    }

    /**
     * Notify all listeners on stone moved
     */
    private void notifyMove() {
        for (BoardListener boardListener : boardListeners) {
            boardListener.onMove();
        }
    }

    /**
     * Notify all listeners on stone rotated
     */
    private void notifyRotate() {
        for (BoardListener boardListener : boardListeners) {
            boardListener.onRotate();
        }
    }

    /**
     * Notify all listeners on stone spawn
     */
    private void notifySpawn(Stone stone) {
        for (BoardListener boardListener : boardListeners) {
            boardListener.onSpawn(stone);
        }
    }

    /**
     * Notify all listeners on next stone changed
     */
    private void notifyNext(Stone stone) {
        for (BoardListener boardListener : boardListeners) {
            boardListener.onNext(stone);
        }
    }

}
