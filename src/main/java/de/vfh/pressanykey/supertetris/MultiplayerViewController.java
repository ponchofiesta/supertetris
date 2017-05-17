package de.vfh.pressanykey.supertetris;

import javafx.concurrent.Task;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

    final TetrisServer server = new TetrisServer();
    final TetrisClient client = new TetrisClient();

    @FXML
    public void btnNewGameClick(ActionEvent actionEvent) throws Exception {

        lbAddress.setText(server.getIP());
        lbHostName.setText(txtName.getCharacters().toString());


        final Thread serverThread = new Thread(() -> server.startServer());

        final Thread clientThread = new Thread(() -> {
            // at first we must wait until the server is running
            try {
                serverThread.join(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // then we can connect as client
            // infos on server
            int port = server.getPortNumber();
            String hostAddress = server.getIP();
            System.out.println("Port: " + port);
            System.out.println("IP: " + hostAddress);
            // connect
            client.startClient(hostAddress, port);
        });

        serverThread.start();
        clientThread.start();
    }

    @FXML
    public void btnJoinGameClick(ActionEvent actionEvent) throws Exception {

    }
}
