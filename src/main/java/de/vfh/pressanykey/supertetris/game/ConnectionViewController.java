package de.vfh.pressanykey.supertetris.game;

import de.vfh.pressanykey.supertetris.network.GameServer;
import de.vfh.pressanykey.supertetris.network.PlayerClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * View Controller for Multiplayer screen
 */
public class ConnectionViewController extends ViewController {

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
    private Label lbHostName;
    @FXML
    private Label lbGuestName;
    @FXML
    private TextField txtName;
    @FXML
    private TextField connectIP;
    @FXML
    private TextField connectPort;


    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

    private final GameServer server = new GameServer();
    private final PlayerClient player = new PlayerClient();
    private String hostAddress;
    private int port;
    private String playerName;

    @FXML
    public void btnNewGameClick(ActionEvent actionEvent) throws Exception {
        // who wants to start a game?
        playerName = txtName.getText();

        // create a thread for starting the server
        server.connect();
        final Thread serverThread = new Thread(() -> server.start());

        // create a thread to connect as client to server
        final Thread playerThread = new Thread(() -> {
            // at first we must wait until the server is running
            try {
                serverThread.join(200);
                // display infos on server
                port = server.getPortNumber();
                hostAddress = server.getHost4Address();
                Platform.runLater(() -> {
                    lbAddress.setText(hostAddress);
                    lbPort.setText(String.valueOf(port));
                    lbHostName.setText(playerName);
                });
                /// then we can connect
                player.connect(hostAddress, port, playerName);
                player.start();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        serverThread.start();
        playerThread.start();


        // example: create the sending protocol ("Example"-class) / database that the client uses for sending
        // makeData is the function getting the data wie want to sent
//        Example ex = Example.makeData();
//        client.send(ex);

    }

    @FXML
    public void btnJoinGameClick(ActionEvent actionEvent) throws Exception {
        // who is connecting?
        playerName = txtName.getText();

        // Get the address we want to connect to from the textfield
        hostAddress = connectIP.getText();
        port = Integer.parseInt(connectPort.getText());

        // this thread is created to deal with the client-server communication while the game is played
        final Thread playerThread = new Thread(() -> {
            // we just want to join the game so we expect that the server is running
            try {
                Platform.runLater(() -> {
                    lbAddress.setText(hostAddress);
                    lbPort.setText(String.valueOf(port));
                    lbGuestName.setText(playerName);
                });
                player.connect(hostAddress, port, playerName);
                player.start();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        playerThread.start();

    }
}
