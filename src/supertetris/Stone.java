package supertetris;

import javafx.scene.paint.Color;

/**
 * A Stone
 */
public class Stone {

    private final Color color;

    private final int[][] matrix;

    private Stone(int[][] matrix, Color color) {
        this.color = color;
        this.matrix = matrix;
    }
}
