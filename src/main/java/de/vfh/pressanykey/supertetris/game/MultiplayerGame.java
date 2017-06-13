package de.vfh.pressanykey.supertetris.game;

import de.vfh.pressanykey.supertetris.network.Player;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.input.KeyCode;

import java.util.Observable;

/**
 * Created by claudia on 12.06.17.
 */
public class MultiplayerGame extends Observable {

    private Player myself = new Player();
    private Player opponent = new Player();
    private IntegerProperty playerCount = new SimpleIntegerProperty(0);

    public MultiplayerGame() {

    }


    public void addMyself(String name) {
        myself.init(name);
        playerCount.setValue(1);
    }

    public void addOpponent(String name) {
        opponent.init(name);
        playerCount.setValue(2);
    }

    public void removeOpponent() {
        opponent.clear();
    }

    public Player getOpponent() {
        return opponent;
    }

    public Player getMyself() {
        return myself;
    }

    public IntegerProperty getPlayerCount() {
        return playerCount;
    }

}
