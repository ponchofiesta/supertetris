package de.vfh.pressanykey.supertetris.game;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Map;

/**
 * View controller for creating a new multiplayer game
 * @author Claudia Kutter, Ute Mayer, Michael Richter
 */
public class CreateViewController extends LobbyViewController {

    // GUI elements
    @FXML
    private Button btnNewGame;
    @FXML
    private Label lblMessage;
    @FXML
    private TextField txtName;


    /**
     * Starts a new game server and connects to it as first client in order to start a new game as host.
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    public void btnNewGameClick(ActionEvent actionEvent) throws Exception {
        Platform.runLater(() -> lblMessage.setText(""));
        if(txtName.getText().trim().equals("")) {
            Platform.runLater(() -> lblMessage.setText("Es muss ein Name eingegeben werden."));
            return;
        }
        Map<String,String> connect = startServer();
        int port = Integer.parseInt(connect.get("port"));
        String host = connect.get("host");
        startClient(port, host);
    }
}
