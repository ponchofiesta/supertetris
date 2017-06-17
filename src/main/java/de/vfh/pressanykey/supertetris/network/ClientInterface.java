package de.vfh.pressanykey.supertetris.network;

import de.vfh.pressanykey.supertetris.game.Scores;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class ClientInterface {

    public static PlayerClient client;

    /**
     * Constructor
     * @param client Client that should be handled through this interface
     */
    public ClientInterface(PlayerClient client) {
        this.client = client;
    }


    /**
     * Sends message that the game was started
     */
    public void sendGameStarted() {
        JSONObject gameStartAction = new JSONObject();
        gameStartAction.put("action", Actions.GAME_START);
        String message = gameStartAction.toJSONString();
        client.send(message.getBytes());
    }


    /**
     * Sends message that a player has connected to the game
     */
    public void sendLogin(String playerName) {
        JSONObject loginAction = new JSONObject();
        loginAction.put("action", Actions.LOGIN);
        loginAction.put("player", playerName);
        String message = loginAction.toJSONString();
        client.send(message.getBytes());
    }


    /**
     * Sends message that a player has logged out from the game
     */
    public void sendLogout() {
        JSONObject loginAction = new JSONObject();
        loginAction.put("action", Actions.DISCONNECT);
        String message = loginAction.toJSONString();
        client.send(message.getBytes());
    }


    /**
     * Sends message that a player has lost the game
     */
    public void sendGameOver() {
        JSONObject gameOverAction = new JSONObject();
        gameOverAction.put("action", Actions.GAME_OVER);
        String message = gameOverAction.toJSONString();
        client.send(message.getBytes());
    }


    /**
     * Sends message that a player's scroe has changed
     * @param score  Score of the player
     */
    public void sendScore(Scores score) {
        JSONObject scoreAction = new JSONObject();
        scoreAction.put("action", Actions.SCORE_CHANGE);
        scoreAction.put("level", score.getLevel().toString());
        scoreAction.put("lineCount", score.getLineCount().toString());
        scoreAction.put("points", score.getPoints().toString());
        String message = scoreAction.toJSONString();
        client.send(message.getBytes());
    }


    /**
     * Sends message that the game was paused
     */
    public void sendPause(Boolean isPaused) {
        JSONObject pauseAction = new JSONObject();
        pauseAction.put("action", Actions.GAME_PAUSE);
        pauseAction.put("state", isPaused.toString());
        String message = pauseAction.toJSONString();
        client.send(message.getBytes());
    }

    /**
     * Sends message that rows were deleted
     * @param count Number of deleted rows
     */
    public void sendDeletedRows(int count) {
        JSONObject deleteAction = new JSONObject();
        deleteAction.put("action", Actions.ROW_DELETED);
        deleteAction.put("rows", String.valueOf(count));
        String message = deleteAction.toJSONString();
        client.send(message.getBytes());
    }
}
