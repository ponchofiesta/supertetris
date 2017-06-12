package de.vfh.pressanykey.supertetris.game;

import de.vfh.pressanykey.supertetris.network.Player;
import de.vfh.pressanykey.supertetris.network.PlayerClient;
import de.vfh.pressanykey.supertetris.packages.Package01Disconnect;


public class MultiGameController extends GameController {

    private PlayerClient client;
    private Player myself;
//    protected Player opponent;

    public MultiGameController() {
        super();
        this.client = ViewController.client;
        myself = new Player(client.getPlayerName());
        System.out.println("client for " + client.getPlayerName() + " running on " + client.toString());
        board.addBoardListener(new BoardListener() {

            @Override
            void onMove() {
                // for testing:
                byte[] data = "moved".getBytes();
                client.send(data);
            }

        });
    }

    public void stop() {
        Package01Disconnect packet = new Package01Disconnect(myself.getName());
        packet.writeData(ViewController.client);
        stopwatch.stop();
        board.stop();
    }
}
