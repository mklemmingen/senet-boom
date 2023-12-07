package com.senetboom.game;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.senetboom.game.Piece;

import static com.senetboom.game.SenetBoom.*;

public class Board {
    /*
    2D Array of Pieces of the senet Board
     */
    private static Tile[] board = new Tile[30];

    public Board() {
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
            if (i == 25) { // The Waters of Chaos
                board[i].setSpecialState(Tile.SpecialState.WATER);
            } else if (i >= 26 && i <= 28) { // Safe tiles
                board[i].setSpecialState(Tile.SpecialState.SAFE);
            } else if (i == 29) { // The House of Happiness (final tile)
                board[i].setSpecialState(Tile.SpecialState.HOUSE);
            } else {
                board[i].setSpecialState(Tile.SpecialState.NONE);
            }
        }
    }

    public static Stage drawBoard() {
        // create a root table
        Table root = new Table();
        // iterate through the board, at each tile
        Tile[] board = getBoard();
        for(Tile tile: board) {
            Stack stack = new Stack();

            // add an empty png to the stack, so that it is represented even if no piece or special state is present
            Image empty = new Image(emptyTexture);
            empty.setSize(tileSize, tileSize);
            empty.setZIndex(0);
            stack.addActor(empty);

            // if the tile has a special state, draw the special state
            if(tile.hasSpecialState()) {
                Image specialState;
                if(tile.getSpecialState() == Tile.SpecialState.HOUSE) {
                    // draw a house to the stack
                    specialState = new Image(house);
                } else if(tile.getSpecialState() == Tile.SpecialState.WATER) {
                    // draw water to the stack
                    specialState = new Image(water);
                } else {
                    // draw a safe tile to the stack
                    specialState = new Image(safe);
                }
                specialState.setSize(tileSize, tileSize);
                specialState.setZIndex(1);
                stack.addActor(specialState);
            }

            boolean addDrag = false;
            // if the tile has a piece, draw the piece
            if(tile.hasPiece()) {
                // go through the arraylist of positions where no piece should be drawn (empty variables),
                // if the current tile is in the arraylist, don't draw a piece
                // if the current tile is not in the arraylist, draw a piece
                for(int emptyTiles: SenetBoom.emptyVariables) {
                    if(emptyTiles == tile.getPosition()) {
                        Image piece;
                        if(tile.getPiece().getColour() == Piece.Color.BLACK) {
                            // draw a black piece to the stack
                            piece = new Image(blackpiece);
                        } else {
                            // draw a white piece to the stack
                            piece = new Image(whitepiece);
                        }
                        piece.setSize(tileSize, tileSize);
                        piece.setZIndex(2);
                        stack.addActor(piece);
                        addDrag = true;
                        break;
                    }
                }
            }

            if(addDrag){
                // drag and drop listeners
                // TODO
            }

            root.add(stack);
            if(tile.getPosition() == 9 || tile.getPosition() == 19) {
                root.row();
            }
        }

        return null;
    }

    public void movePiece(int x, int y, int newX, int newY) {
        // move a piece from x, y to newX, newY
        // check if the move is valid
        // if it is, move the piece
            //  check if the move is to a special tile
            //  if it is, do the special situation
        // if it isn't, don't move the piece
    }

    public static void setBoard(Tile[] saveBoard) {
        board = saveBoard;
    }

    public static Tile[] getBoard() {
        return board;
    }
}
