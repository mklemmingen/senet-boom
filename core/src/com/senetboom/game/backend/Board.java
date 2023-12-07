package com.senetboom.game.backend;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.senetboom.game.SenetBoom;

import static com.senetboom.game.SenetBoom.*;

public class Board {
    /*
    2D Array of Pieces of the senet Board
     */
    private static Tile[] board = new Tile[30];
    public static boolean reverse = false;

    public Board() {
    }

    public static void initializeBoard() {
        // populate the board with pieces, depending on the senet rules
        for(int i=0; i<30; i++) {
            board[i] = new Tile(i);
        }
        // the first 10 pieces are black, white, black, white, black, white ...
        for(int i=0; i<10; i++) {
            if(i%2 == 0) {
                board[i].setPiece(new Piece(Piece.Color.BLACK));
            } else {
                board[i].setPiece(new Piece(Piece.Color.WHITE));
            }
        }
        // the special states are:
        for (int i = 0; i < board.length; i++) {
            if (i == 26) {
                // The Waters of Chaos, throws back to tile 15 (index 14) or earlier
                board[i].setSpecialState(Tile.SpecialState.WATER);
            } else if (i == 25 || i == 27 || i == 28) {
                // Safe tiles
                board[i].setSpecialState(Tile.SpecialState.SAFE);
            } else if (i == 14) {
                // The House of Rebirth, if landed upon, the piece is
                // put under protection and cannot be switched with for one time
                board[i].setSpecialState(Tile.SpecialState.REBIRTH);
            } else if (i == 29) {
                // The House of Happiness (final tile), if landed upon, the piece is
                // removed from the board
                board[i].setSpecialState(Tile.SpecialState.HAPPY);
            } else {
                // all other tiles are normal
                board[i].setSpecialState(Tile.SpecialState.NONE);
            }
        }
    }

    public static void setAllowedTile(int index) {
        possibleMove = index;
    }

    public static void setBoard(Tile[] saveBoard) {
        board = saveBoard;
    }

    public static Tile[] getBoard() {
        return board;
    }
}
