package de.vfh.pressanykey.supertetris.network;

import de.vfh.pressanykey.supertetris.game.MultiplayerGame;
import de.vfh.pressanykey.supertetris.game.Stone;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.*;

public class PlayerClient extends Thread {

    private final int MAX_PACKET_SIZE = 1024;
    private int port;
    private InetAddress ipAddress;
    private DatagramSocket socket;
    private MultiplayerGame game;

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

    public void run() {
        while(true) {
            byte[] data = new byte[MAX_PACKET_SIZE];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
        }
    }

    @SuppressWarnings("unchecked")
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
                    case Actions.GAME_STOP:
                        break;
                    case Actions.CONNECT_STATE:
                        String player1 = (String) messageObject.getOrDefault("player1", "");
                        String player2 = (String) messageObject.getOrDefault("player2", "");
                        System.out.println("CLIENT > Connected players: " + player1 + " and " + player2);
                        if (!player1.equals("")) {
                            game.addMyself(player1);
                        }
                        if (!player2.equals("")) {
                            game.addOpponent(player2);
                        }
                        break;
                    case Actions.DISCONNECT:
                        System.out.println("CLIENT > User has left the game :( ...");
                        game.removeOpponent();
                        break;
                    case Actions.NEW_STONE:
                        Stone stone = (Stone) messageObject.get("stone");
                        game.setCurrentStone(stone);
                        System.out.println("CLIENT > Got new stone");
                    default:
                        System.out.println("CLIENT > Invalid packet type");
                }
            } catch (ParseException e) {
                System.out.println("CLIENT > Packet could not be parsed: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("CLIENT > Problem with game control: " + e.getMessage());
            }
        }
    }


    public void send(byte[] data) {
        assert(socket.isConnected());
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
