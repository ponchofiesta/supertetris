package de.vfh.pressanykey.supertetris.game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * View Controller for start screen
 */
public class StartViewController extends ViewController  {

    @FXML
    private Button btnSingleplayer;
    @FXML
    private Button btnMultiplayer;

    @FXML
    public void btnSingleplayerClick(ActionEvent actionEvent) throws Exception {
        setView((Stage)btnSingleplayer.getScene().getWindow(), "board.fxml");
    }

    @FXML
    public void btnMultiplayerClick(ActionEvent actionEvent) throws Exception {
        setView((Stage)btnMultiplayer.getScene().getWindow(), "connection.fxml");
    }

}
