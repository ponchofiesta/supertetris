package de.vfh.pressanykey.supertetris.game;


import de.vfh.pressanykey.supertetris.SupertetrisApp;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The MultiBoardViewController handles the multiplayer view and displays the game state of the opponent.
 */
public class MultiBoardViewController extends BoardViewController {

    public Stage currentStage;
    @FXML
    protected StackPane oppBoardPaneContainer;
    @FXML
    protected StackPane oppBoardPane;
    @FXML
    protected Button btnPause;
    @FXML
    protected Label lblName;
    @FXML
    protected Label lblOppName;
    @FXML
    protected Label lblOppPoints;
    @FXML
    protected Label lblOppRows;
    @FXML
    protected Label lblOppLevel;
    @FXML
    protected BorderPane boardview;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        game.setView(this);
        currentStage = SupertetrisApp.getPrimaryStage();

        gameController = new MultiGameController();
        gameController.setView(this);
        boardPane.getChildren().add(gameController.getBoardPane());


        // bind stopwatch updates to view
        gameController.getStopwatch().addObserver((obj, value) -> this.lbTimer.setText((String)value));

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


        // Methods for multiplayer mode
        // Opponent's board
        oppBoardPane.getChildren().add(((MultiGameController)gameController).getOppBoardPane());

        // Set player names initially
        lblName.setText(game.myName.getValue());
        lblOppName.setText(game.oppName.getValue());

        // Delete opponent's name and score when he leaves the game
        game.oppName.addListener(((o, oldVal, newVal) -> {
            Platform.runLater(() -> {
                lblOppName.setText(game.oppName.getValue());
                lblOppRows.setText("");
                lblOppPoints.setText("");
                lblOppLevel.setText("");
                oppBoardPane.getChildren().clear();
            });
        }));

        // Pause game if the pause button is clicked
        game.gamePaused.addListener((o, oldVal, newVal) -> ((MultiGameController)gameController).pause(false));

        // If the opponent's score has changed, update the corresponding labels
        game.oppLines.addListener(((o, oldVal, newVal) -> {
            Platform.runLater(() -> {
                lblOppRows.setText(game.oppLines.getValue().toString());
                lblOppLevel.setText(game.oppLevel.getValue().toString());
                lblOppPoints.setText(game.oppPoints.getValue().toString());
            });
        }));

        // If the opponent has deleted a row, add them to the board
        game.deletedRows.addListener(((o, oldVal, newVal) -> {
            Platform.runLater(() -> ((MultiGameController)gameController).addRows((int) newVal));
        }));

        // If the opponent has dropped a new stone, redraw the opponent's board
        game.boardSignal.addListener(((o, oldVal, newVal) -> {
            Platform.runLater(() -> ((MultiGameController)gameController).redrawOppStoneMatrix(game.dropStoneMatrix));
        }));

        // If the opponent has moved the stone, redraw the stone to the board
        game.stoneSignal.addListener(((o, oldVal, newVal) -> {
            Platform.runLater(() -> ((MultiGameController)gameController).redrawFallingStone(game.fallingStone, game.stonePosX, game.stonePosY));
        }));

        gameController.start();

    }

    @FXML
    public void btnPauseClick(ActionEvent actionEvent) throws Exception {
        ((MultiGameController)gameController).pause(true);
    }


    /**
     * Shows a game over screen with the scores of both players
     * @param myName  Name of the current player
     * @param oppName  Name of the opponent
     * @param scores  Scores of the opponent
     */
    public void showGameOver(String myName, String oppName, Scores scores) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("gameover.fxml"));
            Stage stage = (Stage)btnStop.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 800, 500));
            GameoverViewController gocontroller = loader.getController();
            gocontroller.setScores(gameController.getScores());
            gocontroller.setNames(myName, oppName);
            gocontroller.setOppScores(scores);
            stage.show();
        } catch(Exception ex) {
            System.out.println("Could not show gameover screen:" + ex.getMessage());
        }
    }

}
