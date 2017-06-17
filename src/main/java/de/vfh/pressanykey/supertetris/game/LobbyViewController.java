package de.vfh.pressanykey.supertetris.game;

import de.vfh.pressanykey.supertetris.SupertetrisApp;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * View Controller for Multiplayer screen
 */
public class LobbyViewController extends ViewController {

    // GUI elements
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
    public Stage currentStage;

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
     * Starts a new game server and connects to it as first client in order to start a new game as host.
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    public void btnNewGameClick(ActionEvent actionEvent) throws Exception {
        // who wants to start a game?
        playerName = txtName.getText();

        // create a thread for starting the server
        server.connect();
        final Thread serverThread = new Thread(() -> server.start(), "ServerThread");

        // create a thread to connect as client to server
        final Thread playerThread = new Thread(() -> {
            try {
                // at first we must wait until the server is running
                serverThread.join(200);
                // display infos on server
                port = server.getPortNumber();
                hostAddress = server.getHost4Address();
                // then we can connect
                client.connect(hostAddress, port, game);
                client.start();
                clientInterFace.sendLogin(playerName);
                game.addMyself(playerName);
                // display information
                client.join(200);
                Platform.runLater(() -> {
                    lbAddress.setText(hostAddress);
                    lbPort.setText(String.valueOf(port));
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "Client1Thread");

        serverThread.start();
        playerThread.start();
    }


    /**
     * Connects to a running server when the join game button is clicked.
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    public void btnJoinGameClick(ActionEvent actionEvent) throws Exception {
        // who is connecting?
        playerName = txtName.getText();

        // Get the address we want to connect to from the textfield
        hostAddress = connectIP.getText();
        try {
            port = Integer.parseInt(connectPort.getText());
        } catch (Exception e) {
            Platform.runLater(() -> lblMessage.setText("Der angegebene Port ist fehlerhaft."));
        }

        // this thread is created to deal with the client-server communication while the game is played
        final Thread playerThread = new Thread(() -> {
            // we just want to join the game so we expect that the server is running
            try {
                client.connect(hostAddress, port, game);
                client.start();
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
        }, "Client2Thread");

        playerThread.start();
    }


    // determines the current stage
//    private void setCurrentStage(ActionEvent actionEvent){
//        currentStage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
//    }

}
