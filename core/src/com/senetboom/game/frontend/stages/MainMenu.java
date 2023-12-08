package com.senetboom.game.frontend.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.senetboom.game.SenetBoom;
import com.senetboom.game.backend.Board;

import static com.senetboom.game.SenetBoom.renderBoard;
import static com.senetboom.game.SenetBoom.tileSize;

public class MainMenu {
    public static Stage createMenu() {

        Stage menuStage = new Stage();
        Gdx.input.setInputProcessor(menuStage);

        Skin skin = SenetBoom.getSkin();

        // Begin of Main Menu Layout - Root Table arranges content automatically and adaptively as ui-structure
        final Table root = new Table();
        root.setFillParent(true);
        menuStage.addActor(root);

        final Image title = new Image(SenetBoom.logo);
        title.setSize(tileSize*4, tileSize*4);
        root.add(title).top().padBottom(tileSize/4);
        root.row();

        TextButton helpButton = new TextButton("Tutorial Book", skin);
        root.add(helpButton).padBottom(tileSize/4);
        // if help button is pressed, create a new stage for the help information
        helpButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                SenetBoom.createHelpStage();
            }
        });
        root.row();

        TextButton play2Button = new TextButton("Play 2 Player Game", skin);
        root.add(play2Button).padBottom(tileSize/4);

        play2Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                Board.initializeBoard();
                SenetBoom.gameStarted = true;
                SenetBoom.inGame = true;
                renderBoard();
            }
        });
        root.row();

        TextButton optionsButton = new TextButton("Options", skin);
        root.add(optionsButton).padBottom(tileSize/4);
        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SenetBoom.createOptionsStage();
            }
        });
        root.row();

        TextButton exitButton = new TextButton("Exit", skin);
        root.add(exitButton).padBottom(tileSize/40).padRight(tileSize/4);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // for exiting the game
                Gdx.app.exit();
                // for ending all background activity on Windows systems specifically
                System.exit(0);
            }
        });
        root.row();

        return menuStage;
    }
}
