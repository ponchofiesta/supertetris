package de.vfh.pressanykey.supertetris;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class TetrisClient {

    public void startClient(String hostName, int portNumber, String playerName) {
        try {
            Socket tetrisSocket = new Socket(hostName, portNumber);
            System.out.println("Client is running");
            new TetrisClientThread(tetrisSocket).action();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName + ":" + portNumber);
            System.exit(1);
        }
    }
}