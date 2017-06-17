package de.vfh.pressanykey.supertetris.game;

import javafx.application.Platform;
import javafx.beans.property.*;
import java.util.Observable;

/**
 * Class for storing the connection state and the opponent's game state
 */
public class MultiplayerGame extends Observable {

    // Attributes for initializing the game
    private ViewController currentView;
    public StringProperty myName = new SimpleStringProperty();
    StringProperty oppName = new SimpleStringProperty();
    IntegerProperty playerCount = new SimpleIntegerProperty(0);

    // Score attributes
    IntegerProperty oppLevel = new SimpleIntegerProperty(1);
    IntegerProperty oppLines = new SimpleIntegerProperty(0);
    IntegerProperty oppPoints = new SimpleIntegerProperty(0);
    // Game state (pause)
    BooleanProperty gamePaused = new SimpleBooleanProperty(false);

    // Board
    BooleanProperty boardSignal = new SimpleBooleanProperty(false);
    IntegerProperty deletedRows = new SimpleIntegerProperty(0);


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
        int count = playerCount.getValue();
        playerCount.setValue(count + 1);
    }


    /**
     * Adds the opponent to the game
     * @param name  Name of the opponent
     */
    public void addOpponent(String name) {
        oppName.setValue(name);
        int count = playerCount.getValue();
        playerCount.setValue(count + 1);
    }


    /**
     * Starts the game for both players
     * @throws Exception
     */
    public void startGame() throws Exception {
        Platform.runLater(() -> {
            try {
                currentView.setView(((LobbyViewController)currentView).currentStage, "multiplayer.fxml");
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * Removes the opponent from the game
     */
    public void removeOpponent() {
        oppName.setValue("");
        int count = playerCount.getValue();
        playerCount.setValue(count - 1);
    }


    /**
     * Stores the score of the opponent
     * @param level  Current level of the opponent
     * @param lineCount  Current line count of the opponent
     * @param points  Current points of the opponent
     */
    public void setScore(int level, int lineCount, int points) {
            oppPoints.setValue(points);
            oppLevel.setValue(level);
            oppLines.setValue(lineCount);
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


    // Helper method for toggling boolean values
    private void toggle(BooleanProperty value) {
        if(value.getValue()) {
            value.setValue(false);
        } else {
            value.setValue(true);
        }
    }
}
