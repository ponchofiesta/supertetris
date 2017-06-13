package de.vfh.pressanykey.supertetris.game;

import de.vfh.pressanykey.supertetris.network.Player;
import de.vfh.pressanykey.supertetris.network.PlayerClient;
import de.vfh.pressanykey.supertetris.packages.Package01Disconnect;


public class MultiGameController extends GameController {

    private PlayerClient client;
    private Player myself;
    private Player opponent;


    public MultiGameController() {
        super();
        this.client = ViewController.client;
        this.myself = myself;
        this.opponent = opponent;


        // check what's happening on the board and send it to the opponent
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

    public String getPlayerName() {
        return myself.getName();
    }

    public String getOpponentName() {
        return opponent.getName();
    }
}
