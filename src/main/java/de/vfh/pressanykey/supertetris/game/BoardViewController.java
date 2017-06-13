package de.vfh.pressanykey.supertetris.game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * View Controller for game boardPane
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
    protected Button btnRestart;
    @FXML
    protected Label lbTimer = new Label();
    @FXML
    protected Label lblPoints = new Label();
    @FXML
    protected Label lblLevel = new Label();
    @FXML
    protected Label lblRows = new Label();

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
        setView((Stage)btnStop.getScene().getWindow(), "start.fxml");
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

    public void showGameOver() {
        try {
            setView((Stage) btnStop.getScene().getWindow(), "gameover.fxml");
        } catch(Exception ex) {
            System.out.println("Error: Could not show gameover screen.");
        }
    }

}
