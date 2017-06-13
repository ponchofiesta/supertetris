package de.vfh.pressanykey.supertetris.game;

import javafx.application.Platform;
import javafx.beans.property.*;

import java.util.Observable;


public class MultiplayerGame extends Observable {

    private ViewController currentView;
    protected StringProperty myName = new SimpleStringProperty();
    protected StringProperty oppName = new SimpleStringProperty();
    protected IntegerProperty playerCount = new SimpleIntegerProperty(0);
    protected Stone currentStone;
    protected BooleanProperty gotStone = new SimpleBooleanProperty(false);


    public MultiplayerGame(ViewController view) {
        this.currentView = view;
    }


    public void addMyself(String name) {
        myName.setValue(name);
        playerCount.setValue(1);
    }

    public void addOpponent(String name) {
        oppName.setValue(name);
        playerCount.setValue(2);
    }


    public void startGame() throws Exception {
        Platform.runLater(() -> {
            try {
                currentView.setView(((ConnectionViewController)currentView).currentStage, "multiplayer.fxml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void removeOpponent() {
        oppName.setValue("");
    }

    public void setCurrentStone(Stone stone) {
        this.currentStone = stone;
        gotStone.setValue(true);
    }

}
