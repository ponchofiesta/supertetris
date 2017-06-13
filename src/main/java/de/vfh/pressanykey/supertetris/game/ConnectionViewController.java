package de.vfh.pressanykey.supertetris.game;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    MultiplayerGame game;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        game = new MultiplayerGame();

        // Update playernames on connection
        game.getPlayerCount().addListener((ChangeListener<Number>)((o, oldVal, newVal) -> {
            Platform.runLater(() -> {
                lbFirstPlayer.setText(game.getMyself().getName());
                lbSecondPlayer.setText(game.getOpponent().getName());
            });
        }));
    }


    @FXML
    public void btnStartGameClick(ActionEvent actionEvent) throws Exception {
        System.out.println("playercount is: " + game.getPlayerCount());
        if(game.getPlayerCount().getValue() != 2) {
            Platform.runLater(() -> {
                lblMessage.setText("Dir fehlt ein Mitspieler, um das Spiel zu starten.");
            });
        } else {
            // TODO: switch the screen for both
            setView((Stage)btnStartGame.getScene().getWindow(), "multiplayer.fxml");
        }
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
                client.connect(hostAddress, port, playerName, game);
                client.start();
                // display information
                client.join(200);
                Platform.runLater(() -> {
                    lbAddress.setText(hostAddress);
                    lbPort.setText(String.valueOf(port));
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
                client.connect(hostAddress, port, playerName, game);
                client.start();
                // display information
                client.join(200);
                Platform.runLater(() -> {
                    lbAddress.setText(hostAddress);
                    lbPort.setText(String.valueOf(port));
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        playerThread.start();
    }
}
