package de.vfh.pressanykey.supertetris;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.Observable;

/**
 * Created by poncho on 07.06.2017.
 */
public class Stopwatch extends Observable {

    private Timeline stopwatchTimer;
    private long stopwatchStart = 0;
    private long stopwatchPause = 0;
    private boolean isPaused = false;

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
            //view.setStopwatchText(time);
            setChanged();
            notifyObservers(time);
        }));
        stopwatchTimer.setCycleCount(Animation.INDEFINITE);
        stopwatchTimer.play();
    }

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

    public void stop() {
        stopwatchTimer.stop();
    }
}