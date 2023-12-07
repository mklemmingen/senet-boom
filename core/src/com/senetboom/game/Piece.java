package com.senetboom.game;

import java.util.ArrayList;

public class Piece {
    public enum Color {
        BLACK,
        WHITE,
    }

    public enum State {
        NORMAL,
        SELECTED
    }

    private Color colour;
    private State state;

    public Piece(Color color) {
        this.colour = color;
        this.state = State.NORMAL;
    }

    public ArrayList<Tile> calculateMove(int x, int y, int newX, int newY) {
        // calculate if the move is valid
        // if it is, return true
        // if it isn't, return false
        return null;
    }

    public Color getColour() {
        return this.colour;
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
