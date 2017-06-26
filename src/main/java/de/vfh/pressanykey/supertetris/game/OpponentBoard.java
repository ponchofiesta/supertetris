package de.vfh.pressanykey.supertetris.game;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.*;

/**
 * Logic for the opponent's board which is only passive and used to draw the opponents game state on it
 * @author Claudia Kutter
 */
public class OpponentBoard {

    // Hidden rows on top of the board (for new spawning stones)
    private static final int BOARD_HIDDEN = 2;

    // Reference to the BoardPane (view)
    private BoardPane boardPane;


    /**
     * Set new BoardPane (view)
     * @param boardPane Board pane that is to be set
     */
    public void setBoardPane(BoardPane boardPane) {
        this.boardPane = boardPane;
    }


    /**
     * Loops through the matrix of dropped stones and draws it on the board
     * @param dropStoneMatrix Matrix containing all dropped stones
     */
    public void redrawDroppedStones(List<HashMap<String, Object>> dropStoneMatrix){
        clearBoard();
        for(HashMap<String, Object> droppedStone : dropStoneMatrix ){
            String color = droppedStone.get("color").toString();
            Color currentColor = Color.valueOf(color);
            currentColor = currentColor.darker().desaturate();
            drawDroppedStone((int) droppedStone.get("x"), (int) droppedStone.get("y"), currentColor);
        }
    }


    /**
     * Loops through the matrix the current stone and draws it to the board
     * @param fallingStone Currently falling stone
     * @param posX x-position of the stone
     * @param posY y-position of the stone
     */
    public void redrawStone(List<HashMap<String, Object>> fallingStone, int posX, int posY) {
        for(HashMap<String, Object> block : fallingStone ){
            String color = block.get("color").toString();
            Color currentColor = Color.valueOf(color);
            currentColor = currentColor.darker().desaturate();
            // Determine current position; y-position must be adjusted because the board has two invisible rows on top
            int currentX = (int) block.get("x") + posX;
            int currentY = (int) block.get("y") + posY - BOARD_HIDDEN;
            drawStone(currentX, currentY, currentColor);
        }
    }


    // Helper method for drawing the stones in the matrix to the board
    private void drawDroppedStone(int x, int y, Color color) {
        Rectangle rectangle = Stone.createBlock(x, y, boardPane.getBlockSize(), color);
        boardPane.getChildren().add(rectangle);
    }


    // Helper method for clearing the board before redrawing
    private void clearBoard() {
        boardPane.getChildren().clear();
    }


    // Helper method for drawing the stone
    private void drawStone(int x, int y, Color color) {
        Rectangle rectangle = Stone.createBlock(x, y, boardPane.getBlockSize(), color);
        boardPane.getChildren().add(rectangle);
    }

}
