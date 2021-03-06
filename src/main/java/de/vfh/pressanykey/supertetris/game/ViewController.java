package de.vfh.pressanykey.supertetris.game;

import de.vfh.pressanykey.supertetris.SupertetrisApp;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Abstract view controller with methods all controllers need
 * @author Michael Richter, Claudia Kutter, Ute Mayer
 */
public abstract class ViewController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    protected void setView(String fxmlfile) throws Exception {
        Stage stage = SupertetrisApp.getPrimaryStage();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(fxmlfile));
        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }

}
