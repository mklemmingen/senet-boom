package com.senetboom.game.frontend.image;

import java.util.Random;

/**
 * Generates the desert background as mathematical pixel art.
 *
 * <p>Everything is computed from a seed: a dithered sky gradient, a sun with a dithered halo,
 * small pixel clouds, pyramids on the horizon and layered dunes whose height is a sum of sine
 * waves. Dune faces are lit or shaded by their slope relative to the sun. A new seed at every
 * start gives a new landscape; the pixel size stays fixed.
 *
 * <p>This class is plain Java (no libGDX) so it can be tested and previewed on its own;
 * {@link PixelDesertTexture} turns the result into a texture.
 */
public final class PixelDesert {

    /** Size of one art pixel in world units. */
    public static final int PIXEL = 8;
    /** Art resolution: 1536x896 world units / 8. */
    public static final int WIDTH = 192;
    public static final int HEIGHT = 112;

    // 4x4 Bayer matrix for ordered dithering (values 0..15).
    private static final int[][] BAYER = {
            {0, 8, 2, 10},
            {12, 4, 14, 6},
            {3, 11, 1, 9},
            {15, 7, 13, 5}};

    // Sky, top to horizon.
    private static final int[] SKY = {0x1F5A9EFF, 0x2C74BEFF, 0x3F8ED3FF, 0x63AEE0FF, 0x97CFE8FF, 0xCBE7EAFF};
    private static final int SUN_CORE = 0xFFF8D6FF;
    private static final int SUN_RIM = 0xFFE58CFF;
    private static final int SUN_HALO = 0xFFF2B8FF;
    private static final int CLOUD_LIGHT = 0xFFFBEFFF;
    private static final int CLOUD_SHADE = 0xEADBB8FF;

    // Dune layers, back to front: {crest, lit, shade, deep shade}.
    private static final int[][] DUNES = {
            {0xF1DDB0FF, 0xE3C99AFF, 0xC9AE8AFF, 0xB79D82FF},
            {0xF6D88EFF, 0xE9BF6CFF, 0xC9945AFF, 0xA8784FFF},
            {0xFAD585FF, 0xEDB456FF, 0xC27F3FFF, 0x99603AFF},
            {0xFFD47AFF, 0xF0A845FF, 0xB86A30FF, 0x7E4430FF}};
    private static final int PYRAMID_LIT = 0xEBC98CFF;
    private static final int PYRAMID_SHADE = 0xB99067FF;
    private static final int PYRAMID_EDGE = 0xF7E2B5FF;

    private PixelDesert() {
    }

    /** Renders a landscape for the given seed. Returns RGBA8888 colours, row by row from the top. */
    public static int[] render(long seed) {
        Random rnd = new Random(seed);
        int[] px = new int[WIDTH * HEIGHT];

        int horizon = 58 + rnd.nextInt(6);

        // --- sky: vertical gradient, dithered between bands ---
        for (int y = 0; y < HEIGHT; y++) {
            float t = Math.min(1f, y / (float) horizon) * (SKY.length - 1);
            int band = Math.min(SKY.length - 2, (int) t);
            float frac = t - band;
            float level = frac < 0.55f ? 0f : (frac - 0.55f) / 0.45f;
            for (int x = 0; x < WIDTH; x++) {
                px[y * WIDTH + x] = dither(x, y, level) ? SKY[band + 1] : SKY[band];
            }
        }

        // --- sun ---
        boolean sunRight = rnd.nextBoolean();
        float sunX = WIDTH * (sunRight ? 0.62f + rnd.nextFloat() * 0.22f : 0.16f + rnd.nextFloat() * 0.22f);
        float sunY = HEIGHT * (0.13f + rnd.nextFloat() * 0.1f);
        float sunR = 8 + rnd.nextInt(4);
        for (int y = 0; y < horizon; y++) {
            for (int x = 0; x < WIDTH; x++) {
                float d = dist(x, y, sunX, sunY);
                if (d <= sunR - 1.5f) {
                    px[y * WIDTH + x] = SUN_CORE;
                } else if (d <= sunR) {
                    px[y * WIDTH + x] = SUN_RIM;
                } else if (d <= sunR * 2.2f) {
                    float density = 1f - (d - sunR) / (sunR * 1.2f);
                    if (dither(x, y, density * 0.55f)) px[y * WIDTH + x] = SUN_HALO;
                }
            }
        }

        // --- clouds: unions of circles with a flat base ---
        int clouds = 2 + rnd.nextInt(3);
        for (int c = 0; c < clouds; c++) {
            float cx = rnd.nextFloat() * WIDTH;
            float cy = 10 + rnd.nextFloat() * (horizon * 0.45f);
            if (Math.abs(cx - sunX) < sunR * 2.5f && Math.abs(cy - sunY) < sunR * 2.5f) continue;
            int puffs = 3 + rnd.nextInt(3);
            float[][] p = new float[puffs][3];
            for (int i = 0; i < puffs; i++) {
                float edge = 1f - Math.abs(i - (puffs - 1) / 2f) / puffs;   // bigger puffs in the middle
                p[i][2] = 2.5f + edge * 3.5f + rnd.nextFloat() * 1.2f;
                p[i][0] = cx + (i - (puffs - 1) / 2f) * 3.2f;
                p[i][1] = cy + 2 - p[i][2] * 0.75f;
            }
            float base = cy + 2;
            for (int y = (int) (cy - 10); y <= base; y++) {
                for (int x = (int) (cx - 24); x <= cx + 24; x++) {
                    if (x < 0 || y < 0 || x >= WIDTH || y >= horizon) continue;
                    for (float[] q : p) {
                        float d = dist(x, y, q[0], q[1]);
                        if (d <= q[2] && y <= base) {
                            boolean shade = y >= base - 1 || (sunRight ? x < q[0] - q[2] * 0.4f : x > q[0] + q[2] * 0.4f) && d > q[2] - 1.5f;
                            px[y * WIDTH + x] = shade ? CLOUD_SHADE : CLOUD_LIGHT;
                            break;
                        }
                    }
                }
            }
        }

        // --- far dune line (haze) behind the pyramids ---
        float[] far = waves(rnd, 3, 0.05f, 2f);
        fillDune(px, far, horizon, DUNES[0], sunRight, rnd, 0f);

        // --- pyramids on the horizon ---
        int pyramids = 1 + rnd.nextInt(3);
        float px0 = WIDTH * (0.1f + rnd.nextFloat() * 0.35f);
        for (int i = 0; i < pyramids; i++) {
            int half = 7 + rnd.nextInt(9) - i * 2;
            if (half < 4) break;
            int baseY = horizon + 2 + i;
            int apexX = (int) px0;
            for (int y = baseY - half; y <= baseY; y++) {
                int w = y - (baseY - half);
                for (int x = apexX - w; x <= apexX + w; x++) {
                    if (x < 0 || x >= WIDTH || y < 0) continue;
                    boolean litSide = sunRight ? x >= apexX : x <= apexX;
                    int col = litSide ? PYRAMID_LIT : PYRAMID_SHADE;
                    if (x == apexX) col = PYRAMID_EDGE;
                    // stepped courses: every third row slightly darker on the shaded side
                    if (!litSide && (y - (baseY - half)) % 3 == 2) col = DUNES[0][3];
                    px[y * WIDTH + x] = col;
                }
            }
            px0 += (sunRight ? -1 : 1) * (half * 2 + 3 + rnd.nextInt(10));
            if (px0 < 8 || px0 > WIDTH - 8) break;
        }

        // --- dune layers, back to front ---
        int[] bases = {horizon + 6, horizon + 17, horizon + 31, horizon + 45};
        float[] amps = {3.5f, 6f, 9f, 12f};
        float[] freqs = {0.045f, 0.035f, 0.028f, 0.022f};
        for (int layer = 0; layer < 4; layer++) {
            float[] h = waves(rnd, 3, freqs[layer], amps[layer]);
            fillDune(px, h, bases[layer], DUNES[layer], sunRight, rnd, layer >= 2 ? 0.035f * layer : 0f);
        }
        return px;
    }

