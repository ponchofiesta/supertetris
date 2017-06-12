package de.vfh.pressanykey.supertetris.packages;

import de.vfh.pressanykey.supertetris.network.GameServer;
import de.vfh.pressanykey.supertetris.network.PlayerClient;

/**
 * Created by claudia on 11.06.17.
 */
public class Package01Disconnect extends Packet {

    private String username;

    public Package01Disconnect(byte[] data) {
        super(01);
        this.username = readData(data);
    }

    public Package01Disconnect(String username) {
        super(01);
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
        return("01" + this.username).getBytes();
    }

    public String getUsername() {
        return username;
    }
}
