package com.senetboom.game.backend;

public class Coordinate {
    /*
    Holds two int values, x and y.
     */
    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
