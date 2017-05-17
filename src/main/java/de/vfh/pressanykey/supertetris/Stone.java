package de.vfh.pressanykey.supertetris;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

/**
 * A Stone
 */
public class Stone extends Group {

    private static final Random RANDOM = new Random();

    private final int[][] matrix;

    private StoneType stoneType;

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
    }, Color.BLUE);

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
    }, Color.PURPLE);

    private static final StoneType Z = new StoneType(new int[][]{
        {1, 1, 0},
        {0, 1, 1},
        {0, 0, 0}
    }, Color.ORANGERED);

    private static final StoneType[] STONE_TYPES = new StoneType[]{I, J, L, O, S, T, Z};

    /**
     * constructor
     * @param stonetype which stone to create
     * @param blockSize size of an block
     */
    private Stone(StoneType stonetype, ReadOnlyDoubleProperty blockSize) {
        matrix = stonetype.matrix;
        stoneType = stonetype;

        // fill the matrix of the stone
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {

                // the painted block
                final Rectangle rectangle = new Rectangle();
                final int finalI = i;
                final int finalJ = j;

//                ChangeListener<Number> changeListener = new ChangeListener<Number>() {
//                    @Override
//                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
//                        rectangle.setWidth(number2.doubleValue());
//                        rectangle.setHeight(number2.doubleValue());
//                        rectangle.setTranslateY(number2.doubleValue() * finalI);
//                        rectangle.setTranslateX(number2.doubleValue() * finalJ);
//                    }
//                };
//                rectangle.setUserData(changeListener);

                // set position and dimensions of the block
                rectangle.setWidth(blockSize.doubleValue());
                rectangle.setHeight(blockSize.get());
                rectangle.setTranslateY(blockSize.get() * finalI);
                rectangle.setTranslateX(blockSize.get() * finalJ);

                // paint the blocks - or hide them
                if (matrix[i][j] == 1) {
                    rectangle.setFill(stonetype.color);
                    rectangle.setArcHeight(7);
                    rectangle.setArcWidth(7);
                } else {
                    rectangle.setOpacity(0);
                }

                // add block to the stone
                getChildren().add(rectangle);

            }
        }
    }

    /**
     * get a random stone
     * @param size size of a single block
     * @return a random stone
     */
    public static Stone getRandom(ReadOnlyDoubleProperty size) {
        StoneType stonetype = STONE_TYPES[RANDOM.nextInt(STONE_TYPES.length)];

        return new Stone(stonetype, size);
    }

    public int[][] getMatrix() {
        return matrix;
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
