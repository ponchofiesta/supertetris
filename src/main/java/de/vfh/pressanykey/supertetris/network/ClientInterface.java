package de.vfh.pressanykey.supertetris.network;

import de.vfh.pressanykey.supertetris.game.Scores;
import de.vfh.pressanykey.supertetris.game.Stone;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Claudia Kutter
 */
@SuppressWarnings("unchecked")
public class ClientInterface {

    public static PlayerClient client;

    /**
     * Constructor
     *
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
     *
     * @param score Score of the player
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
     *
     * @param count Number of deleted rows
     */
    public void sendDeletedRows(int count) {
        JSONObject deleteAction = new JSONObject();
        deleteAction.put("action", Actions.ROW_DELETED);
        deleteAction.put("rows", String.valueOf(count));
        String message = deleteAction.toJSONString();
        client.send(message.getBytes());
    }


    /**
     * Sends message that block with dropped stones has changed
     *
     * @param droppedStones Block of all dropped stones
     */
    public void sendDroppedStones(Rectangle[][] droppedStones) {
        JSONObject message = new JSONObject();
        message.put("action", Actions.STONE_DROPPED);

        // Serialize the dropped stone matrix
        JSONArray droppedStoneMatrix = new JSONArray();
        for (int i = 0; i < droppedStones.length; i++) {
            for (int j = 0; j < droppedStones[0].length; j++) {
                // create new rectangle for this block
                Rectangle rectangle = droppedStones[i][j];
                // only consider filled blocks
                if (rectangle != null) {
                    Color color = (Color) rectangle.getFill();
                    JSONObject stoneProperties = new JSONObject();
                    stoneProperties.put("x", j);
                    stoneProperties.put("y", i);
                    stoneProperties.put("color", color.toString());
                    droppedStoneMatrix.add(stoneProperties);
                }
            }
        }
        message.put("dropStoneMatrix", droppedStoneMatrix.toJSONString());
        client.send(message.toJSONString().getBytes());
    }


    /**
     * Sends message that a stone is falling
     *
     * @param currentStone Currently falling stone
     */
    public void sendStonePosition(Stone currentStone, int stoneX, int stoneY) {
        JSONObject message = new JSONObject();
        message.put("action", Actions.STONE_MOVED);

        //Serialize falling stone
        int[][] stoneMatrix = currentStone.getMatrix();
        JSONArray fallingStone = new JSONArray();
        for (int i = 0; i < stoneMatrix.length; i++) {
            for (int j = 0; j < stoneMatrix[0].length; j++) {
                // only consider filled blocks
                if (stoneMatrix[i][j] == 1) {
                    JSONObject stoneProperties = new JSONObject();
                    stoneProperties.put("x", j);
                    stoneProperties.put("y", i);
                    stoneProperties.put("color", currentStone.getColor().toString());
                    fallingStone.add(stoneProperties);
                }
            }
        }
        message.put("fallingStone", fallingStone.toJSONString());
        // Add stone position
        message.put("stoneX", String.valueOf(stoneX));
        message.put("stoneY", String.valueOf(stoneY));
        client.send(message.toJSONString().getBytes());
    }

}


