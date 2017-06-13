package de.vfh.pressanykey.supertetris.network;

import de.vfh.pressanykey.supertetris.game.Stone;
import org.json.simple.JSONObject;


public class ClientInterface {

    public static PlayerClient client;

    public ClientInterface(PlayerClient client) {
        this.client = client;
    }


    public void sendGameStarted() {
        JSONObject gameStartAction = new JSONObject();
        gameStartAction.put("action", Actions.GAME_START);
        String message = gameStartAction.toJSONString();
        client.send(message.getBytes());
    }

    public void sendLogin(String playerName) {
        JSONObject loginAction = new JSONObject();
        loginAction.put("action", Actions.LOGIN);
        loginAction.put("player", playerName);
        String message = loginAction.toJSONString();
        client.send(message.getBytes());
    }

    public void sendLogout() {
        JSONObject loginAction = new JSONObject();
        loginAction.put("action", Actions.DISCONNECT);
        String message = loginAction.toJSONString();
        client.send(message.getBytes());
    }

    public void sendStone(Stone stone) {
        JSONObject stoneAction = new JSONObject();
        stoneAction.put("action", Actions.NEW_STONE);
        stoneAction.put("stone", stone);
        String message = stoneAction.toJSONString();
        client.send(message.getBytes());
    }
}
