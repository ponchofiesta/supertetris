package supertetris;

import javafx.scene.shape.Rectangle;

/**
 * The play board
 */
public class Board {

    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGHT = 20;

    private final Rectangle[][] matrix = new Rectangle[BOARD_HEIGHT][BOARD_WIDTH];

}
