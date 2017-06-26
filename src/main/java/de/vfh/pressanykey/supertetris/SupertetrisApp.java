package de.vfh.pressanykey.supertetris;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;


/**
 * Main class
 * @author Michael Richter, Claudia Kutter, Ute Mayer
 */
public class SupertetrisApp extends Application {

    public static Stage primaryStage;
    public static Task task;

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            // Load fonts
            Font.loadFont(getClass().getResourceAsStream("/fonts/BungeeOutline-Regular.ttf"), 36);
            Font.loadFont(getClass().getResourceAsStream("/fonts/Bungee-Regular.ttf"), 20);
            Font.loadFont(getClass().getResourceAsStream("/fonts/BungeeHairline-Regular.ttf"), 20);
            // Set stage
            this.primaryStage = primaryStage;
            Parent root = FXMLLoader.load(getClass().getResource("/start.fxml"));
            primaryStage.setTitle("Super-Tetris");
            primaryStage.setScene(new Scene(root, 800, 500));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        startMusic();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Play Tetris Music in a Background Thread
     */
    public static void startMusic() {
        task = new Task() {
            @Override
            protected Object call() throws Exception {
                ClassLoader classLoader = getClass().getClassLoader();
                URL resource = classLoader.getResource("sounds/music.mp3");
                Media sound = new Media(resource.toString());
                MediaPlayer mediaPlayer = new MediaPlayer(sound);
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                mediaPlayer.play();
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.start();
    }
}
