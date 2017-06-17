package de.vfh.pressanykey.supertetris.network;

/**
 * Defines all possible actions for the client-server communication.
 */
public interface Actions {

    public static String LOGIN = "login";
    public static String DISCONNECT = "disconnect";
    public static String CONNECT_STATE = "connectstate";
    public static String GAME_START = "gamestart";
    public static String GAME_PAUSE = "gamepause";
    public static String GAME_OVER = "gameover";
    public static String SCORE_CHANGE = "scorechange";
    public static String ROW_DELETED = "rowdeleted";

}
