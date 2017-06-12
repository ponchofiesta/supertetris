package de.vfh.pressanykey.supertetris.network;

import de.vfh.pressanykey.supertetris.packages.Package00Login;
import de.vfh.pressanykey.supertetris.packages.Package01Disconnect;
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

    public void connect() {
        try {
            this.socket = new DatagramSocket();
            this.port = socket.getLocalPort();
            System.out.println("SERVER > DatagramSocket is open on port " + this.port);
        } catch (SocketException e) {
             e.printStackTrace();
        }
    }

    public void run() {
        System.out.println("SERVER > Server is running.");
        while(true) {
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
        Packet packet;
        switch(type) {
            default:
            case INVALID:
                break;
            case LOGIN:
                packet = new Package00Login(data);
                System.out.println("SERVER > User " + ((Package00Login)packet).getUsername() + " has connected with " + address + " : " + port);
                PlayerOnServer player = new PlayerOnServer(address, port, ((Package00Login)packet).getUsername());
                this.addConnection(player, (Package00Login) packet);
                break;
            case DISCONNECT:
                packet = new Package01Disconnect(data);
                System.out.println("SERVER > User " + ((Package01Disconnect)packet).getUsername() + " has disconnected :( ...");
                this.removeConnection((Package01Disconnect)packet);
                break;
        }

//        dumpPacket(data, address, port);

    }

    private void send(byte[] data, InetAddress address, int port) {
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

    private void addConnection(PlayerOnServer player, Package00Login packet) {
        boolean alreadyConnected = false;
        for (PlayerOnServer p : allPlayers) {
            if(player.getName().equalsIgnoreCase(p.getName())) {
                if(p.address == null) {
                    p.address = player.address;
                }
                if(p.port == -1) {
                    p.port = player.port;
                }
                alreadyConnected = true;
            } else {
                send(packet.getData(), p.address, p.port);
            }
        }
        if(!alreadyConnected) {
            this.allPlayers.add(player);
        }
    }

    private void removeConnection(Package01Disconnect packet) {
        allPlayers.remove(getPlayerIndex(packet.getUsername()));
        packet.writeData(this);
    }

    private int getPlayerIndex(String username) {
        int index = 0;
        for (PlayerOnServer p : allPlayers) {
            if(p.getName().equalsIgnoreCase(username)) {
                break;
            }
            index++;
        }
        return index;
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
        List<Inet4Address> serverAddress = new ArrayList<>();
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets)) {
            Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
            for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                    serverAddress.add((Inet4Address)inetAddress);
                }
            }
        }
        return serverAddress;
    }

    public String getHost4Address() throws SocketException {
        List<Inet4Address> inet4 = getInet4Addresses();
        return !inet4.isEmpty()
                ? inet4.get(0).getHostAddress()
                : null;
    }


}
