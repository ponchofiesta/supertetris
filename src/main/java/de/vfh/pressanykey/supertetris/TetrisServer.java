package de.vfh.pressanykey.supertetris;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

/**
 * Handles the connection of players
 */
public class TetrisServer {

    private int portNumber;

    public void startServer() {
        boolean listening = true;
        try {
            // Create socket
            ServerSocket serverSocket = new ServerSocket(0);
            portNumber = serverSocket.getLocalPort();

            // Listen for connections until two players are connected
            // Set up a new thread for each connected player
            int connectNumber = 0;
            while (listening) {
                System.out.println("listening on: " + portNumber);
                new TetrisServerThread(serverSocket.accept()).start();
                System.out.println("connected new player");
                connectNumber++;
                if (connectNumber == 2) {
                    listening = false;
                }
            }
            System.out.println("stopped listening");
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }

    public int getPortNumber() {
        return portNumber;
    }

    public String getIP() {
        String serverIP = null;
        try {
            serverIP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return serverIP;
    }
}
