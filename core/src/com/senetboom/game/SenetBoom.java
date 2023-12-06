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
	boolean showOptions;
	Stage OptionsStage;
	boolean showCredits;
	Stage CreditsStage;

	Texture blackpiece;
	Texture whitepiece;
	Texture blackpieceSelected;
	Texture whitepieceSelected;

	Turn gameState;

	boolean gameStarted;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("badlogic.jpg");

		// from scene2dUi
		currentStage = new Stage();
		showOptions = false;
		OptionsStage = new Stage();
		showCredits = false;
		CreditsStage = new Stage();

		blackpiece = new Texture("blackpiece.png");
		whitepiece = new Texture("whitepiece.png");
		blackpieceSelected = new Texture("blackpieceSelected.png");
		whitepieceSelected = new Texture("whitepieceSelected.png");

		gameState = Turn.PLAYERWHITE;
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(background, 0, 0);
		batch.end();

		// from scene2dUi
		currentStage.act();
		currentStage.draw();

		if (showOptions) {
			OptionsStage.act();
			OptionsStage.draw();
		}

		if (showCredits) {
			CreditsStage.act();
			CreditsStage.draw();
		}

		if(gameStarted){
			// play an animation that decides randomly if white or black begins
			// if white begins, gameState = Turn.PLAYERWHITE
			// if black begins, gameState = Turn.PLAYERBLACK

			// TODO

			// draw the board
			createGame();

			gameStarted = false;
		}

		processTurn();
	}

	// enum of turn
	private enum Turn {
		PLAYERWHITE,
		PLAYERBLACK,
	}

	public void processTurn() {
		// process the turn

		//
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
