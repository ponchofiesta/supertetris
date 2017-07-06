package de.vfh.pressanykey.supertetris.game;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

/**
 * A Stone
 * @author Michael Richter
 */
public class Stone extends Group {

    private static final Random RANDOM = new Random();

    /**
     * Board matrix
     */
    private int[][] matrix;

    private StoneType stoneType;

    public Color getColor() {
        return stoneType.color;
    }

    private ReadOnlyDoubleProperty blockSize = new SimpleDoubleProperty();

    private static final StoneType I = new StoneType(new int[][]{
        {0, 0, 0, 0},
        {1, 1, 1, 1},
        {0, 0, 0, 0},
        {0, 0, 0, 0}
    }, Color.CYAN);

    private static final StoneType J = new StoneType(new int[][]{
        {1, 0, 0},
        {1, 1, 1},
        {0, 0, 0}
    }, Color.DARKCYAN);

    private static final StoneType L = new StoneType(new int[][]{
        {0, 0, 1},
        {1, 1, 1},
        {0, 0, 0}
    }, Color.ORANGE);

    private static final StoneType O = new StoneType(new int[][]{
        {1, 1},
        {1, 1}
    }, Color.YELLOW);

    private static final StoneType S = new StoneType(new int[][]{
        {0, 1, 1},
        {1, 1, 0},
        {0, 0, 0}
    }, Color.GREENYELLOW);

    private static final StoneType T = new StoneType(new int[][]{
        {0, 1, 0},
        {1, 1, 1},
        {0, 0, 0}
    }, Color.FUCHSIA);

    private static final StoneType Z = new StoneType(new int[][]{
        {1, 1, 0},
        {0, 1, 1},
        {0, 0, 0}
    }, Color.ORANGERED);

    private static final StoneType[] STONE_TYPES = new StoneType[]{I, J, L, O, S, T, Z};

    /**
     * Constructor
     * @param stonetype which stone to create
     * @param blockSize size of an block
     */
    private Stone(StoneType stonetype, ReadOnlyDoubleProperty blockSize) {
        matrix = stonetype.matrix;
        stoneType = stonetype;
        this.blockSize = blockSize;
        // fill the matrix of the stone
        createBlocks();
    }

    /**
     * Get a random stone
     * @param size size of a single block
     * @return a random stone
     */
    public static Stone getRandom(ReadOnlyDoubleProperty size) {
        StoneType stonetype = STONE_TYPES[RANDOM.nextInt(STONE_TYPES.length)];

        return new Stone(stonetype, size);
    }

    /**
     * Get board matrix
     * @return
     */
    public int[][] getMatrix() {
        return matrix;
    }

    /**
     * Rotate a given matrix by 90deg
     * @param matrix The matrix to be rotated
     * @return The rotated matrix
     */
    public static int[][] rotateMatrix(int[][] matrix) {
        int[][] newMatrix = new int[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                newMatrix[j][matrix.length - 1 - i] = matrix[i][j];
            }
        }
        return newMatrix;
    }

    /**
     * Rotate the stone by 90deg
     */
    public void rotate() {
        matrix = rotateMatrix(matrix);
        getChildren().clear();
        createBlocks();
    }

    /**
     * Create all blocks for this stone
     */
    private void createBlocks() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {

                // the painted block
                Rectangle rectangle = createBlock(j, i, blockSize);

                // paint the blocks - or hide them
                if (matrix[i][j] == 1) {
                    rectangle.setFill(stoneType.color);
                } else {
                    rectangle.setOpacity(0);
                }

                // add block to the stone
                getChildren().add(rectangle);

            }
        }
    }

    /**
     * Create a single block
     * @param x X-position
     * @param y Y-position
     * @param blockSize Size of the block
     * @param color Color of the block
     * @return A rectangle representing the block
     */
    public static Rectangle createBlock(int x, int y, ReadOnlyDoubleProperty blockSize, Color color) {
        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(blockSize.doubleValue());
        rectangle.setHeight(blockSize.doubleValue());
        rectangle.setTranslateX(blockSize.doubleValue() * x);
        rectangle.setTranslateY(blockSize.doubleValue() * y);
        rectangle.setFill(color);
        rectangle.getStyleClass().add("block");
        return rectangle;
    }

    /**
     * Create a single block
     * @param x X-position
     * @param y Y-position
     * @param blockSize Size of the block
     * @return A rectangle representing the block
     */
    public static Rectangle createBlock(int x, int y, ReadOnlyDoubleProperty blockSize) {
        Color[] colors = new Color[]{
                Color.CYAN, Color.DARKCYAN, Color.ORANGE, Color.YELLOW, Color.GREENYELLOW, Color.BISQUE, Color.ORANGERED
        };
        Color color = colors[RANDOM.nextInt(colors.length)];
        return createBlock(x, y, blockSize, color);
    }

    /**
     * Stone base
     * maybe it could be an abstract class but we already extend Group
     */
    private static class StoneType {
        private final Color color;

        private final int[][] matrix;

        private StoneType(int[][] matrix, Color color) {
            this.color = color;
            this.matrix = matrix;
        }
    }
}
