package com.lifeistech.android.tournament;

public class RoundResult {
    String winner;
    String loser;
    String score;

    public RoundResult() {

    }

    public RoundResult(String winner, String loser, String score) {
        this.winner = winner;
        this.loser = loser;
        this.score = score;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void setLoser(String loser) {
        this.loser = loser;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getWinner() {
        return winner;
    }

    public String getLoser() {
        return loser;
    }

    public String getScore() {
        return score;
    }
}
