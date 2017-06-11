package de.vfh.pressanykey.supertetris.network;

import java.net.InetAddress;

/**
 * Stores the connection data of a player connected to server
 */
public class PlayerOnServer {

    public String name;
    public InetAddress address;
    public int port;
    public boolean status = false;
    public static int playerID = 123;

    public PlayerOnServer(InetAddress address, int port, String name) {
        playerID++;
        this.address = address;
        this.port = port;
        this.name = name;
        this.status = true;
    }

    public String getName() {
        return name;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
}
