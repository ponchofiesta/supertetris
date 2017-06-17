package de.vfh.pressanykey.supertetris.network;

import de.vfh.pressanykey.supertetris.game.MultiplayerGame;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.*;

public class PlayerClient extends Thread {

    // Attributes for client-server communication
    private int port;
    private InetAddress ipAddress;
    private DatagramSocket socket;
    private boolean running;
    // Game state buffer
    private MultiplayerGame game;


    /**
     * Initializes the client by creating a socket for communication
     * @param ipAddress  IP address of the server
     * @param port  Port of the server
     * @param game  Game for which the communication is needed
     */
    public void connect(String ipAddress, int port, MultiplayerGame game) {
        try {
            this.game = game;
            // handle connection
            this.port = port;
            this.ipAddress = InetAddress.getByName(ipAddress);
            this.socket = new DatagramSocket();
        } catch (SocketException e) {
            System.out.println("Fehlerhaftes Socket: " + e.getMessage());
        } catch (UnknownHostException e) {
            System.out.println("Unbekannter Host: "  + e.getMessage());
        }
    }


    /**
     * Starts the client thread that listens for incoming datagram packets
     */
    public void run() {
        running = true;
        while(running) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
        }
    }


    /**
     * Sends data to the server
     * @param data  Data as byte array that is to be sent
     */
    public void send(byte[] data) {
        assert(socket.isConnected());
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unchecked")
    // Handles the processing of packages based on their package type which is stored in the "action" key
    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        if (message.startsWith("{")) {
            try {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(message);
                JSONObject messageObject = (JSONObject) obj;
                String action = (String) messageObject.get("action");
                switch (action) {
                    case Actions.GAME_START:
                        game.startGame();
                        break;
                    case Actions.GAME_OVER:
                        game.stopGame();
                        running = false;
                        System.out.println("CLIENT > Shut down");
                        break;
                    case Actions.CONNECT_STATE:
                        String player1 = (String) messageObject.get("player1");
                        String player2 = (String) messageObject.get("player2");
                        // Find out which of both the opponent's name is
                        String opponent = (player1.equals(game.myName.getValue())) ? player2 : player1;
                        // Add opponent if it's not null
                        if (opponent != null) {
                            game.addOpponent(opponent);
                        }
                        System.out.println("CLIENT > Connected players: " + player1 + " and " + player2);
                        break;
                    case Actions.DISCONNECT:
                        System.out.println("CLIENT > User has left the game :( ...");
                        game.removeOpponent();
                        running = false;
                        break;
                    case Actions.GAME_PAUSE:
                        game.setGamePaused();
                        break;
                    case Actions.SCORE_CHANGE:
                        String level = (String) messageObject.get("level");
                        String lineCount = (String) messageObject.get("lineCount");
                        String points = (String) messageObject.get("points");
                        game.setScore(Integer.parseInt(level), Integer.parseInt(lineCount), Integer.parseInt(points));
                        break;
                    case Actions.ROW_DELETED:
                        String rows = (String) messageObject.get("rows");
                        game.setDeletedRows(Integer.parseInt(rows));
                        break;
                    default:
                        System.out.println("CLIENT > Invalid packet type: " + action);
                }
            } catch (ParseException e) {
                System.out.println("CLIENT > Packet could not be parsed: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("CLIENT > Problem with game control: " + e.getMessage());
            }
        }
    }

}
