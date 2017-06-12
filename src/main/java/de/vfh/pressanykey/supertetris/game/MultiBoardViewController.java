package de.vfh.pressanykey.supertetris.game;


import java.net.URL;
import java.util.ResourceBundle;

public class MultiBoardViewController extends BoardViewController {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // TODO:
        // kopiert momentan die komplete initialize-Methode vom BoardViewController; einzige Änderung:
        // gameController wird als MultiGameController instanziiert
        gameController = new MultiGameController();
        gameController.setView(this);
        boardPane.getChildren().add(gameController.getBoardPane());

        // bind stopwatch updates to view
        gameController.getStopwatch().addObserver((obj, value) -> {
            this.lbTimer.setText((String)value);
        });

        // bind score updates to view
        gameController.getScores().addObserver((o, arg) -> {
            lblPoints.setText(gameController.getScores().getPoints().toString());
            lblLevel.setText(gameController.getScores().getLevel().toString());
            lblRows.setText(gameController.getScores().getLineCount().toString());
        });

        // bind preview stone updates to view
        gameController.getBoard().addBoardListener(new BoardListener() {
            @Override
            void onNext(Stone stone) {
                nextField.getChildren().clear();
                nextField.getChildren().add(stone);
            }
        });

        gameController.start();

    }


}
