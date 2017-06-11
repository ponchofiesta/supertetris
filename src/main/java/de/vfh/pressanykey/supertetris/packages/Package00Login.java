package de.vfh.pressanykey.supertetris.packages;

import de.vfh.pressanykey.supertetris.network.GameServer;
import de.vfh.pressanykey.supertetris.network.PlayerClient;

/**
 * Created by claudia on 11.06.17.
 */
public class Package00Login extends Packet {

    private String username;

    public Package00Login(byte[] data) {
        super(00);
        this.username = readData(data);
    }

    public Package00Login(String username) {
        super(00);
        this.username = username;
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
        return("00" + this.username).getBytes();
    }

    public String getUsername() {
        return username;
    }
}
