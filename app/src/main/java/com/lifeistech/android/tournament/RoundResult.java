package com.lifeistech.android.tournament;

public class RoundResult {
    public RoundResult() {

    }

    int up;
    int down;
    String winner;
    String loser;

    public RoundResult(int up, int down, String winner, String loser) {
        this.loser = loser;
        this.winner = winner;
        this.down = down;
        this.up = up;
    }

    public void setUp(int up) {
        this.up = up;
    }

    public int getUp() {
        return up;
    }

    public void setDown(int down) {
        this.down = down;
    }

    public int getDown() {
        return down;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getLoser() {
        return loser;
    }

    public String getWinner() {
        return winner;
    }

    public void setLoser(String loser) {
        this.loser = loser;
    }
}
