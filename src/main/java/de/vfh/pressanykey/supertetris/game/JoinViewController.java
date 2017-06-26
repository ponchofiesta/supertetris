package de.vfh.pressanykey.supertetris.game;

import de.vfh.pressanykey.supertetris.SupertetrisApp;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * View Controller for Multiplayer screen
 * @author Claudia Kutter, Ute Mayer
 */
public class JoinViewController extends ViewController {

    // GUI elements
    @FXML
    private Button btnBack;
    @FXML
    private Button btnNewGame;
    @FXML
    private Button btnJoinGame;
    @FXML
    private Button btnStartGame;
    @FXML
    private Label lbAddress;
    @FXML
    private Label lbPort;
    @FXML
    private Label lbFirstPlayer;
    @FXML
    private Label lbSecondPlayer;
    @FXML
    private Label lblMessage;
    @FXML
    private TextField txtName;
    @FXML
    private TextField connectIP;
    @FXML
    private TextField connectPort;

    // Variables for connection
    private String hostAddress;
    private int port;
    private String playerName;
    private Thread clientThread;

    /**
     * Initializes the view by creating a new multiplayer game and listening for connecting players
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        game.setView(this);
        currentStage = SupertetrisApp.getPrimaryStage();

        // Update playernames on connection
        game.playerCount.addListener(((o, oldVal, newVal) -> {
            Platform.runLater(() -> {
                lbFirstPlayer.setText(game.myName.getValue());
                lbSecondPlayer.setText(game.oppName.getValue());
            });
        }));
    }


    /**
     * Shows the start screen if the back button is clicked and logs the player out if he was already c
     * onnected to the server
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    public void btnBackClick(ActionEvent actionEvent) throws Exception {
        /* TODO: This doesn't work
        if(clientThread.isAlive() || serverThread.isAlive()) {
            clientInterFace.sendLogout();
        }*/
        setView((Stage)btnBack.getScene().getWindow(), "start.fxml");
    }


    /**
     * Starts the game when the start button is clicked and two players are connected
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    public void btnStartGameClick(ActionEvent actionEvent) throws Exception {
        if(game.playerCount.getValue() != 2) {
            Platform.runLater(() -> lblMessage.setText("Dir fehlt ein Mitspieler, um das Spiel zu starten."));
        } else {
            clientInterFace.sendGameStarted();
        }
   }

    /**
     * Connects to a running server when the join game button is clicked.
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    public void btnJoinGameClick(ActionEvent actionEvent) throws Exception {
        playerName = txtName.getText();

        // Get connection infos
        hostAddress = connectIP.getText();
        try {
            port = Integer.parseInt(connectPort.getText());
        } catch (Exception e) {
            Platform.runLater(() -> lblMessage.setText("Der angegebene Port ist fehlerhaft."));
        }

        // connect to server
        try {
            client.connect(hostAddress, port, game);
            clientThread = new Thread(client);
            clientThread.setDaemon(true);
            clientThread.start();
            clientInterFace.sendLogin(playerName);
            game.addMyself(playerName);
            // display information
            client.join(200);
            Platform.runLater(() -> {
                lbAddress.setText(hostAddress);
                lbPort.setText(String.valueOf(port));
            });
        } catch (Exception e) {
            System.out.println("Fehler bei den Verbindungsdaten.");
        }
    }

}
