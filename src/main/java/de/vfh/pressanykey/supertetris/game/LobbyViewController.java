package de.vfh.pressanykey.supertetris.game;

import de.vfh.pressanykey.supertetris.SupertetrisApp;
import de.vfh.pressanykey.supertetris.network.ClientInterface;
import de.vfh.pressanykey.supertetris.network.GameServer;
import de.vfh.pressanykey.supertetris.network.PlayerClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Global view Controller for Multiplayer screen
 * @author Claudia Kutter, Ute Mayer, Michael Richter
 */
public class LobbyViewController extends ViewController {

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
    private Thread clientThread = null;
    private Thread serverThread = null;

    private GameServer server;
    private PlayerClient client;
    private ClientInterface clientInterface;
    private MultiplayerGame game;

    /**
     * Initializes the view by creating a new multiplayer game and listening for connecting players
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        server = GameServer.getInstance();
        client = PlayerClient.getInstance();
        clientInterface = ClientInterface.getInstance(client);
        game = MultiplayerGame.getInstance();

        game.setView(this);

        // Update playernames on connection
        game.playerSignal.addListener(((o, oldVal, newVal) -> {
            Platform.runLater(() -> {
                lbFirstPlayer.setText(game.myName.getValue());
                lbSecondPlayer.setText(game.oppName.getValue());
                lblMessage.setText("");
            });
        }));

        // Send logout if connection was established and window is closed
        Stage stage = SupertetrisApp.getPrimaryStage();
        Window window = stage.getScene().getWindow();
        window.setOnCloseRequest(event -> {
            if(clientThread != null) {
                clientInterface.sendLogout();
            }
        });
    }


    /**
     * Shows the start screen if the back button is clicked and logs the player out if he was already c
     * onnected to the server
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    public void btnBackClick(ActionEvent actionEvent) throws Exception {
        if(clientThread != null) {
            clientInterface.sendLogout();
            game.removeOpponent();
        }
        setView("start.fxml");
    }


    /**
     * Starts the game when the start button is clicked and two players are connected
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    public void btnStartGameClick(ActionEvent actionEvent) throws Exception {
        if(game.oppName.getValue() == null || game.oppName.getValue().equals("")) {
            Platform.runLater(() -> lblMessage.setText("Dir fehlt ein Mitspieler, um das Spiel zu starten."));
        } else {
            clientInterface.sendGameStarted();
        }
   }


    /**
     * Starts a new multiplayer game server
     * @return  HashMap with port and host on which the server is running
     */
    protected Map startServer() {
        server.connect();
        serverThread = new Thread(server);
        serverThread.setDaemon(true);
        serverThread.start();
        // wait until server is running and get connection infos
        try {
            server.join(200);
            port = server.getPortNumber();
            hostAddress = server.getHost4Address();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> connection = new HashMap<String, String>();
        connection.put("port", String.valueOf(port));
        connection.put("host", hostAddress);
        return connection;
    }


    /**
     * Starts a new client which connects to a multiplayer game server running under the address
     * @param port  Port on which the game server is running
     * @param host  IP-Address on which the game server is running
     */
    protected void startClient(int port, String host) {
        playerName = txtName.getText();
        try {
            // connect
            client.connect(host, port, game);
            clientThread = new Thread(client);
            clientThread.setDaemon(true);
            clientThread.start();
            clientInterface.sendLogin(playerName);
            game.addMyself(playerName);
            // display information
            client.join(200);
            Platform.runLater(() -> {
                lbAddress.setText(hostAddress);
                lbPort.setText(String.valueOf(port));
            });
        } catch (Exception e) {
            Platform.runLater(() -> lblMessage.setText("Fehlerhafte Verbindungsdaten. Verbindung zum Server nicht möglich."));
            e.printStackTrace();
        }
    }

}
