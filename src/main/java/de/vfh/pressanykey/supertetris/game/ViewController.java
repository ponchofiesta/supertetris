package de.vfh.pressanykey.supertetris.game;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Abstract view controller with methods all controllers need
 */
public abstract class ViewController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    protected void setView(Stage stage, String fxmlfile) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(fxmlfile));
        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }
}
