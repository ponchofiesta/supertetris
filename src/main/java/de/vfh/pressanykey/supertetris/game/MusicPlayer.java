package de.vfh.pressanykey.supertetris.game;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Play Music/Sounds
 *
 * @author Ute Mayer, Michael Richter
 */
public class MusicPlayer {
    private static MusicPlayer instance;

    private Map<String, MediaPlayer> players;

    private MusicPlayer() {
        players = new HashMap<>();
    }

    public static MusicPlayer getInstance() {
        if(instance == null) {
            instance = new MusicPlayer();
        }
        return instance;
    }

    /**
     * Add audio file to the player
     * @param id Identifier for this audio file
     * @param filename Filename (URL)
     */
    public void add(String id, String filename) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(filename);
        Media sound = new Media(resource.toString());
        players.put(id, new MediaPlayer(sound));
    }

    /**
     * Play audio file
     * @param id Identiier of the audio file
     * @param times Number of times to repeat
     */
    public void play(String id, int times) {
        if(players.containsKey(id)) {
            MediaPlayer player = players.get(id);
            player.stop();
            player.seek(player.getStartTime());
            player.setCycleCount(times);
            player.play();
        }
    }

    /**
     * Play audio file one time
     * @param id Identiier of the audio file
     */
    public void play(String id) {
        play(id, 1);
    }

    /**
     * Stop playing an audio file
     * @param id Identiier of the audio file
     */
    public void stop(String id) {
        if(players.containsKey(id)) {
            MediaPlayer player = players.get(id);
            player.stop();
        }
    }
}
