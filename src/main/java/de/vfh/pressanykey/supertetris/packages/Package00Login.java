package de.vfh.pressanykey.supertetris.packages;

import de.vfh.pressanykey.supertetris.network.GameServer;
import de.vfh.pressanykey.supertetris.network.PlayerClient;

/**
 * Created by claudia on 11.06.17.
 */
public class Package00Login extends Packet {

    private String username;

    public Package00Login(byte[] data, boolean received) {
        super(00);
        this.username = readData(data, received);
    }

    @Override
    public void writeData(PlayerClient client) {
        client.send(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAll(getData());
    }

    @Override
    public byte[] getData() {
        return("00" + username).getBytes();
    }

    public String getUsername() {
        return username;
    }
}
