package de.vfh.pressanykey.supertetris;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Handles the connection of players
 */
public class TetrisServer {

    private int portNumber;

    public void startServer(String playerName) {
        boolean listening = true;
        try {
            // Create socket
            ServerSocket serverSocket = new ServerSocket(0);
            portNumber = serverSocket.getLocalPort();
            System.out.println("Server socket up and running");

            // Listen for connections until two players are connected
            // Set up a new thread for each connected player
            int connectNumber = 0;
            while (listening) {
                // Waiting for client to connect
                System.out.println("Listening on port " + portNumber);

                // Start thread for new player
                TetrisServerThread client = new TetrisServerThread(serverSocket.accept(), playerName);
                client.start();

                // Count connections and stop listening as soon as two players are connected
                connectNumber++;
                if (connectNumber == 2) {
                    listening = false;
                }
            }
            System.out.println("Server has stopped listening");
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }

    public int getPortNumber() {
        return portNumber;
    }

    // Methods for getting the IP address of the machine on which the server is running:
    private List<Inet4Address> getInet4Addresses() throws SocketException {
        List<Inet4Address> ret = new ArrayList<Inet4Address>();

        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets)) {
            Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
            for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                    ret.add((Inet4Address)inetAddress);
                }
            }
        }

        return ret;
    }

    public String getHost4Address() throws SocketException {
        List<Inet4Address> inet4 = getInet4Addresses();
        return !inet4.isEmpty()
                ? inet4.get(0).getHostAddress()
                : null;
    }
}
