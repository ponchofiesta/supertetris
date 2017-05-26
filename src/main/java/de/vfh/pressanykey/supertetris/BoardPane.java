package de.vfh.pressanykey.supertetris;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 * view of the board
 */
public class BoardPane extends StackPane {

    private int boardWidth = 10;
    private int boardHeight = 20;

    public ReadOnlyDoubleProperty getBlockSize() {
        return blockSize;
    }

    /**
     * size of one single block
     */
    private DoubleProperty blockSize = new SimpleDoubleProperty();

    /**
     * constuctor
     */
    public BoardPane(int boardwidth, int boardheight) {

        this.boardWidth = boardwidth;
        this.boardHeight = boardheight;

        // bind width changes to blocksize object
        blockSize.bind(new DoubleBinding() {
            {
                super.bind(widthProperty());
            }

            @Override
            protected double computeValue() {
                return getWidth() / boardWidth;
            }
        });

        // set clipping -> hide overflow
        Rectangle clip = new Rectangle();
        layoutBoundsProperty().addListener((ov, oldValue, newValue) -> {
            clip.setWidth(newValue.getWidth());
            clip.setHeight(newValue.getHeight());
        });
        setClip(clip);
    }

}
