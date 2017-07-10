package de.vfh.pressanykey.supertetris;

import de.vfh.pressanykey.supertetris.game.MusicPlayer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Main class
 * @author Michael Richter, Claudia Kutter, Ute Mayer
 */
public class SupertetrisApp extends Application {

    public static Stage primaryStage;
    public static MusicPlayer musicPlayer;

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            // Load fonts
            Font.loadFont(getClass().getResourceAsStream("/fonts/Universalisme.ttf"), 20);
            Font.loadFont(getClass().getResourceAsStream("/fonts/Orbitron-Medium.ttf"), 20);
            Font.loadFont(getClass().getResourceAsStream("/fonts/Orbitron-Bold.ttf"), 20);
            // Set stage
            this.primaryStage = primaryStage;
            Parent root = FXMLLoader.load(getClass().getResource("/start.fxml"));
            primaryStage.setTitle("Super-Tetris");
            primaryStage.setScene(new Scene(root, 800, 500));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize MusicPlayer and play background music
        MusicPlayer musicPlayer = MusicPlayer.getInstance();

        musicPlayer.add("music", "sounds/music.mp3");
        musicPlayer.add("drop", "sounds/sfx_sounds_powerup6.wav");
        musicPlayer.add("rotate", "sounds/sfx_sounds_interaction16.wav");
        musicPlayer.add("rowdelete", "sounds/sfx_sounds_powerup6.wav");

        musicPlayer.play("music", MediaPlayer.INDEFINITE);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
