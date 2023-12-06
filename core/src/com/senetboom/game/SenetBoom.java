package com.senetboom.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class SenetBoom extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;

	// from scene2dUi
	Stage currentStage;
	Stage OptionsStage;

	Texture blackpiece;
	Texture whitepiece;
	Texture blackpieceSelected;
	Texture whitepieceSelected;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(background, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}

	public void createGame() {
		// create the game
	}

	public void createOptions() {
		// create the options
	}

	public void createMainMenu() {
		// create the main menu
	}

	public void createCredits() {
		// create the credits
	}

	public void calcualtePxByTile(int x, int y) {
		// calculate the pixel position of a tile
	}
}
