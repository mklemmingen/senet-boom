package com.sonetboom.game;

public class Piece {
    public enum Color {
        BLACK,
        WHITE
    }

    public enum State {
        NORMAL,
        SELECTED
    }

    private Color color;
    private State state;

    public Piece(Color color) {
        this.color = color;
        this.state = State.NORMAL;
    }

    public Color getColor() {
        return this.color;
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
