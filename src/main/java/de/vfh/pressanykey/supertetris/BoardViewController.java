package de.vfh.pressanykey.supertetris;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * View Controller for game board
 */
public class BoardViewController extends ViewController {

    @FXML
    private Button btnPause;
    @FXML
    private Button btnStop;
    @FXML
    private Button btnRestart;
    @FXML
    private Label lbTimer = new Label();

    @FXML
    public void btnStopClick(ActionEvent actionEvent) throws Exception {
        setView((Stage)btnStop.getScene().getWindow(), "start.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Start timer
        Task timerTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Timer timer = new Timer();
                while (true) {
                    updateMessage(timer.showTime());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
                return null;
            }
        };
        lbTimer.textProperty().bind(timerTask.messageProperty());
        Thread timerThread = new Thread(timerTask);
        timerThread.setName("Timer Task");
        timerThread.setDaemon(true);
        timerThread.start();
    }

}
