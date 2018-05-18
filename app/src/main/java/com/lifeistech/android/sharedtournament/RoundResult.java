package com.lifeistech.android.sharedtournament;

public class RoundResult {
    public RoundResult() {

    }

    int up;
    int down;
    String winner;
    String loser;
    String memo;

    public RoundResult(int up, int down, String winner, String loser, String memo) {
        this.loser = loser;
        this.winner = winner;
        this.down = down;
        this.up = up;
        this.memo = memo;
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

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMemo() {
        return memo;
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