    /** Height profile: sum of sine waves with random phases, one value per column. */
    private static float[] waves(Random rnd, int count, float freq, float amp) {
        float[] f = new float[count], ph = new float[count], a = new float[count];
        for (int i = 0; i < count; i++) {
            f[i] = freq * (1 + i * (0.8f + rnd.nextFloat() * 0.9f));
            ph[i] = rnd.nextFloat() * 6.2832f;
            a[i] = amp / (1 + i * 1.6f);
        }
        float[] h = new float[WIDTH];
        for (int x = 0; x < WIDTH; x++) {
            float v = 0;
            for (int i = 0; i < count; i++) v += a[i] * (float) Math.sin(f[i] * x + ph[i]);
            // sharpen crests a little so dunes look wind-blown, not rounded
            h[x] = v + Math.abs(v) * 0.25f;
        }
        return h;
    }

    /** Fills a dune layer below its height line, shading by slope relative to the sun. */
    private static void fillDune(int[] px, float[] h, int base, int[] pal, boolean sunRight, Random rnd, float speckle) {
        // slope facing the sun is lit, facing away is shaded. Positive slope rises to the right.
        float[] facingAt = new float[WIDTH];
        for (int x = 0; x < WIDTH; x++) {
            float slope = (h[Math.min(WIDTH - 1, x + 1)] - h[Math.max(0, x - 1)]) * 0.5f;
            facingAt[x] = sunRight ? slope : -slope;
        }
        int away = sunRight ? -1 : 1;
        for (int x = 0; x < WIDTH; x++) {
            int top = Math.round(base - h[x]);
            for (int y = Math.max(0, top); y < HEIGHT; y++) {
                int depth = y - top;
                // light falls in at an angle: deeper pixels take the shading of a column further
                // away from the sun, so shadow edges run diagonally down from the crests
                int sx = Math.max(0, Math.min(WIDTH - 1, x + away * Math.round(depth * 1.4f)));
                float facing = facingAt[sx];
                int col;
                if (depth == 0) {
                    col = pal[0];
                } else if (facing > 0.25f) {
                    col = pal[1];
                } else if (facing > -0.05f) {
                    col = dither(x, y, (facing + 0.05f) / 0.3f) ? pal[1] : pal[2];
                } else if (facing > -0.45f) {
                    col = pal[2];
                } else {
                    col = dither(x, y, Math.min(1f, (-facing - 0.45f) / 0.4f)) ? pal[3] : pal[2];
                }
                // deeper sand darkens slightly towards the bottom of the layer
                if (depth > 14 && col == pal[1] && dither(x, y, Math.min(0.5f, (depth - 14) / 30f))) col = pal[2];
                if (speckle > 0 && depth > 1 && hash(x, y, rnd) < speckle) col = col == pal[1] ? pal[0] : pal[3];
                px[y * WIDTH + x] = col;
            }
        }
    }

    private static boolean dither(int x, int y, float level) {
        return level * 16f > BAYER[y & 3][x & 3] + 0.5f;
    }

    private static float dist(float x, float y, float cx, float cy) {
        float dx = x - cx, dy = y - cy;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    private static float hash(int x, int y, Random rnd) {
        int n = x * 374761393 + y * 668265263;
        n = (n ^ (n >>> 13)) * 1274126177;
        n ^= n >>> 16;
        return (n & 0xFFFF) / 65535f;
    }
}
