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
    private Button btnStart;

    @FXML
    public void btnClick(ActionEvent actionEvent) throws Exception {
        Stage stage = (Stage)btnStart.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("board.fxml"));
        stage.setScene(new Scene(root, 600, 600));
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
