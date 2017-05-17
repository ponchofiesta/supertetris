package de.vfh.pressanykey.supertetris;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Manages threads for players connected to server
 */
public class TetrisServerThread extends Thread {
    private Socket socket = null;

    public TetrisServerThread(Socket socket) {
        super("TetrisServerThread");
        this.socket = socket;
    }

    public void run() {
        try {

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String input, output;
            output = "Hallo, hier spricht der Server";
            out.println(output);

            while ((input = in.readLine()) != null) {
                output = input;
                out.println(output);
            }
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
