package com.senetboom.game;

import com.senetboom.game.Piece;

public class Board {
    /*
    2D Array of Pieces of the senet Board
     */
    private Tile[][] board;

    public Board() {
        this.board = new Tile[3][10];
        // populate the board with pieces, depending on the senet rules
        // 5 black pieces
        for (int i = 0; i < 5; i++) {
            this.board[0][i] = new Tile();
            board[0][i].setPiece(new Piece(Piece.Color.BLACK));
        }
        // 5 white pieces
        for (int i = 0; i < 5; i++) {
            this.board[2][i] = new Tile();
            board[0][i].setPiece(new Piece(Piece.Color.WHITE));
        }
        // set the tiles with the special abilities
        // 3 houses
        for (int i = 0; i < 3; i++) {
            this.board[1][i] = new Tile();
            board[1][i].setSpecialState(Tile.SpecialState.HOUSE);
        }
        // 2 water tiles
        for (int i = 0; i < 2; i++) {
            this.board[1][i + 3] = new Tile();
            board[1][i + 3].setSpecialState(Tile.SpecialState.WATER);
        }
        // 2 safe tiles
        for (int i = 0; i < 2; i++) {
            this.board[1][i + 5] = new Tile();
            board[1][i + 5].setSpecialState(Tile.SpecialState.SAFE);
        }
        // 2 water tiles
        for (int i = 0; i < 2; i++) {
            this.board[1][i + 7] = new Tile();
            board[1][i + 7].setSpecialState(Tile.SpecialState.WATER);
        }
        // 3 houses
        for (int i = 0; i < 3; i++) {
            this.board[1][i + 9] = new Tile();
            board[1][i + 9].setSpecialState(Tile.SpecialState.HOUSE);
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
    public Tile[][] getBoard() {
        return this.board;
    }
}
