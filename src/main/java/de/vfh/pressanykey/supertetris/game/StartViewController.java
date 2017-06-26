package de.vfh.pressanykey.supertetris.game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * View Controller for start screen
 * @author Claudia Kutter, Michael Richter, Ute Mayer
 */
public class StartViewController extends ViewController  {

    @FXML
    private Button btnSingleplayer;
    @FXML
    private Button btnMultiplayer;
    @FXML
    private Button btnMultiplayerJoin;

    @FXML
    public void btnSingleplayerClick(ActionEvent actionEvent) throws Exception {
        setView("board.fxml");
    }

    @FXML
    public void btnMultiplayerClick(ActionEvent actionEvent) throws Exception {
        setView("create.fxml");
    }

    @FXML
    public void btnMultiplayerJoinClick(ActionEvent actionEvent) throws Exception {
        setView("join.fxml");
    }
}
