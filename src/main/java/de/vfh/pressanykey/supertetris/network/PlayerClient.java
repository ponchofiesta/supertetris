package de.vfh.pressanykey.supertetris.network;

import de.vfh.pressanykey.supertetris.packages.Package00Login;

import java.io.IOException;
import java.net.*;

public class PlayerClient extends Thread{

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
            System.out.println("SERVER > " + new String(packet.getData()));
        }
    }

    private void gameLogin() {
        Package00Login loginPacket = new Package00Login(playerName);
        send(loginPacket.getData());
    }

    public void send(byte[] data) {
        assert(socket.isConnected());
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        System.out.println("Try sending packet to " + ipAddress + " : " + port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
