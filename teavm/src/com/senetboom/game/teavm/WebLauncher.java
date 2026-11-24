package com.senetboom.game.teavm;

import com.github.xpenatan.gdx.backends.teavm.TeaApplication;
import com.github.xpenatan.gdx.backends.teavm.TeaApplicationConfiguration;
import com.senetboom.game.SenetBoom;

/**
 * Browser entry point. width/height = 0 makes the canvas fill the page (or the itch.io iframe)
 * and follow its size; the game's FitViewport then scales the 1536x896 world to fit.
 */
public class WebLauncher {
    public static void main(String[] args) {
        TeaApplicationConfiguration config = new TeaApplicationConfiguration("canvas");
        config.width = 0;
        config.height = 0;
        new TeaApplication(new SenetBoom(), config);
    }
}
