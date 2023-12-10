package com.senetboom.game.backend;

import com.senetboom.game.SenetBoom;

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
        REBIRTH,
        SAFE
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

    public int getPosition() {
        return this.position;
    }

    @Override
    public boolean isMoveValid(int index, int stickRoll) {

        Tile[] board = Board.getBoard();

        int targetIndex = index + stickRoll;

        if(stickRoll == 0){
            System.out.println("Move invalid: Stick roll is 0, no movement possible.");
            return false; // you can do nothing with a 0 stick throw
        }

        if (targetIndex >= 30) {
            System.out.println("Move invalid: Target index exceeds board limits.");
            return false; // piece has to land on house of happiness
        }

        // targetPiece team Colour ----------------------

        if(board[targetIndex].hasPiece()) {
            if(board[targetIndex].getPiece().getColour() == board[index].getPiece().getColour()) {
                System.out.println("Move invalid: Target tile has a piece of the same color.");
                return false;
            }
        }

        // if there is no targetPiece ---------------------

        if(!(board[targetIndex].hasPiece()))
        {
            // if there is no blockade in between
            if(!(stickRoll == 4 || stickRoll == 6)) {
                // target has no piece on it
                return true;
            } else if (!isBlockadeConfiguration(board, index, stickRoll)) {
                System.out.println("Move invalid: Blockade configuration detected.");
                return false;
            }
        }

        // if the targetPiece exists ---------------------

        // check if the targetPiece is on a safe tile
        if(board[targetIndex].hasSpecialState()) {
            if(board[targetIndex].getSpecialState() == SpecialState.SAFE) {
                System.out.println("Move invalid: Target tile is a safe tile.");
                return false;
            }
        }

        if(stickRoll == 4 || stickRoll == 6) {
            // check for blockade
            // blockade checks for any 3 piece block inbetween the jump
            // issafeconfig checks if the targetpiece is safe from switches
            if (!isBlockadeConfiguration(board, index, stickRoll)) {
                System.out.println("Move invalid: Blockade configuration detected for stick roll 4 or 6.");
                return false;
            }
        }

        if (!isSafeConfiguration(board, index, stickRoll)) {
            System.out.println("Move invalid: Unsafe configuration detected for stick roll 3 and under.");
            return false;
        }

        return true;
    }


    @Override
    public boolean isSafeConfiguration(Tile[] board, int index, int stickRoll) {
        int targetIndex = index + stickRoll;

        // Ensure indices are within bounds
        if (targetIndex < 0 || targetIndex >= board.length) {
            System.out.println("Safe configuration check: Target index out of bounds.");
            return false;
        }

        Piece.Color colorTarget = board[targetIndex].getPiece().getColour();

        // Check adjacent tiles if they exist
        boolean checkBefore = targetIndex - 1 >= 0 && board[targetIndex - 1].hasPiece();
        boolean checkAfter = targetIndex + 1 < board.length && board[targetIndex + 1].hasPiece();

        if (checkBefore) {
            Piece.Color colorBefore = board[targetIndex - 1].getPiece().getColour();
            if (colorTarget == colorBefore) {
                System.out.println("Safe configuration check: Same color piece found before target.");
                return false;
            }
        }
        if (checkAfter) {
            Piece.Color colorAfter = board[targetIndex + 1].getPiece().getColour();
            if (colorTarget == colorAfter) {
                System.out.println("Safe configuration check: Same color piece found after target.");
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isBlockadeConfiguration(Tile[] board, int index, int stickRoll) {
        int targetIndex = index + stickRoll;

        // Ensure targetIndex is within bounds
        if (targetIndex < 0 || targetIndex >= board.length) {
            System.out.println("Blockade configuration check: Target index out of bounds.");
            return false;
        }

        // If the target tile is empty, assume enemy color as the opposite of the current piece
        Piece.Color colorPlayer = board[index].getPiece().getColour();
        Piece.Color colorTarget = (colorPlayer == Piece.Color.BLACK) ? Piece.Color.WHITE : Piece.Color.BLACK;

        // Check for a blockade configuration in the path
        if (stickRoll == 4 || stickRoll == 6) {
            if (checkBlockade(board, index, stickRoll, colorTarget)) {
                System.out.println("Blockade configuration found for stick roll " + stickRoll + ".");
                return true; // Blockade found, move is invalid
            }
        }

        System.out.println("No blockade configuration found for stick roll " + stickRoll + ".");
        return false; // No blockade found, move is valid
    }



    private boolean checkBlockade(Tile[] board, int start, int stickRoll, Piece.Color colorTarget) {
        int consecutiveCount = 0;

        // Check the tiles in the path (excluding the start and target positions)
        for (int i = 1; i < stickRoll; i++) {
            int index = start + i;
            if (index < 0 || index >= board.length) {
                break; // Exit if out of bounds
            }

            if (board[index].hasPiece() && board[index].getPiece().getColour() == colorTarget) {
                consecutiveCount++;
                if (consecutiveCount == 3) {
                    return true; // Found three consecutive enemy pieces
                }
            } else {
                consecutiveCount = 0; // Reset count if sequence is broken
            }
        }
        return false; // No sequence of three consecutive enemy pieces found
    }




    @Override
    public void movePiece(int newIndex) {
        int index = position; // starter position
        Tile[] board = Board.getBoard();
        Piece piece = board[index].getPiece();

        // rebirth case:
        if(board[newIndex].getPiece().hasProtection()) {
            // if the piece has rebirth protection, remove it
            board[newIndex].getPiece().switchProtection();
            return;
        }


        // First, remove the piece from its current tile
        board[index].removePiece();

        // Then, if the new tile already has a piece, swap them
        if (board[newIndex].hasPiece()) {
            Piece piece2 = board[newIndex].getPiece();
            board[index].setPiece(piece2); // move the existing piece to the old position of the moving piece
            board[newIndex].removePiece(); // remove the old piece from the new position
        }

        // Now, move the piece to the new tile
        board[newIndex].setPiece(piece);

        // check for states of the tile it is on now
        if (board[newIndex].hasSpecialState()) {
            switch (board[newIndex].getSpecialState()) {
                case HAPPY:
                    // remove piece from the board, it has reached the end
                    board[newIndex].removePiece();
                    break;
                case WATER:
                    // special handling for WATER tile
                    if (board[14].hasPiece()) {
                        // if there is already a piece on tile 14, move to the earliest available position
                        for (int i = 13; i >= 0; i--) {
                            if (!board[i].hasPiece()) {
                                board[i].setPiece(piece); // move to the earliest free tile
                                break;
                            }
                        }
                    } else {
                        // if tile 14 is free, move the piece there and give it protection
                        board[14].setPiece(piece);
                        piece.switchProtection(); // give protection to the piece
                    }
                    break;
                case REBIRTH:
                    // add rebirth protection
                    piece.switchProtection();
                    break;
                case SAFE:
                    // do nothing for SAFE state
                    break;
            }
        }
    }


}
