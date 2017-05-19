package de.vfh.pressanykey.supertetris;

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
public class MultiplayerViewController extends ViewController {

    @FXML
    private Button btnNewGame;
    @FXML
    private Button btnJoinGame;
    @FXML
    private Button btnStartGame;
    @FXML
    private Label lbAddress;
    @FXML
    private Label lbHostName;
    @FXML
    private Label lbGuestName;
    @FXML
    private TextField txtName;
    @FXML
    private TextField connectInfo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

    private final TetrisServer server = new TetrisServer();
    private final TetrisClient client1 = new TetrisClient();
    private final TetrisClient client2 = new TetrisClient();
    private String hostAddress;
    private int port;
    private String playerName;

    @FXML
    public void btnNewGameClick(ActionEvent actionEvent) throws Exception {
        // who wants to start the server?
        playerName = txtName.getText();

        final Thread serverThread = new Thread(() -> server.startServer(playerName));

        final Thread client1Thread = new Thread(() -> {
            // at first we must wait until the server is running
            try {
                serverThread.join(200);
                // then we can connect as client1
                // infos on server
                port = server.getPortNumber();
                hostAddress = server.getHost4Address();
                Platform.runLater(() -> {
                    lbAddress.setText(hostAddress + ":" + port);
                    lbHostName.setText(playerName);
                });
                // connect
                client1.startClient(hostAddress, port, playerName);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        serverThread.start();
        client1Thread.start();
    }

    @FXML
    public void btnJoinGameClick(ActionEvent actionEvent) throws Exception {
        // who is connecting?
        playerName = txtName.getText();

        // Get the address we want to connect to from the textfield
        String[] split = connectInfo.getText().split(":");
        hostAddress = split[0];
        port = Integer.parseInt(split[1]);

        // this thread is created to deal with the client-server communication while the game is played
        final Thread client2Thread = new Thread(() -> {
            // we just want to join the game so we expect that the server is running
            try {
                Platform.runLater(() -> {
                    lbAddress.setText(hostAddress + ":" + port);
                    lbGuestName.setText(playerName);
                });

                client2.startClient(hostAddress, port, playerName);

            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        client2Thread.start();
    }
}
