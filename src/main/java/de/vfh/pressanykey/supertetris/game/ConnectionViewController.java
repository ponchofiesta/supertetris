package de.vfh.pressanykey.supertetris.game;

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

    // Variables for connection
    private String hostAddress;
    private int port;
    private String playerName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    @FXML
    public void btnStartGameClick(ActionEvent actionEvent) throws Exception {
        // TODO: ensure that two players are connected and switch the screen for both
        setView((Stage)btnStartGame.getScene().getWindow(), "multiplayer.fxml");
    }

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
                // then we can connect
                client.connect(hostAddress, port, playerName);
                client.start();
                // display information
                Platform.runLater(() -> {
                    lbAddress.setText(hostAddress);
                    lbPort.setText(String.valueOf(port));
                    lbHostName.setText(playerName);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        serverThread.start();
        playerThread.start();
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

                client.connect(hostAddress, port, playerName);
                client.start();
                // display information
                Platform.runLater(() -> {
                    lbAddress.setText(hostAddress);
                    lbPort.setText(String.valueOf(port));
                    lbGuestName.setText(playerName);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        playerThread.start();
    }
}
