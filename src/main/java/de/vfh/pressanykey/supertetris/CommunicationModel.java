package de.vfh.pressanykey.supertetris;

import java.util.Observable;


public class CommunicationModel extends Observable {

    /**
     * Zuletzt gesendete Nachricht bzw. Startnachricht.
     */
    private String state = "Niemand da";


    /**
     * Lesen der Nachricht.
     * @return Nachricht
     */
    public String getState()
    {
        return state;
    }


    /**
     * Schreiben der Nachricht, synchronisiert.
     * @param n empfangene Nachricht
     */
    public synchronized void setState(String n)
    {
        state = n;
        setChanged();
        notifyObservers(state);
    }
}
