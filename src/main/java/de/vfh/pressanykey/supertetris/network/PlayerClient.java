package de.vfh.pressanykey.supertetris.network;

import de.vfh.pressanykey.supertetris.game.MultiplayerGame;
import de.vfh.pressanykey.supertetris.packages.Package00Login;
import de.vfh.pressanykey.supertetris.packages.Package01Disconnect;
import de.vfh.pressanykey.supertetris.packages.Package02ConnectState;
import de.vfh.pressanykey.supertetris.packages.Packet;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.*;

public class PlayerClient extends Thread {

    private final byte[] PACKET_HEADER = {0x24, 0x40, 0x21, 0x5D};      // we give our datagrams a unique header of four signs: $@!]
    private final byte[] DATA_SPLIT = {0x23, 0x23}; // we use ## as splitter sign in the datagram
    private final byte[] CONNECTION_MARK = {0x3F, 0x3E}; // we use ?> as sign to tell the server of our connection
    private final int MAX_PACKET_SIZE = 1024;

    private int port;
    private InetAddress ipAddress;
    private DatagramSocket socket;
    private String playerName;
    private MultiplayerGame game;


//    public void connect(GameController game, String ipAddress, int port) {
    public void connect(String ipAddress, int port, String playerName, MultiplayerGame game) {
        try {
            this.game = game;
            this.playerName = playerName;
            // handle connection
            this.port = port;
            this.ipAddress = InetAddress.getByName(ipAddress);
            this.socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        System.out.println("Client is running.");
        gameLogin();
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

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        Packet.PacketTypes type = Packet.lookupPacket(message.substring(0,2));
        Packet packet = null;
        switch(type) {
            default:
            case INVALID:
                break;
            case LOGIN:
                System.out.println("CLIENT > User " + ((Package00Login)packet).getUsername() + " has connected with " + address + " : " + port);
                break;
            case DISCONNECT:
                packet = new Package01Disconnect(data, true);
                System.out.println("CLIENT > User " + ((Package01Disconnect)packet).getUsername() + " has left the game :( ...");
                game.removeOpponent();
                break;
            case CONNECTSTATE:
                packet = new Package02ConnectState(data, true);
                String player1 = ((Package02ConnectState)packet).getFirstPlayer();
                String player2 = ((Package02ConnectState)packet).getSecondPlayer();
                System.out.println("CLIENT > Connected players: " + player1 + " and " + player2);
                if(player1 != null) { game.addMyself(player1); }
                if(player2 != null) { game.addOpponent(player2); }
                break;
        }
    }

    private void gameLogin() {
        String message = playerName;
        Package00Login loginPacket = new Package00Login(message.getBytes(), false);
        send(loginPacket.getData());
    }

    public void send(byte[] data) {
        assert(socket.isConnected());
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
//        System.out.println("Try sending packet to " + ipAddress + " : " + port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPlayerName() {
        return playerName;
    }

}
