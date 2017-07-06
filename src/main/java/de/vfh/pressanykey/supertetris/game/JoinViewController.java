package de.vfh.pressanykey.supertetris.game;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


/**
 * View controller for joining a new multiplayer game
 * @author Claudia Kutter, Ute Mayer, Michael Richter
 */
public class JoinViewController extends LobbyViewController {

    // GUI elements
    @FXML
    private Button btnJoinGame;
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


    /**
     * Connects to a running server when the join game button is clicked.
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    public void btnJoinGameClick(ActionEvent actionEvent) throws Exception {
        Platform.runLater(() -> lblMessage.setText(""));
        // Get connection infos
        hostAddress = connectIP.getText();
        try {
            port = Integer.parseInt(connectPort.getText());
            startClient(port, hostAddress);
        } catch (Exception e) {
            Platform.runLater(() -> lblMessage.setText("Der angegebene Port ist fehlerhaft."));
        }
    }

}
