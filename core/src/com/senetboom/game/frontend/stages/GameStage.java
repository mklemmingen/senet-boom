package com.senetboom.game.frontend.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.senetboom.game.SenetBoom;
import com.senetboom.game.backend.Board;
import com.senetboom.game.backend.Piece;
import com.senetboom.game.backend.Tile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

import static com.senetboom.game.SenetBoom.*;
import static com.senetboom.game.backend.Board.getBoard;
import static com.senetboom.game.backend.Board.setAllowedTile;

public class GameStage {

    public static Stage drawMap() {

        Stage stage = new Stage();

        // create a root table for the tiles
        Table root = new Table();
        root.setFillParent(true);

        // iterate through the board, at each tile
        final Tile[] board = getBoard();

        // first row
        for(int i=0; i<10; i++) {
            createMapStacks(board, root, i);
        }

        root.row();

        // second row
        for(int i=19; i>=10; i--) {
            createMapStacks(board, root, i);
        }

        root.row();

        // third row
        for(int i=20; i<30; i++) {
            createMapStacks(board, root, i);
        }

        stage.addActor(root);

        return stage;
    }

    public static Stage drawBoard() {

        inGame = true;

        Stage stage = new Stage();

        // create a root table for the pawns
        Table pawnRoot = new Table();
        pawnRoot.setFillParent(true);


        // iterate through the board, at each tile
        final Tile[] board = getBoard();

        // first row
        for(int i=0; i<10; i++) {
            createStacks(board, pawnRoot, i);
        }

        pawnRoot.row();

        // second row
        for(int i=19; i>=10; i--) {
            createStacks(board, pawnRoot, i);
        }

        pawnRoot.row();

        // third row
        for(int i=20; i<30; i++) {
            createStacks(board, pawnRoot, i);
        }

        stage.addActor(pawnRoot);

        // add a Table with a single EXIT and OPTION Button
        Table exitTable = new Table();

        // HELP BUTTON THAT SWITCHES THE BOOLEAN displayHelp
        TextButton helpButton = new TextButton("HELP", skin);
        helpButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SenetBoom.displayHelp = !SenetBoom.displayHelp;
            }
        });
        exitTable.add(helpButton).padBottom(tileSize/4);

        // in the same row, add a hint button
        TextButton hintButton = new TextButton("HINT", skin);
        hintButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SenetBoom.displayHint = !SenetBoom.displayHint;
                needRender = true;
            }
        });
        exitTable.add(hintButton).padBottom(tileSize/4).padLeft(tileSize/8);
        exitTable.row();

        // add a skipTurn button
        TextButton skipTurnButton = new TextButton("END TURN", skin);
        skipTurnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SenetBoom.skipTurn = true;
            }
        });
        exitTable.add(skipTurnButton).padBottom(tileSize/4);
        exitTable.row();

        // add the Options button
        TextButton optionsButton = new TextButton("OPTIONS", skin);
        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // TODO
            }
        });
        exitTable.add(optionsButton).padBottom(tileSize/4);
        exitTable.row();

        // add the exit button
        TextButton exitButton = new TextButton("EXIT", skin);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SenetBoom.createMenu();
            }
        });
        exitTable.add(exitButton).padBottom(tileSize/4);

        exitTable.setPosition(Gdx.graphics.getWidth()-tileSize*3, tileSize*1.5f);
        stage.addActor(exitTable);

        return stage;
    }

    public static void createStacks(final Tile[] board, Table pawnRoot, final int i){
        final Tile tile = board[i];

        // ----------------- Pawn Stack -----------------

        // for the stack above being the pawn on the tile
        final Stack pawnStack = new Stack();
        pawnStack.setSize(tileSize, tileSize);

        // EMPTY Texture
        Image empty = new Image(emptyTexture);
        empty.setSize(tileSize, tileSize);
        pawnStack.addActor(empty);

        // if the tile has a piece, draw the piece
        if(tile.hasPiece()) {
            if(!(emptyVariable == tile.getPosition())) {
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
                pawnStack.addActor(piece);
            }
        }

        // check if tile has protection
        if(tile.hasPiece() && tile.getPiece().hasProtection()) {
            // draw a protection to the stack
            Image protection = new Image(rebirthProtection);
            protection.setSize(tileSize, tileSize);
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

                    Piece.Color teamColor = gameBoard[i].getPiece().getColour();
                    // If it's not the current team's turn, cancel the drag and return
                    if (!(teamColor == SenetBoom.getTurn())) {
                        event.cancel();
                        System.out.println("It's not your turn!\n");
                        SenetBoom.renderBoard();
                        return;
                    }

                    System.out.println("Current Stick Value: " + currentStickValue + "\n");
                    System.out.println("Current Tile: " + board[i].getPosition() + "\n");
                    System.out.println("Current Team" + teamColor + "\n");
                    if(board[i].isMoveValid(board[i].getPosition(), currentStickValue)){
                        System.out.println("Move for this piece valid. Setting allowed tile.");
                        setAllowedTile(board[i].getPosition()+currentStickValue);
                    } else{
                        System.out.println("Move for this piece invalid");
                    }

                    /*
                    board[i].getPiece().checkMove(board[i].getPosition(), currentStickValue);
                    */

                    pawnStack.toFront(); // bring to the front

                    // If it's the current team's turn, continue with the drag
                }

                @Override
                public void drag(InputEvent event, float x, float y, int pointer) {

                    // Code here will run during the dragging
                    // move by the difference between the current position and the last position
                    pawnStack.moveBy(x - pawnStack.getWidth() / 2, y - pawnStack.getHeight() / 2);
                }

                @Override
                public void dragStop(InputEvent event, float x, float y, int pointer) {
                    // Code here will run when the player lets go of the actor

                    // Get the position of the tileWidget relative to the parent actor (the gameBoard)
                    Vector2 localCoords = new Vector2(x, y);
                    // Convert the position to stage (screen) coordinates
                    Vector2 screenCoords = pawnStack.localToStageCoordinates(localCoords);

                    System.out.println("\n Drag stopped at screen position: " + screenCoords.x + ", "
                            + screenCoords.y + "\n");

                    int endTile = SenetBoom.calculateTilebyPx((int) screenCoords.x, (int) screenCoords.y);
                    System.out.print("End Tile: " + endTile + "\n");

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
                        System.out.println("Move valid as in check of tile to possibleMove");
                    } else {
                        System.out.println("Move invalid as in check of tile to possibleMove");
                    }

                    // and the possibleMove is cleared
                    possibleMove = -1; // for turning off the Overlay

                    // board is rendered new
                    SenetBoom.renderBoard();
                }
            }); // end of listener creation
        }
        pawnRoot.add(pawnStack);
    }

    public static void createMapStacks(final Tile[] board, Table root, int i){
        final Tile tile = board[i];

        // ----------------- Tile Stack -----------------

        // for the stack below being the tile of the board
        final Stack stack = new Stack();

        stack.setSize(tileSize, tileSize);

        // add a png of a tile texture to the stack
        Image tileTexture = new Image(SenetBoom.tileTexture);
        tileTexture.setSize(tileSize, tileSize);
        stack.addActor(tileTexture);

        // check if tile in possibleMoves
        if(displayHint) {
            needRender = true;
            for (Integer move : possibleMoves) {
                if (move == i) {
                    // add a png of a tile texture to the stack
                    Image possibleMoveTexture = new Image(SenetBoom.possibleMoveTexture);
                    possibleMoveTexture.setSize(tileSize, tileSize);
                    stack.addActor(possibleMoveTexture);
                }
            }
        }

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
            stack.addActor(specialState);
        }
        root.add(stack);
    }
}
