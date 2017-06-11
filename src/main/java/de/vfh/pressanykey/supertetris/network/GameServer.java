package de.vfh.pressanykey.supertetris.network;

import de.vfh.pressanykey.supertetris.packages.Package00Login;
import de.vfh.pressanykey.supertetris.packages.Packet;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class GameServer extends Thread {

    private int port;
    private DatagramSocket socket;
    private final int MAX_PACKET_SIZE = 1024;

    private List<PlayerOnServer> allPlayers = new ArrayList<>();
//
//    public connect(GameController game, String ipAddress, int port) {
    public void connect() {
//        this.game = game;
        try {
            this.socket = new DatagramSocket();
            this.port = socket.getLocalPort();
            System.out.println("DatagramSocket is open on port " + this.port);
        } catch (SocketException e) {
             e.printStackTrace();
        }
    }

    public void run() {
        System.out.println("Server is running.");
        while(true) {
//            DatagramPacket packet = new DatagramPacket(receiveDataBuffer, MAX_PACKET_SIZE);
            byte[] data = new byte[MAX_PACKET_SIZE];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            processPacket(packet.getData(), packet.getAddress(), packet.getPort());
        }
    }

    private void processPacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        Packet.PacketTypes type = Packet.lookupPacket(message.substring(0,2));
        switch(type) {
            default:
            case INVALID:
                break;
            case LOGIN:
                Package00Login packet = new Package00Login(data);
                System.out.println("User " + packet.getUsername() + " has connected with " + address + " : " + port);
                PlayerOnServer player = new PlayerOnServer(address, port, packet.getUsername());
                this.allPlayers.add(player);
                break;
            case DISCONNECT:
                break;
        }

        dumpPacket(data, address, port);

    }

    public void send(byte[] data, InetAddress address, int port) {
        assert(socket.isConnected());
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendDataToAll(byte[] data) {
        for (PlayerOnServer p : allPlayers) {
            send(data, p.address, p.port);
        }
    }

    // for debugging
    private void dumpPacket(byte[] data, InetAddress address, int port) {

        System.out.println("--------------------------");
        System.out.println("PACKET: ");
        System.out.println("\t" + address + " : " + port);
        System.out.println("\tContents: ");
        System.out.print("\t\t");
        for (int i = 0; i < data.length; i++) {
            System.out.print((char) data[i]);
        }
        System.out.println("\n--------------------------");
    }


    public int getPortNumber() {
        return this.port;
    }

    // Methods for getting the IP address of the machine on which the server is running:
    private List<Inet4Address> getInet4Addresses() throws SocketException {
        List<Inet4Address> adress = new ArrayList<Inet4Address>();

        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets)) {
            Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
            for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                    adress.add((Inet4Address)inetAddress);
                }
            }
        }

        return adress;
    }

    public String getHost4Address() throws SocketException {
        List<Inet4Address> inet4 = getInet4Addresses();
        return !inet4.isEmpty()
                ? inet4.get(0).getHostAddress()
                : null;
    }


}
