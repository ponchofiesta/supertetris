package supertetris;

/**
 * The Game Controller
 */
public class GameController {

    private final Board board;

    public GameController() {
        this.board = new Board();
    }

    public Board getBoard() {
        return board;
    }
}
