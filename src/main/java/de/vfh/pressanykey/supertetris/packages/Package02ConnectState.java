package de.vfh.pressanykey.supertetris.packages;

import de.vfh.pressanykey.supertetris.network.GameServer;
import de.vfh.pressanykey.supertetris.network.PlayerClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by claudia on 11.06.17.
 */
public class Package02ConnectState extends Packet {

    String firstPlayer = "";
    String secondPlayer = "";
    String message;

    public Package02ConnectState(byte[] data, boolean received) {
        super(02);
        message = readData(data, received);
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(message);
            JSONObject jsonObject = (JSONObject) obj;
            firstPlayer = (String) jsonObject.get("player1");
            secondPlayer = (String) jsonObject.get("player2");
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        return("02" + this.message).getBytes();
    }

    public String getFirstPlayer() {
        return firstPlayer;
    }

    public String getSecondPlayer() {
        return secondPlayer;
    }
}
