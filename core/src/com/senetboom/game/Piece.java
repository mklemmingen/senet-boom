package com.senetboom.game;

public class Piece {
    public enum Color {
        BLACK,
        WHITE,
    }

    private final Color colour;
    private boolean rebirthProtection;

    public Piece(Color color) {
        this.colour = color;
        this.rebirthProtection = false;
    }

    public void checkMove(int currentIndex, int stickRoll) {
        // calculate if the move to currentIndex+stickRoll is valid
        Tile[] board = Board.getBoard();
        if(board[currentIndex].isMoveValid(currentIndex, stickRoll)){
            // if yes, add currentIndex+stickRoll to the arraylist of valid moves
            SenetBoom.possibleMoves.add(currentIndex+stickRoll);
            // if yes, put the targetIndex into the possibleMove variables of SenetBoom
            SenetBoom.possibleMove = currentIndex+stickRoll;
        }
        SenetBoom.renderBoard();
    }

    public Color getColour() {
        return this.colour;
    }

    public boolean hasProtection() {
        return this.rebirthProtection;
    }

    public void switchProtection() {
        this.rebirthProtection = !this.rebirthProtection;
    }
}
