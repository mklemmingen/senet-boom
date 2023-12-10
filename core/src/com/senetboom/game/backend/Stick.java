package com.senetboom.game.backend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.senetboom.game.SenetBoom;

import static com.senetboom.game.SenetBoom.*;

public class Stick {

    private StickRoll[] stickRolls;
    private int stickValue;
    private static float maxTime = 1.5f;
    private static float elapsedTime = 0;

    // every time a new Stick is created, we need to create a new RollingSticks actor and add it to the stage
    // also save the value of the stick in the stickValue variable

    public Stick() {
        // rolling Sticks
        this.stickRolls = new StickRoll[4];
        this.stickValue = 0;
        for (int i = 0; i < 4; i++) {
            this.stickRolls[i] = new StickRoll();
            this.stickValue += this.stickRolls[i].getStickRoll();
        }
        if(stickValue == 0){ // 4 blacks equal value 6
            stickValue = 6;
        }

        // create RollingsSticks actor and add to stage
        RollingSticks rollingSticks = new RollingSticks();
        SenetBoom.stickStage.addActor(rollingSticks);

        // set sticksTumbling to true
        SenetBoom.sticksTumbling = true;
    }

    public static void update(float delta) {
        // add to elapsedTime till maxTime reached, then set sticksTumbling to false
        elapsedTime += delta;
        if (elapsedTime >= maxTime) {
            SenetBoom.sticksTumbling = false;
            elapsedTime = 0;
            stickStage.clear();
        }
    }


    static class StickRoll {
        private final int stickRoll;

        public StickRoll() {
            // white side up is 1, black side up is 0
            this.stickRoll = MathUtils.random(0,1);
        }

        public int getStickRoll() {
            return this.stickRoll;
        }
    }

    class RollingSticks extends Actor{
        private final float maxTime = 1.5f;
        private float elapsedTime = 0;
        private static final int FRAME_COLS = 16; // Number of columns in animations/explosions.png sprite sheet
        private static final int FRAME_ROWS = 1; // Number of rows
        private final Animation<TextureRegion> throwAnimation;
        private static final float SCALE_FACTOR = 1f;  // scaling factor

        private RollingSticks() {
            // loading the sprite sheet as a Texture
            Texture throwSheet = new Texture(Gdx.files.internal("animations/stickThrow.png"));
            // splitting the Sprite Sheet into individual frames
            TextureRegion[][] tmp = TextureRegion.split(throwSheet,
                    throwSheet.getWidth() / FRAME_COLS,
                    throwSheet.getHeight() / FRAME_ROWS);

            // create the Animation objets by collecting the Frames from the Sprite Sheet
            TextureRegion[] throwFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
            int index = 0;
            for (int i = 0; i < FRAME_ROWS; i++) {
                for (int j = 0; j < FRAME_COLS; j++) {
                    throwFrames[index++] = tmp[i][j];
                }
            }
            throwAnimation = new Animation<TextureRegion>(maxTime/16, throwFrames);
        }

        // draw method
        @Override
        public void draw(Batch batch, float parentAlpha) {
            TextureRegion currentFrame = throwAnimation.getKeyFrame(elapsedTime, true);

            // Calculate the scaled width and height
            float scaledWidth = currentFrame.getRegionWidth() * SCALE_FACTOR;
            float scaledHeight = currentFrame.getRegionHeight() * SCALE_FACTOR;

            float x1 = tileSize;
            float y1 = tileSize*2;

            batch.draw(currentFrame, x1, y1, scaledWidth, scaledHeight); // Draw the current frame at the specified position
        }


        // act method
        @Override
        public void act(float delta) {
            /*
             * this method is called every frame to update the Actor
             */
            super.act(delta);
            elapsedTime += delta;
            if (elapsedTime > maxTime) {
                sticksTumbling = false;
                remove(); // This will remove the actor from the stage

                if (stickValue == 1 || stickValue == 4 || stickValue == 0) {
                    // add extra Turn Actor
                    addExtraTurnActor();
                }
            }
        }
    } // end of RollingSticks class

    public int getValue() {
        // complex method that returns the value of the stick
        if(stickValue == 1){ // if one white side up, 3 black sides up
            // extraTurn to true
            SenetBoom.extraTurn = true;
            return stickValue;
        } else if(stickValue == 2){ // if two white sides up, 2 black sides up
            return stickValue;
        } else if(stickValue == 3){ // if three white sides up, 1 black side up
            return stickValue;
        } else if(stickValue == 4){ // if four white sides up, 0 black sides up
            // extraTurn to true
            SenetBoom.extraTurn = true;
            return stickValue;
        } else if(stickValue == 0){// if zero white sides up, 4 black sides up
            // extraTurn to false
            SenetBoom.extraTurn = true;
            return 6;
        } else { // if any other value, return 0
            return 0;
        }
    }
}

