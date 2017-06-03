package de.vfh.pressanykey.supertetris;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by claudia on 31.05.17.
 */
public class TetrisClientThread extends Thread {
    private Socket client;
    private BufferedReader fromServer;
    private PrintWriter toServer;

    /**
     * Konstruktor,
     * baut Datenstroeme zum/vom Server auf.
     * @param client Socket fuer Serververbindung
     */
    public TetrisClientThread(Socket client)
    {
        try
        {
        // Socket, Datenstroeme
            this.client = client;
            toServer = new PrintWriter
                    ( client.getOutputStream(), true);
            fromServer = new BufferedReader
                    ( new InputStreamReader( client.getInputStream()));
        // Thread
            start();
        }
        catch( Exception e)
        {
            System.out.println
                    ( "\nClient Thread konnte nicht gestartet werden\n " + e);
        }
    }
    /**
     * Interaktion,
     * liest und verarbeitet Nachrichten vom Server.
     */
    public void run()
    {
        try
        {
            String nachricht;
            while(true)
            {
                nachricht = fromServer.readLine();
                if( nachricht != null)
                    System.out.println( nachricht);
            }
        }
        catch( Exception e)
        {
            if( !isInterrupted())
            {
                System.out.println
                        ( "\nVerbindung zum Server unterbrochen\n " + e);
                release();
            }
        }
    }


    /**
     * Nutzereingaben,
     * Eingabe und Versenden von Nachrichten.
     */
    public void action()
    {
        BufferedReader vonTastatur = new BufferedReader
                ( new InputStreamReader( System.in));
        System.out.println
                ( "Dialog gestartet (Dialog mit QUIT beenden)");
        try
        {
            String nachricht;
            do
            {
                nachricht = vonTastatur.readLine();
                if( nachricht.equalsIgnoreCase( "quit"))
                {
                    release();
                    return;
                }
                else toServer.println( nachricht);
            } while( true);
        }
        catch( Exception e)
        {
            System.out.println
                    ( "\nVerbindung zum Server unterbrochen\n " + e);
            release();
        }
    }


    /**
     * Beendet ChatClient,
     * meldet Abbruch dem Server,
     * bricht Verbindung zum Server ab,
     * bricht Programm ab.
     */
    protected void release()
    {
// Thread beenden
        interrupt();
        try
        {
// Server informieren
            System.out.println( "Dialog beendet");
            toServer.println( "quit");
// Clientsocket schliessen
            client.close();
            System.out.println( "Client abgemeldet");
        }
        catch( Exception e)
        {
            System.out.println
                    ( "\nFehler beim Schliessen des Clients\n " + e);
        }
// Programm beenden
        System.exit( 0);
    }
}

