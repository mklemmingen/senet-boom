package com.senetboom.game;

public class Tile {
    /*
    Tiles of the board. The hold a piece or not.
                        They hold a special ability or not.
     */
    private Piece piece;
    private SpecialState specialState;

    public enum SpecialState {
        NONE,
        HOUSE,
        WATER,
        SAFE
    }

    public Tile() {
        this.piece = null;
        this.specialState = SpecialState.NONE;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Piece getPiece() {
        return this.piece;
    }

    public void setSpecialState(SpecialState specialState) {
        this.specialState = specialState;
    }

    public SpecialState getSpecialState() {
        return this.specialState;
    }

    public boolean hasPiece() {
        return this.piece != null;
    }

    public boolean hasSpecialState() {
        return this.specialState != SpecialState.NONE;
    }

    public void removePiece() {
        this.piece = null;
    }

    public void removeSpecialState() {
        this.specialState = SpecialState.NONE;
    }
}
