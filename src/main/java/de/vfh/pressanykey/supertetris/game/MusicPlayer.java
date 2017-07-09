package de.vfh.pressanykey.supertetris.game;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

/**
 * Play Music/Sounds in a Background Thread
 *
 * @author Ute Mayer
 */
public class MusicPlayer {
    private static MusicPlayer instance;

    public void startMusic(String path, int times) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(path);
        Media sound = new Media(resource.toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setCycleCount(times);
        mediaPlayer.play();
    }

    public static MusicPlayer getInstance() {
        if(instance == null) {
            instance = new MusicPlayer();
        }
        return instance;
    }
}
