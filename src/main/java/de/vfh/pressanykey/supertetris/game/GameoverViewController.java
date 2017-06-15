package de.vfh.pressanykey.supertetris.game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * View Controller for Gameover screen
 */
public class GameoverViewController extends ViewController {

    private Scores scores;

    @FXML
    private Button btnHome;
    @FXML
    protected Label lblPoints2;
    @FXML
    protected Label lblLevel2;
    @FXML
    protected Label lblRows2;

    public void btnHomeClick(ActionEvent actionEvent) throws Exception {
        setView((Stage)btnHome.getScene().getWindow(), "start.fxml");
    }

    public void setScores(Scores scores) {
        this.scores = scores;
        if(scores != null) {
            lblPoints2.setText(scores.getPoints().toString());
            lblRows2.setText(scores.getLineCount().toString());
            lblLevel2.setText(scores.getLevel().toString());
        }
    }
}
