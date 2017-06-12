package de.vfh.pressanykey.supertetris.network;

import de.vfh.pressanykey.supertetris.packages.Package00Login;
import de.vfh.pressanykey.supertetris.packages.Package01Disconnect;
import de.vfh.pressanykey.supertetris.packages.Packet;

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


//    public void connect(GameController game, String ipAddress, int port) {
    public void connect(String ipAddress, int port, String playerName) {
        try {
            //        this.game = game;
            this.playerName = playerName;
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
                packet = new Package00Login(data);
                System.out.println("CLIENT > User " + ((Package00Login)packet).getUsername() + " has connected with " + address + " : " + port);
                // TODO: add player to game
                break;
            case DISCONNECT:
                packet = new Package01Disconnect(data);
                System.out.println("CLIENT > User " + ((Package01Disconnect)packet).getUsername() + " has left the game :( ...");
                // TODO: remove player from game
                break;
        }
    }

    private void gameLogin() {
        Package00Login loginPacket = new Package00Login(playerName);
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
