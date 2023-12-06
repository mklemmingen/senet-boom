package com.senetboom.game;

import com.sonetboom.game.Piece;

public class Board {
    /*
    2D Array of Pieces of the senet Board
     */
    private Piece[][] board;

    public Board() {
        this.board = new Piece[3][10];
        // populate the board with pieces, depending on the senet rules
        // 5 black pieces
        for (int i = 0; i < 5; i++) {
            this.board[0][i] = new Piece(Piece.Color.BLACK);
        }
        // 5 white pieces
        for (int i = 0; i < 5; i++) {
            this.board[2][i] = new Piece(Piece.Color.WHITE);
        }
    }

    public void movePiece(int x, int y, int newX, int newY) {
        // move a piece from x, y to newX, newY
        // check if the move is valid
        // if it is, move the piece
            //  check if the move is to a special tile
            //  if it is, do the special situation
        // if it isn't, don't move the piece
    }
    public Piece[][] getBoard() {
        return this.board;
    }
}
