package de.vfh.pressanykey.supertetris;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * View Controller for game boardPane
 */
public class BoardViewController extends ViewController {

    @FXML
    private StackPane boardPaneContainer;
    @FXML
    private StackPane boardPane;
    @FXML
    private Button btnPause;
    @FXML
    private Button btnStop;
    @FXML
    private Button btnRestart;
    @FXML
    private Label lbTimer = new Label();
    @FXML
    private Label lblPoints = new Label();
    @FXML
    private Label lblLevel = new Label();
    @FXML
    private Label lblRows = new Label();

    private GameController gameController;

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
        setView((Stage)btnStop.getScene().getWindow(), "start.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //gameController = GameController.getInstance();
        gameController = new GameController();
        gameController.setView(this);
        boardPane.getChildren().add(gameController.getBoardPane());

        gameController.getStopwatch().addObserver((obj, value) -> {
            this.lbTimer.setText((String)value);
        });

        gameController.getScores().addObserver((o, arg) -> {
            lblPoints.setText(gameController.getScores().getPoints().toString());
            lblLevel.setText(gameController.getScores().getLevel().toString());
            lblRows.setText(gameController.getScores().getLineCount().toString());
        });

        //boardPaneContainer.getChildren().add(gameController.getBoardPane());
        gameController.start();

    }

}
