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

/**
 *
 * @author Claudia Kutter, Michael Richter
 */
@SuppressWarnings("unchecked")
public class GameServer extends Thread implements Runnable {

    // Attributes for client-server communication
    private final int PACKET_SIZE = 16384;
    private int port;
    private DatagramSocket socket;
    private boolean running;
    // List for storing all connected clients
    private List<PlayerOnServer> allPlayers = new ArrayList<>();

    private static GameServer instance;

    /**
     * Constructor
     */
    private GameServer() {

    }

    /**
     * Get Singleton instance
     * @return
     */
    public static GameServer getInstance() {
        if(instance == null) {
            instance = new GameServer();
        }
        return instance;
    }

    /**
     * Initializes the server by creating a socket
     */
    public void connect() {
        try {
            this.socket = new DatagramSocket();
            this.port = socket.getLocalPort();
            System.out.println("SERVER > DatagramSocket is open on port " + this.port);
        } catch (SocketException e) {
             e.printStackTrace();
        }
    }


    /**
     * Starts the server and listens for incoming datagram packets
     */
    public void run() {
        running = true;
        System.out.println("SERVER > Server is running.");
        while(running) {
            byte[] data = new byte[PACKET_SIZE];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            processPacket(packet.getData(), packet.getAddress(), packet.getPort());
        }
    }


    /**
     * Returns the port number of the server.
     * @return  Port number
     */
    public int getPortNumber() {
        return this.port;
    }


    /**
     * Return the public IP address of the server as string.
     * @return Server IP address
     * @throws SocketException
     */
    public String getHost4Address() throws SocketException {
        List<Inet4Address> inet4 = getInet4Addresses();
        return !inet4.isEmpty()
                ? inet4.get(0).getHostAddress()
                : null;
    }


    // Handles the processing of packages based on their package type which is stored in the "action" key
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
                    case Actions.GAME_OVER:
                        sendDataToAll(message.getBytes());
                        running = false;
                        System.out.println("SERVER > Server has shut down");
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
                        if(allPlayers.isEmpty()) {
                            running = false;
                            System.out.println("SERVER > Server has shut down");
                        }
                        break;
                    case Actions.STONE_MOVED:
                    case Actions.STONE_DROPPED:
                    case Actions.ROW_DELETED:
                    case Actions.SCORE_CHANGE:
                    case Actions.GAME_PAUSE:
                        sendToOthers(message.getBytes(), address, port);
                        break;
                    default:
                        System.out.println("SERVER > Invalid packet type: " + action);
                }
            } catch (ParseException e) {
                System.out.println("SERVER > Packet could not be parsed: " + e.getMessage());
            }
        }
    }


    // Forwards received data to all clients connected to the server
    private void sendDataToAll(byte[] data) {
        for (PlayerOnServer p : allPlayers) {
            send(data, p.address, p.port);
        }
    }


    // Forwards received data to all clients but the sender
    private void sendToOthers(byte[] data, InetAddress address, int port) {
        for (PlayerOnServer p : allPlayers) {
            if(p.getAddress().equals(address) && p.getPort() == port) {
                continue;
            }
            send(data, p.address, p.port);
        }
    }


    // Sends the names of all connected players so that everyone gets informed who is online
    private void sendPlayerState() {
        JSONObject state = new JSONObject();
        state.put("action", Actions.CONNECT_STATE);
        int count = 1;
        for (PlayerOnServer p : allPlayers) {
            state.put("player"+count, p.getName());
            count++;
        }
        String message = state.toJSONString();
        sendDataToAll(message.getBytes());
    }


    // Sends the data to a certain client, based on this IP address and port
    private void send(byte[] data, InetAddress address, int port) {
        assert(socket.isConnected());
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    // Finds the public IP address of the server
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


    // Returns the index of a player in the list of all players based on his IP address and port
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
}
