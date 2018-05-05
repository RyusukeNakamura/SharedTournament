package com.lifeistech.android.tournament;

public class RoundResult {
    String winner;
    String loser;
    int point;

    public RoundResult() {

    }

    public RoundResult(String winner, String loser, int point) {
        this.winner = winner;
        this.loser = loser;
        this.point=point;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void setLoser(String loser) {
        this.loser = loser;
    }

    public void setScore(int point) {
        this.point = point;
    }

    public String getWinner() {
        return winner;
    }

    public String getLoser() {
        return loser;
    }

    public int getPoint() {
        return point;
    }
}
