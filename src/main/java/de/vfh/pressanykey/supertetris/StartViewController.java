package de.vfh.pressanykey.supertetris;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * View Controller for start screen
 */
public class StartViewController implements Initializable {

    @FXML
    private Button btnSingleplayer;
    @FXML
    private Button btnMultiplayer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void btnSingleplayerClick(ActionEvent actionEvent) throws Exception {
        setView((Stage)btnSingleplayer.getScene().getWindow(), "board.fxml");
    }

    @FXML
    public void btnMultiplayerClick(ActionEvent actionEvent) throws Exception {
        setView((Stage)btnMultiplayer.getScene().getWindow(), "multiplayer.fxml");
    }

    private void setView(Stage stage, String fxmlfile) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(fxmlfile));
        stage.setScene(new Scene(root, 600, 600));
        stage.show();
    }
}
