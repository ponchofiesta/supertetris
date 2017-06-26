package de.vfh.pressanykey.supertetris.game;

/**
 * Listener class for Board events
 * @author Michael Richter
 */
public abstract class BoardListener {

    /**
     * Fired on game over
     */
    void onGameover() {

    }

    /**
     * Fired when Rows are deleted
     * @param count
     */
    void onRowDeleted(int count) {

    }

    /**
     * Fired when rows are added
     */
    void onRowAdded() {

    }

    /**
     * Fired when a new preview stone was chosen
     * @param stone the next stone
     */
    void onNext(Stone stone) {

    }

    /**
     * Fired when a stone was dropped
     */
    void onDropped() {
    }

    /**
     * Fired when a stone was moved
     */
    void onMove() {
    }

    /**
     * Fired when a stone was rotated
     */
    void onRotate() {
    }

    /**
     * Fired when a new stone was added
     * @param stone the new stone
     */
    void onSpawn(Stone stone) {
    }
}
