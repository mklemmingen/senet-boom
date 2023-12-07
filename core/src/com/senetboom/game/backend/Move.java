package com.senetboom.game.backend;

public interface Move {

    // method for calculating if a move is valid
    // takes a index between 0 and 29
    // returns a boolean

    boolean isMoveValid(int index, int stickRoll);
    boolean isSafeConfiguration(Tile[] board, int index, int stickRoll);
    boolean isBlockadeConfiguration(Tile[] board, int index, int stickRoll);

    // method for actually moving a piece from A to B
    // takes a index between 0 and 29
    // returns void, renders the board again
    void movePiece(int newIndex);

}
