package de.vfh.pressanykey.supertetris;

import java.io.*;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Manages threads for players connected to server
 */
public class TetrisServerThread extends Thread {
    private Socket socket = null;
    private String playerName;
    // we store all clients in a vector
    protected static Vector<TetrisServerThread> handlers = new Vector<>();
    PrintWriter out;
    BufferedReader in;

    public TetrisServerThread(Socket socket, String playerName) {
        super("TetrisServerThread");
        this.socket = socket;
        this.playerName = playerName;
    }

    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // add client to handler
            handlers.addElement(this);
            out.println("Player has connected: " + playerName);

            // send or receive messages
            String input, output;

            // später brauchen wir besser DataInput und Output streams, dann schreiben wir hier
            // out.writeUTF(output)
            // out.flush()

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
