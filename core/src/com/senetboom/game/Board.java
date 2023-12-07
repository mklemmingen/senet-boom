package com.senetboom.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

import java.util.ArrayList;

import static com.senetboom.game.SenetBoom.*;

public class Board {
    /*
    2D Array of Pieces of the senet Board
     */
    private static Tile[] board = new Tile[30];
    private static boolean reverse = false;

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
            } else if (i == 14 || i == 25 || i == 27 || i == 28) {
                // Safe tiles
                board[i].setSpecialState(Tile.SpecialState.SAFE);
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

    public static Stage drawBoard() {
        Stage stage = new Stage();
        // create a root table
        Table root = new Table();
        Table pawnRoot = new Table();
        // iterate through the board, at each tile
        final Tile[] board = getBoard();
        for(final Tile tile: board) {
            final Stack stack = new Stack();
            final Stack pawnStack = new Stack();

            // add an empty png to the stack, so that it is represented even if no piece or special state is present
            Image empty = new Image(emptyTexture);
            empty.setSize(tileSize, tileSize);
            empty.setZIndex(0);
            pawnStack.addActor(empty);

            // add a png of a tile texture to the stack
            Image tileTexture = new Image(SenetBoom.tileTexture);
            tileTexture.setSize(tileSize, tileSize);
            tileTexture.setZIndex(0);
            stack.addActor(tileTexture);

            // if the tile has a special state, draw the special state
            if(tile.hasSpecialState()) {
                Image specialState;
                switch(tile.getSpecialState()) {
                    case HAPPY:
                        // draw a house to the stack
                        specialState = new Image(happy);
                        break;
                    case WATER:
                        // draw water to the stack
                        specialState = new Image(water);
                        break;
                    case SAFE:
                        // draw a safe tile to the stack
                        specialState = new Image(safe);
                        break;
                    case REBIRTH:
                        // draw a rebirth tile to the stack
                        specialState = new Image(rebirth);
                        break;
                    default:
                        // draw an empty tile to the stack
                        specialState = new Image(emptyTexture);
                        break;
                }
                specialState.setSize(tileSize, tileSize);
                specialState.setZIndex(1);
                stack.addActor(specialState);
            }

            // if the tile has a piece, draw the piece
            if(tile.hasPiece()) {
                if(emptyVariable != tile.getPosition()) {
                    // if tile not the empty currently bot move pawn position
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
                    pawnStack.addActor(piece);
                    break;
                }
            }

            // check if tile has protection
            if(tile.hasPiece() && tile.getPiece().hasProtection()) {
                // draw a protection to the stack
                Image protection = new Image(rebirthProtection);
                protection.setSize(tileSize, tileSize);
                protection.setZIndex(3);
                pawnStack.addActor(protection);
            }

            if(tile.hasPiece() && tile.getPiece().getColour() == SenetBoom.getTurn()){
                // drag and drop listeners
                pawnStack.addListener(new DragListener() {
                       @Override
                       public void dragStart(InputEvent event, float x, float y, int pointer) {
                           // Code runs when dragging starts:
                           System.out.println("Started dragging the Pawn!\n");

                           // Get the team color of the current tile
                           Tile[] gameBoard = Board.getBoard();

                           int tile = SenetBoom.calculateTilebyPx((int) x, (int) y);

                           Piece.Color teamColor = gameBoard[tile].getPiece().getColour();
                           // If it's not the current team's turn, cancel the drag and return
                           if (teamColor != SenetBoom.getTurn()) {
                               event.cancel();
                               System.out.println("It's not your turn!\n");
                               SenetBoom.renderBoard();
                               return;
                           }

                           if(board[tile].isMoveValid(board[tile].getPosition(), currentStickValue)){
                               setAllowedTile(board[tile].getPosition()+currentStickValue);
                           }
                       }

                       @Override
                       public void drag(InputEvent event, float x, float y, int pointer) {
                           // Code here will run during the dragging
                           stack.moveBy(x - stack.getWidth() / 2, y - stack.getHeight() / 2);

                           if(stack.getRotation() == 20){
                               Board.reverse = true;
                           }
                           if(stack.getRotation() == -20){
                               Board.reverse = false;
                           }

                           if(!Board.reverse) {
                               stack.setRotation((float) (stack.getRotation() + 0.01));
                           } else {
                               stack.setRotation((float) (stack.getRotation() - 0.01));
                           }

                       }

                       @Override
                       public void dragStop(InputEvent event, float x, float y, int pointer) {
                           // Code here will run when the player lets go of the actor

                           // Get the position of the tileWidget relative to the parent actor (the gameBoard)
                           Vector2 localCoords = new Vector2(x, y);
                           // Convert the position to stage (screen) coordinates
                           Vector2 screenCoords = stack.localToStageCoordinates(localCoords);

                           System.out.println("\n Drag stopped at screen position: " + screenCoords.x + ", "
                                   + screenCoords.y + "\n");

                           int endTile = SenetBoom.calculateTilebyPx((int) screenCoords.x, (int) screenCoords.y);

                           // for loop through validMoveTiles, at each tile we check for equality of currentCoord
                           // with the Coordinate
                           // in the ArrayList by using currentCoord.checkEqual(validMoveTiles[i]) and if true,
                           // we set the
                           // validMove Variable to true, call on the update method of the Board class and break
                           // the for loop
                           // then clear the Board.


                           if(endTile == possibleMove){
                               // Board.update with oldX, oldY, newX, newY
                               board[tile.getPosition()].movePiece(endTile);
                               SenetBoom.legitMove = true;
                           }


                           // and the possibleMove is cleared
                           possibleMove = -1; // for turning off the Overlay

                           // board is rendered new
                           SenetBoom.renderBoard();
                       }
                }); // end of listener creation
            }

            pawnStack.add(pawnStack);
            root.add(stack);
            if(tile.getPosition() == 9 || tile.getPosition() == 19) {
                pawnRoot.row();
                root.row();
            }
        }
        root.setZIndex(1);
        pawnRoot.setZIndex(2);
        stage.addActor(root);
        stage.addActor(pawnRoot);
        return stage;
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
