package com.senetboom.game;

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

        if (targetIndex >= 30) {
            return false; // piece has to land on house of happiness
        }

        // for rebirth protection and loss if piece can hit the pawn with protection
        if(board[targetIndex].getPiece().hasProtection()) {
            board[targetIndex].getPiece().switchProtection(); // lose protection
            SenetBoom.renderBoard(); // target piece lost protection
            return false; // piece can only land on a piece with rebirth protection
        }

        boolean isSafeConfiguration = isSafeConfiguration(board, index, stickRoll);
        // Check for 2-in-a-row safe configuration

        boolean isBlockadeConfiguration = isBlockadeConfiguration(board, index, stickRoll);
        // Check for 3-in-a-row blockade

        return isSafeConfiguration && isBlockadeConfiguration;
    }


    @Override
    public boolean isSafeConfiguration(Tile[] board, int index, int stickRoll) {
        // Check for adjacent tiles with the same color pieces
        if(board[index+stickRoll].hasPiece()) {
            Piece.Color color = board[index].getPiece().getColour();
            // safe configuration found if true, false otherwise

            Piece piece2 = board[index+stickRoll].getPiece(); // target piece
            Piece piece1 = board[index+stickRoll-1].getPiece(); // current piece
            Piece piece3 = board[index+stickRoll+1].getPiece(); // piece after target piece

            Piece.Color colorTarget = piece2.getColour();
            if(colorTarget != color) {
                if (piece1 != null) {
                    return piece1.getColour() != colorTarget; // blockade configuration
                }
                if (piece3 != null){
                    return piece3.getColour() != colorTarget; // blockade configuration
                }
            } else {
                return false; // target same color as current piece
            }
        }
        return true; // if no piece at position or piece different color and no blockade
    }

    @Override
    public boolean isBlockadeConfiguration(Tile[] board, int index, int stickRoll) {
        // Check for 3 consecutive tiles with the same color pieces
        if(stickRoll >= 4) {
            if(board[index + 1].hasPiece()
                && board[index + 2].hasPiece()
                && board[index + 3].hasPiece())
            {
                Piece.Color color = board[index].getPiece().getColour();
                // blockade configuration found if true, false otherwise
                return board[index + 1].getPiece().getColour() != color
                       && board[index + 2].getPiece().getColour() != color
                       && board[index + 3].getPiece().getColour() != color;
            }
        }
        return true;
    }

    @Override
    public void movePiece(int newIndex) {
        int index = position; // starter position
        // switches two pieces on the board, or a piece to an empty position
        Tile[] board = Board.getBoard();
        Piece piece = board[index].getPiece();

        if(board[newIndex].hasPiece()){
            Piece piece2 = board[newIndex].getPiece();
            board[newIndex].setPiece(piece);
            board[index].setPiece(piece2);
        } else {
            board[newIndex].setPiece(piece);
        }

        // check for states of the tile it is on now
        if(board[newIndex].hasSpecialState()){
            // switch between all relevant states
            switch (board[newIndex].getSpecialState()){
                case HAPPY:
                    // remove piece from the board, it has reached the end
                    board[newIndex].removePiece();
                    break;
                case WATER:
                    // switch piece with the piece on the 14 index (15 tile)

                    // if there already is someone there, go to the earliest possible position before the 14 index
                    if(board[14].hasPiece()){
                        for(int i = 13; i >= 0; i--){
                            if(!(board[i].hasPiece())){
                                // recursive since lazy
                                board[newIndex].movePiece(i);
                                break;
                            }
                        }
                    } else {
                        // recursive approach to not have to write Rebirth
                        board[newIndex].movePiece(14);
                    }
                    break;
                case REBIRTH:
                    // add rebirth protection if it has landed on index 14 (15 tile)
                    board[newIndex].getPiece().switchProtection();
                    break;
                case SAFE:
                    // do nothing, it doesn't matter
                    break;
            }
        }
    }


}
