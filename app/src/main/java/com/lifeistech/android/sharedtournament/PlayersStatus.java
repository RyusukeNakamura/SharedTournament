package com.lifeistech.android.sharedtournament;

public class PlayersStatus {
    String name;
    private int winPoint;
    private int r1point;
    private int r2point;
    private int r3point;


    public PlayersStatus() {

    }

    public PlayersStatus(String name, int winPoint, int r1point, int r2point, int r3point) {
        this.r1point = r1point;
        this.r2point = r2point;
        this.r3point = r3point;
        this.name = name;
        this.winPoint = winPoint;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setWinPoint(int winPoint) {
        this.winPoint = winPoint;
    }

    public void setR1point(int r1point) {
        this.r1point = r1point;
    }


    public void setR2point(int r2point) {
        this.r2point = r2point;
    }

    public void setR3point(int r3point) {
        this.r3point = r3point;
    }

    public String getName() {
        return name;
    }

    public int getWinPoint() {
        return winPoint;
    }

    public int getR1point() {
        return r1point;
    }

    public int getR2point() {
        return r2point;
    }

    public int getR3point() {
        return r3point;
    }
}
