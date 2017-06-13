package de.vfh.pressanykey.supertetris.network;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
        if(message.startsWith("{")){
            try {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(message);
                JSONObject messageObject = (JSONObject) obj;
                String action = (String) messageObject.get("action");
                switch (action) {
                    case Actions.GAME_START:
                        sendDataToAll(message.getBytes());
                        break;
                    case Actions.GAME_STOP:
                        break;
                    case Actions.LOGIN:
                        String player = (String) messageObject.getOrDefault("player", "");
                        System.out.println("SERVER > User " + player + " has connected with " + address + " : " + port);
                        PlayerOnServer gamePlayer = new PlayerOnServer(address, port, player);
                        allPlayers.add(gamePlayer);
                        sendPlayerState();
                        break;
                    case Actions.DISCONNECT:
                        sendDataToAll(message.getBytes());
                        allPlayers.remove(getPlayerIndex(address, port));
                        break;
                    case Actions.NEW_STONE:
                        sendToOthers(message.getBytes(), address, port);
                    default:
                        System.out.println("SERVER > Invalid packet type");
                }
            } catch (ParseException e) {
                System.out.println("SERVER > Packet could not be parsed: " + e.getMessage());
            }
        }
//        dumpPacket(data, address, port);

    }

    private void sendPlayerState() {
        JSONObject state = new JSONObject();
        state.put("action", Actions.CONNECT_STATE);
        int count = 1;
        //TODO in der message unterscheiden zwischen "ich" und "gegner" statt player1 und player2
        for (PlayerOnServer p : allPlayers) {
            state.put("player"+count, p.getName());
            count++;
        }
        String message = state.toJSONString();
        sendDataToAll(message.getBytes());
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

    public void sendToOthers(byte[] data, InetAddress address, int port) {
        for (PlayerOnServer p : allPlayers) {
            if(p.getAddress().equals(address) && p.getPort() == port) {
                continue;
            }
            send(data, p.address, p.port);
        }
    }

    private int getPlayerIndex(InetAddress address, int port) {
        int index = 0;
        for (PlayerOnServer p : allPlayers) {
            if(p.getAddress().equals(address) && p.getPort() == port) {
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
