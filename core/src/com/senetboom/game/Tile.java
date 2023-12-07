package com.senetboom.game;

import java.util.Optional;


public class Tile implements Move {
    /*
    Tiles of the board. The hold a piece or not.
                        They hold a special ability or not.
     */
    private Piece piece;
    private SpecialState specialState;
    private final int position;

    public enum SpecialState {
        NONE,
        HAPPY,
        WATER,
        REBIRTH
    }

    public Tile(int position) {
        this.position = position;
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

    public int getPosition() {
        return this.position;
    }

    @Override
    public boolean isMoveValid(int index, int stickRoll) {

        Tile[] board = Board.getBoard();

        int targetIndex = index + stickRoll;

        if (targetIndex < 0 || targetIndex >= board.length) {
            return false;
        }

        boolean isSafeConfiguration = isSafeConfiguration(index, stickRoll);
        // Check for 2-in-a-row safe configuration

        boolean isBlockadeConfiguration = isBlockadeConfiguration(index, stickRoll);
        // Check for 3-in-a-row blockade

        boolean checkStates = checkStates(index, stickRoll);
        // SpecialState rules apply

        return isSafeConfiguration && isBlockadeConfiguration && checkStates;
    }


    @Override
    public boolean isSafeConfiguration(int index, int stickRoll) {
        // Check for adjacent tiles with the same color pieces
        // Adjust logic based on your game's interpretation of safe spots
        return false; // Placeholder
    }

    @Override
    public boolean checkStates(int index, int stickRoll) {
        // Assuming stickRoll is the number of steps (1 to 4)
        int targetIndex = index + stickRoll;
        Tile[] board = Board.getBoard();

        // Check if target index is outside the board (successful exit)
        if (targetIndex >= board.length) {
            return true; // Piece can exit the board
        }

        SpecialState targetTile = board[targetIndex].getSpecialState();

        switch (targetTile) {
            case WATER:
                // The Waters of Chaos can always be landed on
                return true;
            case HAPPY:
                // The House of Happiness can always be landed on
                return true;
            case REBIRTH:
                // Rebirth tiles can always be landed on
                return true;
            default:
                // No special rules for this tile, TILE NONE
                return true;
        }
    }

    @Override
    public boolean isBlockadeConfiguration(int index, int stickRoll) {
        // Check for 3 consecutive tiles with the same color pieces
        // Adjust logic based on your game's interpretation of blockades
        return false; // Placeholder
    }


}
