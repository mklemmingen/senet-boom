package com.senetboom.game.frontend.image;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

/** Turns a {@link PixelDesert} landscape into a texture with crisp, unfiltered pixels. */
public final class PixelDesertTexture {

    private PixelDesertTexture() {
    }

    /** Generates a new desert for the given seed. Draw it at WIDTH*PIXEL x HEIGHT*PIXEL world units. */
    public static Texture create(long seed) {
        int[] px = PixelDesert.render(seed);
        Pixmap pixmap = new Pixmap(PixelDesert.WIDTH, PixelDesert.HEIGHT, Pixmap.Format.RGBA8888);
        for (int y = 0; y < PixelDesert.HEIGHT; y++) {
            for (int x = 0; x < PixelDesert.WIDTH; x++) {
                pixmap.drawPixel(x, y, px[y * PixelDesert.WIDTH + x]);
            }
        }
        Texture texture = new Texture(pixmap);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        pixmap.dispose();
        return texture;
    }
}
