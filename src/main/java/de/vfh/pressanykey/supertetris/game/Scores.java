package de.vfh.pressanykey.supertetris.game;

import java.util.Observable;

/**
 * Manage scores
 */
public class Scores extends Observable {

    private final static double pointFactor = 1.2;

    /**
     * Current level
     */
    private int level = 1;

    /**
     * Number of lines deleted
     */
    private int lineCount = 0;

    /**
     * Current score
     */
    private int points = 0;

    /**
     * Get current level
     * @return
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * Get current score
     * @return
     */
    public Integer getPoints() {
        return points;
    }

    /**
     * Get number of deleted rows
     * @return
     */
    public Integer getLineCount() {
        return lineCount;
    }

    /**
     * Calculate Points when rows where deleted
     * @param count
     */
    public void rowsDeleted(int count) {

        // calculate points
        points += count * count * Math.pow(pointFactor, level);

        // level up
        lineCount += count;
        level = lineCount / 10 + 1;

        setChanged();
        notifyObservers();
    }

    /**
     * Get speed of falling stones
     * @return Delay in ms
     */
    public int getSpeed() {
        return getSpeed(level);
    }

    /**
     * Get speed of falling stones
     * @param level Current level
     * @return Delay in ms
     */
    public static int getSpeed(int level) {
        // returns the speed based on the level
        // Graph: http://fooplot.com/?lang=de#W3sidHlwZSI6MCwiZXEiOiItbG9nKHgrMSkqNTAwKzEwMDAiLCJjb2xvciI6IiMwMDAwMDAifSx7InR5cGUiOjEwMDAsIndpbmRvdyI6WyIwIiwiMzAiLCItMCIsIjEwMDAiXX1d
        return (int)(-Math.log10(level) * 640 + 1000);
    }
}
