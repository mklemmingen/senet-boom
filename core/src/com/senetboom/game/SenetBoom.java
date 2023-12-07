package com.senetboom.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class SenetBoom extends ApplicationAdapter {

	// for the stage the bot moves a piece on
	public static Group botMovingStage;

	// for the empty tile texture
	public static Texture emptyTexture;

	// for the empty variables (the tiles that are currently moved by a bot)
	public static int emptyVariable;

	// for the possible moves that the bot uses for decision-making
	// Array of int values
	public static Array<Integer> possibleMoves;
	public static int possibleMove;

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
	static Stage stickStage;

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
	static Turn gameState;

	// for the game boolean value of it having just started
	static boolean gameStarted;

	// for the tileSize relative to screenSize from RelativeResizer
	public static float tileSize;

	// textures for special state
	public static Texture happy;
	public static Texture water;
	public static Texture safe;
	public static Texture rebirth;

	// boolean determining if the game is in progress
	public static boolean inGame;

	// typewriter
	public static Typewriter typeWriter;

	// for knowing when to skip the Turn
	public boolean skipTurn;

	// for setting to true once the sticks are thrown
	public boolean sticksThrown = false;

	// relative resizer
	public static RelativeResizer relativeResizer;

	public static Texture logo;

	// for the textbutton skin
	public static Skin skin;

	// if the Sticks are Tumbling
	public static boolean sticksTumbling;

	// current Stick value
	public static int currentStickValue;
	// Sticks
	public static Stick gameSticks;

	//Game End Stage
	public static Stage gameEndStage;

	// legitMove boolean
	public static boolean legitMove;

	// rebirth protection
	public static Texture rebirthProtection;

	// for the music volume
	public static float volume;

	// texture for the tile
	public static Texture tileTexture;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("textures/egypt.png");

		// from scene2dUi
		currentStage = new Stage();
		showOptions = false;
		OptionsStage = new Stage();
		showCredits = false;
		CreditsStage = new Stage();
		inGame = false;
		gameStarted = false;
		gameEndStage = new Stage();
		stickStage = new Stage();
		typeWriterStage = new Stage();
		hitStage = new Stage();
		helpOverlayStage = new Stage();
		handStage = new Stage();

		displayHelp = false;
		skipTurn = false;
		sticksTumbling = false;
		legitMove = false;

		// loading the skin
		skin = new Skin(Gdx.files.internal("menu.commodore64/uiskin.json"));

		// loading all textures
		blackpiece = new Texture("textures/blackpiece.png");
		whitepiece = new Texture("textures/whitepiece.png");
		// blackpieceSelected = new Texture("textures/blackpieceSelected.png");
		// whitepieceSelected = new Texture("textures/whitepieceSelected.png");
		happy = new Texture("textures/happy.png");
		water = new Texture("textures/water.png");
		safe = new Texture("textures/safe.png");
		rebirth = new Texture("textures/rebirth.png");
		rebirthProtection = new Texture("textures/rebirthprotection.png");
		logo = new Texture("textures/logo.png");
		tileTexture = new Texture("textures/tile.png");
		emptyTexture = new Texture("textures/empty.png");

		// for the empty tile texture
		emptyTexture = new Texture("textures/empty.png");

		// for the empty variable (the tile that is currently moved by a bot)
		emptyVariable = -1;

		// for the tileSize relative to screenSize from RelativeResizer
		tileSize = 80;

		gameState = Turn.PLAYERWHITE;
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);

		batch.begin();
		batch.draw(background, 0, 0);
		batch.end();

		// relative resizer
		// check to make sure the screen hasn't resized
		if(RelativeResizer.ensure()) {
			// if so, adapts tileSize already in RelativeResizer, we need to re-render the currentStage
			if(inGame){
				renderBoard();
			} else { // creates a main menu stage as a failsafe
				createMenu();
			}
			// sets viewport correctly
			resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}

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

			gameEndStage.act();
			gameEndStage.draw();

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
		if(sticksTumbling){
			Stick.update();
		}
		else{
			// ----------------- help part - add later
			// after thrown, check if any moves are even possible with the stick(dice) number
			// if the next player has no moves possible, a slight hint gets added to the screen
			// we run calculateMove on any piece of the player and if it returns null,
			// the next player has no moves possible

			// ----------------- legit Move part

			// wait for a legitMove from a player of gameState
			if(legitMove) {
				// if not legitMove, waits for a legitMove switch around
				// if legitMove, moves the piece in its respective dragStop Listener
				// check if the game is over
				checkForGameEnd();
				// switch turn after completing the move
				switchTurn();
			}
		}

		// ----------------- skip turn part
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
		renderBoard();
		gameSticks = new Stick();
		currentStickValue = gameSticks.getValue();
	}

	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}

	public static Coordinate calculatePXbyTile(int x, int y) {
		// calculate the pixel position of a tile
		int posX = 0;
		int posY = 0;

		// TODO

		return new Coordinate(posX, posY);
	}

	public static int calculateTilebyPx(int x, int y) {
		// calculate the tile position of a pixel
		int tile = 0;

		// TODO

		return tile;
	}

	public static void createHelpStage() {
	}

	public static void createOptionsStage() {
	}

	public static Skin getSkin() {
		return skin;
	}

	public static void renderBoard() {
		currentStage = Board.drawBoard();
	}

	private void createMenu() {
		currentStage = MainMenu.createMenu();
	}

	private void checkForGameEnd() {
		// goes over the gameBoard and counts, if someone has no pieces at all left
		// if so, the game ends and the winner is displayed
		// if not, the game continues

		Tile[] gameBoard = Board.getBoard();
		int whitePieces = 0;
		int blackPieces = 0;
		for(Tile tile : gameBoard){
			if(tile.hasPiece()){
				if(tile.getPiece().getColour() == Piece.Color.WHITE){
					whitePieces++;
				} else {
					blackPieces++;
				}
			}
		}
		if(whitePieces == 0){
			createGameEnd(Piece.Color.WHITE);
		}
		if(blackPieces == 0){
			createGameEnd(Piece.Color.BLACK);
		}
	}

	private void createGameEnd(Piece.Color color) {
		/*
		creats a stage that displays the winner
		 */

		// TODO
	}

	public static Piece.Color getTurn(){
		return gameState == Turn.PLAYERWHITE ? Piece.Color.WHITE : Piece.Color.BLACK;
	}
}
