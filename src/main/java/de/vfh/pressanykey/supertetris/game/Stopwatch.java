package de.vfh.pressanykey.supertetris.game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.Observable;

/**
 * Stopwatch for play time
 * @author Claudia Kutter, Michael Richter
 */
public class Stopwatch extends Observable {

    private Timeline stopwatchTimer;
    private long stopwatchStart = 0;
    private long stopwatchPause = 0;
    protected boolean isPaused = false;

    /**
     * Start the stopwatch
     */
    public void start() {
        stopwatchStart = System.currentTimeMillis();
        stopwatchTimer = new Timeline(new KeyFrame(Duration.millis(1000), ae -> {
            long nowTime = System.currentTimeMillis();
            double diffTime = (nowTime - stopwatchStart) / 1000.0;
            String time;
            int minutes;
            int seconds;
            minutes = (int) diffTime/60;
            seconds = (int) diffTime%60;
            time = (minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + seconds;
            /// Variante mit format, soll allerdings langsamer sein:
            //time = String.format("%02d:%02d", minutes, seconds);
            setChanged();
            notifyObservers(time);
        }));
        stopwatchTimer.setCycleCount(Animation.INDEFINITE);
        stopwatchTimer.play();
    }

    /**
     * Pause and resume stopwatch
     */
    public void pause() {
        if(isPaused) {
            stopwatchStart = System.currentTimeMillis() - (stopwatchPause - stopwatchStart);
            stopwatchTimer.play();
        } else {
            stopwatchTimer.pause();
            stopwatchPause = System.currentTimeMillis();
        }
        isPaused = !isPaused;
    }

    /**
     * Stop stopwatch
     */
    public void stop() {
        stopwatchTimer.stop();
    }
}
