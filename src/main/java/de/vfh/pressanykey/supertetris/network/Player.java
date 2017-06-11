package de.vfh.pressanykey.supertetris.network;

/**
 * Created by claudia on 12.06.17.
 */
public abstract class Player {


    protected boolean isMoving;
    protected String name;

    public Player(String name) {
        init();
        this.name = name;
    }

    public final void init() {

    }

}
