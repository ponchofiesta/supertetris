package de.vfh.pressanykey.supertetris.game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * View Controller for game boardPane
 * @author Michael Richter, Ute Mayer
 */
public class BoardViewController extends ViewController {

    @FXML
    protected StackPane boardPaneContainer;
    @FXML
    protected StackPane boardPane;
    @FXML
    protected StackPane nextField;
    @FXML
    protected Button btnPause;
    @FXML
    protected Button btnStop;
    @FXML
    protected Label lbTimer;
    @FXML
    protected Label lblPoints;
    @FXML
    protected Label lblLevel;
    @FXML
    protected Label lblRows;

    protected GameController gameController;

//    public StackPane getBoardPane() {
//        return this.boardPane;
//    }

    @FXML
    public void btnPauseClick(ActionEvent actionEvent) throws Exception {
        gameController.pause();
    }

    @FXML
    public void btnStopClick(ActionEvent actionEvent) throws Exception {
        gameController.stop();
        MusicPlayer musicPlayer = MusicPlayer.getInstance();
        musicPlayer.startMusic("sounds/music.mp3", 15);
        showGameOver(gameController.getScores());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //gameController = GameController.getInstance();
        gameController = new GameController();
        gameController.setView(this);
        boardPane.getChildren().add(gameController.getBoardPane());

        // bind stopwatch updates to view
        gameController.getStopwatch().addObserver((obj, value) -> {
            this.lbTimer.setText((String)value);
        });

        // bind score updates to view
        gameController.getScores().addObserver((o, arg) -> {
            lblPoints.setText(gameController.getScores().getPoints().toString());
            lblLevel.setText(gameController.getScores().getLevel().toString());
            lblRows.setText(gameController.getScores().getLineCount().toString());
        });

        // bind preview stone updates to view
        gameController.getBoard().addBoardListener(new BoardListener() {
            @Override
            void onNext(Stone stone) {
                nextField.getChildren().clear();
                nextField.getChildren().add(stone);
            }
        });

        gameController.start();

    }

    public void showGameOver(Scores scores) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("gameover.fxml"));
            Stage stage = (Stage)btnStop.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 800, 500));
            GameoverViewController gocontroller = loader.getController();
            gocontroller.setScores(scores);
            stage.show();
        } catch(Exception ex) {
            System.out.println("Error: Could not show gameover screen.");
        }
    }

}
