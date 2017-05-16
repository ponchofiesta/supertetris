package de.vfh.pressanykey.supertetris;

import java.awt.*;

/**
 * Class for stopwatch showing the elapsed time in a game
 */
public class Timer {

    private final long start;

    public Timer() {
        start = System.currentTimeMillis();
    }

    private double elapsedTime() {
        long now = System.currentTimeMillis();
        return (now - start) / 1000.0;
    }

    public String showTime() {
        String time;
        int minutes;
        int seconds;
        minutes = (int) elapsedTime()/60;
        seconds = (int) elapsedTime()%60;
        time = (minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + seconds;
        // Variante mit format, soll allerdings langsamer sein:
        //time = String.format("%02d:%02d", minutes, seconds);
        return time;
    }

}
