package de.vfh.pressanykey.supertetris.game;

import javafx.application.Platform;
import javafx.beans.property.*;

import java.util.HashMap;
import java.util.List;
import java.util.Observable;

/**
 * Class for storing the connection state and the opponent's game state
 * @author Claudia Kutter, Ute Mayer, Michael Richter
 */
public class MultiplayerGame extends Observable {

    // Attributes for initializing the game
    private ViewController currentView;
    public StringProperty myName = new SimpleStringProperty(null);
    StringProperty oppName = new SimpleStringProperty(null);
    BooleanProperty playerSignal = new SimpleBooleanProperty(false);

    // Score attributes
    IntegerProperty oppLevel = new SimpleIntegerProperty(1);
    IntegerProperty oppLines = new SimpleIntegerProperty(0);
    IntegerProperty oppPoints = new SimpleIntegerProperty(0);

    // Game state (pause)
    BooleanProperty gamePaused = new SimpleBooleanProperty(false);
    BooleanProperty stopSignal = new SimpleBooleanProperty(false);

    // Board
    List<HashMap<String, Object>> dropStoneMatrix;
    BooleanProperty boardSignal = new SimpleBooleanProperty(false);
    IntegerProperty deletedRows = new SimpleIntegerProperty(0);

    // Stone
    List<HashMap<String, Object>> fallingStone;
    BooleanProperty stoneSignal = new SimpleBooleanProperty(false);
    int stonePosX;
    int stonePosY;

    private static MultiplayerGame instance;

    /**
     * Constructor
     */
    private MultiplayerGame() {
    }

    /**
     * Get Singleton instance
     * @return
     */
    public static MultiplayerGame getInstance() {
        if(instance == null) {
            instance = new MultiplayerGame();
        }
        return instance;
    }

    /**
     * Sets the view that the game is currently in
     * @param view View which the game currently uses
     */
    public void setView(ViewController view) {
        this.currentView = view;
    }

    /**
     * Adds the current player to the game
     * @param name Name of the current player
     */
    public void addMyself(String name) {
        myName.setValue(name);
        toggle(playerSignal);
    }

    /**
     * Adds the opponent to the game
     * @param name  Name of the opponent
     */
    public void addOpponent(String name) {
        System.out.println("Added opponent: " + name);
        oppName.setValue(name);
        toggle(playerSignal);
    }


    /**
     * Starts the game for both players
     * @throws Exception
     */
    public void startGame() throws Exception {
        Platform.runLater(() -> {
            try {
                currentView.setView("multiplayer.fxml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * Stops the game for both players
     */
    public void stopGame() {
        Scores oppScores = new Scores(oppLevel.getValue(), oppLines.getValue(), oppPoints.getValue());
        Platform.runLater(() -> {
            try {
                ((MultiBoardViewController)currentView).showGameOver(myName.getValue(), oppName.getValue(), oppScores);
                oppName.setValue(null);
                myName.setValue(null);
                dropStoneMatrix = null;
                fallingStone = null;
                toggle(stopSignal);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * Removes the opponent from the game
     */
    public void removeOpponent() {
        oppName.setValue(null);
        toggle(playerSignal);
    }


    /**
     * Stores the score of the opponent
     * @param level  Current level of the opponent
     * @param lineCount  Current line count of the opponent
     * @param points  Current points of the opponent
     */
    public void setScore(int level, int lineCount, int points) {
            oppLevel.setValue(level);
            oppLines.setValue(lineCount);
            oppPoints.setValue(points);
    }


    /**
     * Indicates whether the game is paused
     */
    public void setGamePaused() {
        toggle(gamePaused);
    }


    /**
     * Indicates that the opponent has deleted rows
     * @param rows Number of deleted rows
     */
    public void setDeletedRows(int rows) {
        deletedRows.setValue(rows);
        deletedRows.setValue(0);
    }


    /**
     * Indicates that the opponent's matrix of dropped stones has changed
     * @param dropStoneMatrix Block of dropped stones
     */
    public void setDroppedMatrix(List<HashMap<String, Object>> dropStoneMatrix) {
        this.dropStoneMatrix = dropStoneMatrix;
        toggle(boardSignal);
    }


    /**
     * Indicates that a stone was added
     * @param fallingStone Blocks of the new stone
     */
    public void setFallingStone(List<HashMap<String, Object>> fallingStone) {
        this.fallingStone = fallingStone;
        toggle(stoneSignal);
    }


    /**
     * Indicates that a stone was moved
     * @param stoneX x-coordinate of the stone
     * @param stoneY y-coordinate of the stone
     */
    public void setStonePosition(int stoneX, int stoneY) {
        this.stonePosX = stoneX;
        this.stonePosY = stoneY;
    }


    // Helper method for toggling boolean values
    private void toggle(BooleanProperty value) {
        if(value.getValue()) {
            value.setValue(false);
        } else {
            value.setValue(true);
        }
    }
}
