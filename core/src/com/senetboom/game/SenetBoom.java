package com.senetboom.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class SenetBoom extends ApplicationAdapter {

	// for the stage the bot moves a piece on
	public static Group botMovingStage;

	// for the empty tile texture
	public static Texture emptyTexture;

	// for the empty variables (the tiles that are currently moved by a bot)
	public static int[] emptyVariables;

	// for the batch
	SpriteBatch batch;

	// for the background
	Texture background;

	// for the currentStage
	static Stage currentStage;

	// stage options
	boolean showOptions;
	Stage OptionsStage;

	// stage credit
	boolean showCredits;
	Stage CreditsStage;

	// stick stage
	Stage stickStage;

	// typeWriterStage
	static Stage typeWriterStage;

	// hitStage
	Stage hitStage;
	Animation<TextureRegion> hitAnimation;

	// helpOverlayStage
	Stage helpOverlayStage;
	Texture help;
	boolean displayHelp;

	// handStage
	Stage handStage;
	Texture hand;
	// for the pieces textures unselected
	static Texture blackpiece;
	static Texture whitepiece;

	// for the selected pieces textures
	Texture blackpieceSelected;
	Texture whitepieceSelected;

	// for the turn constant
	Turn gameState;

	// for the game boolean value of it having just started
	boolean gameStarted;

	// for the tileSize relative to screenSize from RelativeResizer
	public static float tileSize;

	// textures for special state
	public static Texture house;
	public static Texture water;
	public static Texture safe;

	// boolean determining if the game is in progress
	public boolean inGame;

	// typewriter
	public static Typewriter typeWriter;

	// for knowing when to skip the Turn
	public boolean skipTurn;

	// for setting to true once the sticks are thrown
	public boolean sticksThrown = false;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("egypt.png");

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

		//loading screen

		// from scene2dUi
		currentStage.act();
		currentStage.draw();

		if(displayHelp){
			helpOverlayStage.act();
			helpOverlayStage.draw();
		}

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
			renderBoard();

			gameStarted = false;
		}

		if(inGame){
			// all stages affiliated with the game ongoing
			stickStage.act();
			stickStage.draw();

			typeWriterStage.act();
			typeWriterStage.draw();

			hitStage.act();
			hitStage.draw();

			handStage.act();
			handStage.draw();

			processTurn();
		}
	}

	// enum of turn
	private enum Turn {
		PLAYERWHITE,
		PLAYERBLACK,
	}

	public void processTurn() {
		// --------------- stickThrow part
		// set waiting for a stickThrow
		// wait for a stickThrow

		// --------------- calculate Move part
		if(sticksThrown) {


			// after thrown, check if any moves are even possible with the stick(dice) number
			// if the next player has no moves possible, the turns get ended and switched with skipTurn = true
			// we run calculateMove on any piece of the player and if it returns null,
			// the next player has no moves possible

			// ----------------- legit Move part

			// wait for a legitMove from a player of gameState

			// if not legitMove, wait for a legitMove from a player

			// if legitMove, move the piece

			// check if the game is over

			// switch turn after completing the move
			switchTurn();

			// ----------------- skip turn part
		}

		// check, beside if(legitMove) above, if the player has decided to push the skip turn button
		if (skipTurn) {
			// add a typewriter of ANGER or CONFUSED or SAD
			if (gameState == Turn.PLAYERWHITE)
				typeWriter.makeSpeech(Typewriter.Emotion.ANGRY, Typewriter.Character.WHITE);
			else
				typeWriter.makeSpeech(Typewriter.Emotion.ANGRY, Typewriter.Character.BLACK);
			skipTurn = false;
			// other players turn
			switchTurn();
		}


	}

	private void switchTurn() {
		// switch the turn
		if (gameState == Turn.PLAYERWHITE) {
			gameState = Turn.PLAYERBLACK;
		} else {
			gameState = Turn.PLAYERWHITE;
		}
		sticksThrown = false;
	}


	public static void renderBoard() {
		currentStage = Board.drawBoard();
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

	public static Coordinate calculatePXbyTile(int x, int y) {
		// calculate the pixel position of a tile
		int posX = 0;
		int posY = 0;

		return new Coordinate(posX, posY);
	}
}
