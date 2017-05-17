package de.vfh.pressanykey.supertetris;

import javafx.concurrent.Task;
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
    private StackPane boardPane;
    @FXML
    private Button btnPause;
    @FXML
    private Button btnStop;
    @FXML
    private Button btnRestart;
    @FXML
    private Label lbTimer = new Label();

    private GameController gameController;

    public StackPane getBoardPane() {
        return this.boardPane;
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
        boardPane.getChildren().add(gameController.getBoard());
        gameController.start();



        // Start timer
        Task timerTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Timer timer = new Timer();
                while (true) {
                    updateMessage(timer.showTime());
                    //System.out.println(boardPane.getTranslateX() + "," + boardPane.getTranslateX() + "," + boardPane.getWidth() + "," + boardPane.getHeight());
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
