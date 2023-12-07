package com.senetboom.game.backend;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.senetboom.game.SenetBoom;

public class Stick {

    private StickRoll[] stickRolls;
    private int stickValue;
    private int maxTime = 3;
    private int elapsedTime = 0;

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

        // create RollingsSticks actor and add to stage
        RollingSticks rollingSticks = new RollingSticks(stickValue);
        SenetBoom.stickStage.addActor(rollingSticks);
    }

    public static void update() {
    }


    class StickRoll {
        private final int stickRoll;

        public StickRoll() {
            this.stickRoll = MathUtils.random(0,1);
        }

        public int getStickRoll() {
            return this.stickRoll;
        }
    }

    class RollingSticks extends Actor{
        private final int stickValue;
        private final int maxTime = 3;
        private int elapsedTime = 0;

        private RollingSticks(int stickValue) {
            this.stickValue = stickValue;
        }

        // draw method

        // act method
    }

    public int getValue() {
        return this.stickValue;
    }
}
