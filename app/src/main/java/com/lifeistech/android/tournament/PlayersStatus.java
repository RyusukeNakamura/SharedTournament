package com.lifeistech.android.tournament;

public class PlayersStatus {
    String name;
    int r1point;
    int r2point;
    int r3point;

    public PlayersStatus() {

    }

    public PlayersStatus(String name, int r1point, int r2point, int r3point) {
        this.r1point = r1point;
        this.r2point = r2point;
        this.r3point = r3point;
        this.name=name;
    }

    public void setName(String name) {
        this.name = name;
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
