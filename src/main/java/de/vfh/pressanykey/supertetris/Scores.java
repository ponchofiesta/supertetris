package de.vfh.pressanykey.supertetris;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 * Created by poncho on 08.06.2017.
 */
public class Scores extends Observable {

    private final static double pointFactor = 1.2;

    private int level = 1;
    private int lineCount = 0;
    private int points = 0;

    public Integer getLevel() {
        return level+1;
    }

    public Integer getPoints() {
        return points;
    }

    public Integer getLineCount() {
        return lineCount;
    }

    public void rowsDeleted(int count) {

        // calculate points
        points += count * count * Math.pow(pointFactor, level);

        // level up
        lineCount += count;
        level = lineCount / 10;

        setChanged();
        notifyObservers();
    }

    public int getSpeed() {
        // returns the speed based on the level
        // Graph: http://fooplot.com/?lang=de#W3sidHlwZSI6MCwiZXEiOiItbG9nKHgrMSkqNTAwKzEwMDAiLCJjb2xvciI6IiMwMDAwMDAifSx7InR5cGUiOjEwMDAsIndpbmRvdyI6WyIwIiwiMzAiLCItMCIsIjEwMDAiXX1d
        return getSpeed(level+1);
    }

    public static int getSpeed(int level) {
        // returns the speed based on the level
        // Graph: http://fooplot.com/?lang=de#W3sidHlwZSI6MCwiZXEiOiItbG9nKHgrMSkqNTAwKzEwMDAiLCJjb2xvciI6IiMwMDAwMDAifSx7InR5cGUiOjEwMDAsIndpbmRvdyI6WyIwIiwiMzAiLCItMCIsIjEwMDAiXX1d
        return (int)(-Math.log10(level+1) * 640 + 1000);
    }
}