package com.senetboom.game;

import com.badlogic.gdx.math.MathUtils;

public class Stick {
    private final int stick;

    public Stick() {
        this.stick = MathUtils.random(0,1);
    }

    public int getStick() {
        return this.stick;
    }
}
