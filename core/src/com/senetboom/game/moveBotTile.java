package com.senetboom.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import static com.senetboom.game.SenetBoom.tileSize;

public class moveBotTile {
    /*
     * This class is used to simulate the dragging of a piece by the bot. It slowly moves an image
     * of the soldier along the white line
     */

    // properties for beginning and end coordinates
    public int start;
    public int end;
    private Coordinate startPx;
    private Coordinate endPx;

    // properties for elapsed time, boolean isMoving and current position
    private static float elapsedTime;
    private static float moveDuration;
    public static boolean isMoving;
    public boolean movingFinished;
    private Stack pieceStack;
    private Image pieceImage;

    public moveBotTile() {
        elapsedTime = 0;
        isMoving = false;
        movingFinished = false;
    }

    // method for starting the move
    public void startMove(int startX, int startY, int endX, int endY) {
        /*
         * Method to simulate a move (start of the Move)
         */

        // coordinates
        this.start = startX;
        this.end = endX;

        // rerender Board with new Empty Variables
        SenetBoom.renderBoard();

        // set the maximum duration fitting the length of the way that the soldier moves
        // per 50 pixel, add 0.5f to max duration
        // Begin: calculate Vector:
        startPx = SenetBoom.calculatePXbyTile(startX, startY);
        endPx = SenetBoom.calculatePXbyTile(endX, endY);

        Vector2 pointA = new Vector2(startPx.getX(), startPx.getY());
        Vector2 pointB = new Vector2(endPx.getX(), endPx.getY());
        Vector2 vectorAB = pointB.sub(pointA);

        float lengthVec = vectorAB.len();
        int timefactor = (int) lengthVec / 50;
        moveDuration = timefactor * 0.75f;


        Tile[] gameBoard = Board.getBoard();

        Tile startTile = gameBoard[start];
        if (startTile.getPiece().getColour() == Piece.Color.WHITE) {
            startTile.setPiece(null);
        } else {
            startTile.setPiece(null);
        }

        pieceStack = new Stack();

        pieceStack.setSize(tileSize, tileSize);
        pieceStack.setVisible(true);

        pieceImage.setSize(tileSize, tileSize);
        pieceImage.setVisible(true);

        // add pieceImage to the widget and fill it
        pieceStack.add(pieceImage);

        SenetBoom.botMovingStage.addActor(pieceStack);

        // setting boolean isMoving to true, since we started moving
        isMoving = true;
        // setting elapsedTime to zero, since time got set to zero
        elapsedTime = 0;
    }

    // method for updating the move
    public void update(float delta) {
        /*
         * updates the BotMove object every frame
         */

        if (isMoving) {
            elapsedTime += delta; // adding time to elapsed time variable

            if (elapsedTime < moveDuration) { // if the elapsed time has not reached the maximum duration

                // Interpolate position // could be option changed by user

                float progress = elapsedTime / moveDuration;

                int currentX = startPx.getX() + (int) ((endPx.getX() - startPx.getX()) * progress);
                int currentY = startPx.getY() + (int) ((endPx.getY() - startPx.getY()) * progress);

                System.out.println("Moving, currently: " + currentX + " " + currentY);

                // call the renderAt method to render the image at the current position
                renderAt(currentX, currentY);
            } else {
                // Move completed
                movingFinished = true;
                isMoving = false;

                // resets Empty Variables
                SenetBoom.emptyVariables = new int[0];
            }
        }
    }

    private void renderAt(int currentX, int currentY) {
        /*
         * This method is used to render the image at the current position
         */

        currentX -= (int) (tileSize/2);
        currentY -= (int) (tileSize/2);

        pieceStack.setPosition(currentX, currentY);
    }

    public boolean getIsMoving() {
        return isMoving;
    }

}
