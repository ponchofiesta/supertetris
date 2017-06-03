package de.vfh.pressanykey.supertetris;

import java.io.*;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

/**
 * Manages threads for players connected to server
 */
public class TetrisServerThread extends Thread implements Observer {

    private CommunicationModel model;
    private static int clientCount = 0;
    private int clientNo;
    private String playerName;
    private Socket socket;
    private BufferedReader fromClient;
    private PrintWriter toClient;

    /**
     * Konstruktor,
     * baut Ueberwachungsmechanismus
     * und Datenstroeme zum/vom Client auf.
     * @param s Socket fuer Clientverbindung
     * @param m Modell auf dem Server
     */
    public TetrisServerThread(Socket s, CommunicationModel m, String playerName) {
        this.playerName = playerName;
        this.socket = s;
        // we watch if there are any changes in the model
        model = m;
        model.addObserver(this);
        clientNo = ++clientCount;
        try {
            // Read data
            fromClient = new BufferedReader
                    (new InputStreamReader(s.getInputStream()));
            toClient = new PrintWriter
                    (s.getOutputStream(), true);
            // Initial state
            model.setState
                    ("\t" + playerName + " betritt das Spiel," +
                            " Personen im Spiel: " + model.countObservers());
            // Thread
            start();
        } catch (Exception e) {
            System.out.println
                    ("\nThread fuer Client " + playerName + " konnte nicht gestartet\n " + e);
        }
    }


    /**
     * Interaktion,
     * liest und verarbeitet Nachrichten vom Client.
     */
    public void run()
    {
        System.out.println
                ( "Thread fuer Client " + playerName + " gestartet");
        try
        {
            String message;
            do
            {
                message = fromClient.readLine();
                if(message.equalsIgnoreCase("quit"))
                {
                    model.setState
                            ( "\t" + playerName + " verlaesst den Raum," +
                                    " Personen im Chatroom: " + (model.countObservers() - 1));
                    release();
                    return;
                }
                else
                {
                    System.out.println
                            ( playerName + " schreibt: " + message);
                    model.setState
                            ( "\t" + playerName + " schreibt: " + message);
                }
            } while(true);
        }
        catch( Exception e)
        {
            System.out.println
                    ( "\nVerbindung zum Client unterbrochen\n " + e);
            release();
        }
    }

    /**
    * Schliesst Verbindung zum Client,
    * setzt Model zurueck.
    */
    private void release()
    {
        System.out.println
                ( "Thread fuer " + playerName + " beendet");
        try
        {
            socket.close();
            model.deleteObserver(this);
            model = null;
        }
        catch( Exception e)
        {
            System.out.println
                    ( "\nFehler beim Schliessen des Clientprotokoll\n " + e);
        }
    }

    /**
     * Ueberschreibt Interfacemethode update des Observer,
     * teilt Client Aenderung im Modell mit.
     * @param m Modell, welches Aenderungen meldet
     * @param o geaendertes Objekt
     */
    public void update(Observable m, Object o)
    {
        if(model != m) {
            return;
        }
        System.out.println( "Datentransfer Client " + playerName);
        toClient.println( o);
    }

}
