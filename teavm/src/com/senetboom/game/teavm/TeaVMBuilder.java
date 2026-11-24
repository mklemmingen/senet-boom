package com.senetboom.game.teavm;

import com.github.xpenatan.gdx.backends.teavm.config.AssetFileHandle;
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuildConfiguration;
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuilder;
import com.senetboom.game.SenetBoom;
import org.teavm.tooling.TeaVMTool;

import java.io.File;
import java.io.IOException;

/**
 * WEB BUILD ENTRY POINT — run by the {@code :teavm:buildJavaScript} Gradle task.
 *
 * <p>This is a normal {@code main} method that drives the TeaVM tooling: it tells TeaVM which
 * assets to bundle, where to write the finished website, and which {@code ApplicationListener} to
 * start in the browser ({@link SenetBoom}). The output lands in {@code teavm/build/dist} —
 * zip that folder and upload it to itch.io.
 *
 * <p>You normally never touch this file; change the title/size below if you want a different
 * default canvas.
 */
public class TeaVMBuilder {

    public static void main(String[] args) throws IOException {
        TeaBuildConfiguration configuration = new TeaBuildConfiguration();

        // Assets to bundle into the website. Paths are relative to the teavm module directory.
        configuration.assetsPath.add(new AssetFileHandle("../assets"));

        // Where to write the generated website (index.html + JavaScript + assets).
        configuration.webappPath = new File("build/dist").getCanonicalPath();

        // The generated index.html: title + canvas size shown on the page / itch.io iframe.
        configuration.htmlTitle = "Senet Boom";
        configuration.htmlWidth = 1152;   // 1536x896 world at 75%; the canvas then fills the page anyway
        configuration.htmlHeight = 672;
        configuration.showLoadingLogo = false;   // no separate startup logo asset required

        // Start our game in the browser. The default TeaVM launcher instantiates this listener.
        configuration.setApplicationListener(SenetBoom.class);
        // our own launcher: canvas fills the page and follows its size
        configuration.mainClass = WebLauncher.class.getName();

        TeaVMTool tool = TeaBuilder.config(configuration);
        TeaBuilder.build(tool);
    }
}
