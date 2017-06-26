package de.vfh.pressanykey.supertetris.game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * View Controller for Gameover screen
 * @author Michael Richter, Claudia Kutter
 */
public class GameoverViewController extends ViewController {

    private Scores scores;
    private Scores oppScores;

    @FXML
    private Button btnHome;
    @FXML
    protected Label lblName;
    @FXML
    protected Label lblPoints2;
    @FXML
    protected Label lblLevel2;
    @FXML
    protected Label lblRows2;
    @FXML
    protected Label lblOppName;
    @FXML
    protected Label lblOppPoints;
    @FXML
    protected Label lblOppLevel;
    @FXML
    protected Label lblOppRows;


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

    public void setNames(String myName, String oppName) {
        lblName.setText(myName);
        lblOppName.setText(oppName);
    }

    public void setOppScores(Scores scores) {
        this.oppScores = scores;
        if(scores != null) {
            lblOppPoints.setText(scores.getPoints().toString());
            lblOppRows.setText(scores.getLineCount().toString());
            lblOppLevel.setText(scores.getLevel().toString());
        }
    }
}
