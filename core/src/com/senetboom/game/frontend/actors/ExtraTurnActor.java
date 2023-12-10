package com.senetboom.game.frontend.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import static com.senetboom.game.SenetBoom.*;

public class ExtraTurnActor extends Actor {
    /*
    class to hold an actor that gets displayed on screen for 2 seconds if the player got an extra Turn
     */

    // px coords
    private float X;
    private float Y;
    // elapsed time since addition to stage
    private float elapsed = 0;
    // this is the maximum duration that the bubble will be on the screen
    private static final float MAX_DURATION = 2f;
    // this is the stack of the bubble
    private final Stack stack;

    public ExtraTurnActor(){
        /*
        This function creates a Stack object that holds the speech bubble that says "Attack in Progress!"
         */
        this.stack = new Stack();
        stack.setSize(tileSize*4, tileSize*2);
        stack.addActor(new Image(extraTurnTexture));
        this.X = tileSize*8;
        this.Y = tileSize*8;
    }

    @Override
    public void act(float delta) {
        /*
         * this method is called every frame to update the Actor
         */
        super.act(delta);
        elapsed += delta;
        if (elapsed > MAX_DURATION) {
            remove(); // This will remove the actor from the stage
        }
    }

    // Override the draw method to add the stack at the correct position
    @Override
    public void draw(Batch batch, float parentAlpha) {
        /*
        This method is called every frame to draw the indicator
         */
        super.draw(batch, parentAlpha);
        stack.setPosition(X, Y);
        stack.draw(batch, parentAlpha);
    }
}
