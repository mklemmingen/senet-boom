package com.senetboom.game;

public interface Move {

    // method for calculating if a move is valid
    // takes a index between 0 and 29
    // returns a boolean

    boolean isMoveValid(int index, int stickRoll);
    boolean isSafeConfiguration(int index, int stickRoll);
    boolean checkStates(int index, int stickRoll);
    boolean isBlockadeConfiguration(int index, int stickRoll);

}
