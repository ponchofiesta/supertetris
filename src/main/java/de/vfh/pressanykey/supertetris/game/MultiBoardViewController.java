package de.vfh.pressanykey.supertetris.game;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MultiBoardViewController extends BoardViewController {


    @FXML
    protected StackPane oppBoardPaneContainer;
    @FXML
    protected StackPane oppBoardPane;

    @FXML
    protected Label lblName;
    @FXML
    protected Label lblOppPoints;
    @FXML
    protected Label lblOppRows;
    @FXML
    protected Label lblOppLevel;
    @FXML
    protected Label lblOppName;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // TODO:
        // kopiert momentan die komplete initialize-Methode vom BoardViewController; einzige Änderung:
        // gameController wird als MultiGameController instanziiert
        gameController = new MultiGameController();
        gameController.setView(this);
        boardPane.getChildren().add(gameController.getBoardPane());


        //opponent's board
        oppBoardPane.getChildren().add(((MultiGameController)gameController).getOppBoardPane());


        // Set player names initially
        lblName.setText(game.myName.getValue());
        lblOppName.setText(game.oppName.getValue());

        // Delete opponent's name when he leaves the game
        game.oppName.addListener(((o, oldVal, newVal) -> {
            Platform.runLater(() -> {
                lblOppName.setText(game.oppName.getValue());
            });
        }));


        //show opponents stone
        game.gotStone.addListener(((o, oldVal, newVal) -> {
            Platform.runLater(() -> {
                oppBoardPane.getChildren().add(game.currentStone);
            });
        }));



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
