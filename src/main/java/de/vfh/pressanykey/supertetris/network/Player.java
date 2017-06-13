package de.vfh.pressanykey.supertetris.network;

import de.vfh.pressanykey.supertetris.game.Scores;


public class Player {

    protected String name = "";
    protected Scores scores;  // player's level, line count, scores

    public void init(String name) {
        this.name = name;
        this.scores = new Scores();
    }

    public String getName() {
        return name;
    }

    public Scores getScores() {
        return scores;
    }

    public void clear() {
        this.name = "";
        this.scores = null;
    }

}
