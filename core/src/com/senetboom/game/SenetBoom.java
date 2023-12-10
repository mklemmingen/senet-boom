package com.senetboom.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.senetboom.game.backend.*;
import com.senetboom.game.frontend.actors.ExtraTurnActor;
import com.senetboom.game.frontend.stages.GameStage;
import com.senetboom.game.frontend.stages.MainMenu;
import com.senetboom.game.frontend.special.RelativeResizer;
import com.senetboom.game.frontend.text.Typewriter;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;

public class SenetBoom extends ApplicationAdapter {

	// for the stage the bot moves a piece on
	public static Group botMovingStage;

	// for the empty tile texture
	public static Texture emptyTexture;

	// for the empty variables (the tiles that are currently moved by a bot)
	public static int emptyVariable;

	// for the possible moves that the bot uses for decision-making
	// Array of int values
	public static ArrayList<Integer> possibleMoves;
	public static int possibleMove;

	// for the batch
	SpriteBatch batch;

	// for the background
	Texture background;

	// for the currentStage
	static Stage currentStage;

	// stage options
	public static boolean showOptions;
	Stage OptionsStage;

	// stage credit
	boolean showCredits;
	Stage CreditsStage;

	// stick stage
	public static Stage stickStage;

	// typeWriterStage
	public static Stage typeWriterStage;

	// hitStage
	Stage hitStage;
	Animation<TextureRegion> hitAnimation;

	// helpOverlayStage
	static Stage helpOverlayStage;
	Texture help;
	public static boolean displayHelp;

	// handStage
	Stage handStage;
	Texture hand;
	// for the pieces textures unselected
	public static Texture blackpiece;
	public static Texture whitepiece;

	// for the turn constant
	static Turn gameState;

	// for the game boolean value of it having just started
	public static boolean gameStarted;

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
	public static boolean skipTurn;

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

	// map stage
	public static Stage mapStage;

	// boolean for an extra turn
	public static boolean extraTurn;
	// texture
	public static Texture extraTurnTexture;

	//stage for extra turn
	public static Stage extraTurnStage;

	// stick value Stage
	private static Stage stickValueStage;

	// textures for the sticks
	public static Texture blackStick;
	public static Texture whiteStick;

	// number textures
	public static Texture number0;
	public static Texture number1;
	public static Texture number2;
	public static Texture number3;
	public static Texture number4;
	public static Texture number6;

	// for the game start
	public static boolean startUndecided;
	public static Stage deciderStage;

	// textures of starter
	public static Texture whiteStarts;
	public static Texture blackStarts;

	// boolean for the decider
	public static boolean deciderStarted;

	// for when a turn has not been checked yet
	public static boolean gameUnchecked;

	// hint stage
	public static Stage hintStage;

	// for displaying the current Turn
	public static Stage currentTurnStage;

	// turn play textures
	public static Texture whitePlays;
	public static Texture blackPlays;

	// texture for the help
	public static Texture helpTexture;

	// no moves texture
	public static Texture noMovesTexture;

	public static boolean displayHint = false;

	public static Texture possibleMoveTexture;

	public static boolean needRender;

	// enum of turn
	private enum Turn {
		PLAYERWHITE,
		PLAYERBLACK,
	}

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
		mapStage = new Stage();
		extraTurnStage = new Stage();
		stickValueStage = new Stage();
		deciderStage = new Stage();
		hintStage = new Stage();
		currentTurnStage = new Stage();

		// possible Moves is a ArrayList of int values
		possibleMoves = new ArrayList<Integer>();

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
		logo = new Texture("logoSenet.png");
		tileTexture = new Texture("textures/tile.png");
		emptyTexture = new Texture("textures/empty.png");
		extraTurnTexture = new Texture("textures/extraTurn.png");
		whiteStarts = new Texture("textures/whiteStart.png");
		blackStarts = new Texture("textures/blackStarts.png");

		// for the sticks
		blackStick = new Texture("textures/blackStick.png");
		whiteStick = new Texture("textures/whiteStick.png");

		// for the numbers
		number0 = new Texture("textures/0.png");
		number1 = new Texture("textures/1.png");
		number2 = new Texture("textures/2.png");
		number3 = new Texture("textures/3.png");
		number4 = new Texture("textures/4.png");
		number6 = new Texture("textures/6.png");

		// possible Move Texture
		possibleMoveTexture = new Texture("textures/possibleMove.png");

		// no moves texture
		noMovesTexture = new Texture("textures/noMovesTexture.png");

		// help texture
		helpTexture = new Texture("textures/rules.png");

		// for the turn plays textures
		whitePlays = new Texture("textures/whitePlays.png");
		blackPlays = new Texture("textures/blackPlays.png");

		// for the empty tile texture
		emptyTexture = new Texture("textures/empty.png");

		// for the empty variable (the tile that is currently moved by a bot)
		emptyVariable = -1;

		// for the tileSize relative to screenSize from RelativeResizer
		tileSize = 80;

		gameState = Turn.PLAYERWHITE;

		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		Gdx.input.setInputProcessor(currentStage);

		createHelpStage(); // for the help overlay that can be activated

		createMenu();
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);

		batch.begin();
		batch.draw(background, 0, 0);
		batch.end();

		if(needRender){
			renderBoard();
			needRender = false;
		}

		if(inGame){
			mapStage.act();
			mapStage.draw();
		}

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

		if(inGame){
			// all stages affiliated with the game ongoing

			if(gameStarted){
				// decide who starts!
				if(startUndecided){
					if(!(deciderStarted)) {
						// randomly decides the first turn state + displays this deciding on the screen
						decideStarter();
					}

					// gets called till the timer in deciderStarter is up, then it sets startUndecided to false
					deciderStage.act();
					deciderStage.draw();
					return;
				}

				updateLogoStage();
				// first act of a new game:
				// throw the sticks!
				gameSticks = new Stick();
				currentStickValue = gameSticks.getValue();
				sticksTumbling = true;
				// add the stickValueStage
				stickValueStage = SenetBoom.drawStickValue(currentStickValue);
				gameStarted = false;
			}

			// stick animation
			stickStage.act();
			stickStage.draw();

			// for the explicit typewriter
			typeWriterStage.act();
			typeWriterStage.draw();

			// for the switch animation (kinda like hit)
			hitStage.act();
			hitStage.draw();

			// for the hints if displayHint turned on
			if(displayHint) {
				hintStage.act();
				hintStage.draw();
			}

			// for the hand texture that follow the drag position of the piece
			// depending on if white or black (from up or from down)
			handStage.act();
			handStage.draw();

			// for the extra turn symbol
			extraTurnStage.act();
			extraTurnStage.draw();

			// current turn stage
			currentTurnStage.act();
			currentTurnStage.draw();

			if(!(sticksTumbling)) {
				// display the current stick value if the sticks are not tumbling
				stickValueStage.act();
				stickValueStage.draw();
			} else{
				// waiting for the sticks to end tumbling ( its animation )
				Stick.update(Gdx.graphics.getDeltaTime());
				return;
			}

			// for the display of the game having ended
			gameEndStage.act();
			gameEndStage.draw();

			// only if the sticks have stopped tumbling, and if the turn hasn't skipped,
			// we can let a turn be processed
			if (skipTurn) { // skip turn gets set to true if player pushed the skipTurn button

				// add a typewriter of ANGER or CONFUSED or SAD
				/* TODO
				if (gameState == Turn.PLAYERWHITE)
					typeWriter.makeSpeech(Typewriter.Emotion.ANGRY, Typewriter.Character.WHITE);
				else
					typeWriter.makeSpeech(Typewriter.Emotion.ANGRY, Typewriter.Character.BLACK);
			 	*/

				// other players turn
				switchTurn();

				skipTurn = false;
			}

			processTurn();
		}
	}

	public void processTurn() {

		// ----------------- legit Move part

		// wait for a legitMove from a player of gameState
		if(legitMove) {
			// resets the legitMove check
			legitMove = false;

			// if not legitMove, waits for a legitMove switch around
			// if legitMove, moves the piece in its respective dragStop Listener
			// check if the game is over
			checkForGameEnd();
			// switch turn after completing the move
			switchTurn();
		}
	}

	private void switchTurn() {
		// switch the turn


		if(extraTurn){ // if extraTurn is true, do not switch turn
			// player has an extra turn
			extraTurn = false;
		}
		else { // switch turn
			if (gameState == Turn.PLAYERWHITE) {
				gameState = Turn.PLAYERBLACK;
			} else {
				gameState = Turn.PLAYERWHITE;
			}
		}

		possibleMoves = new ArrayList<Integer>();

		gameSticks = new Stick();

		currentStickValue = gameSticks.getValue();

		// ----------------- help part
		// after thrown, check if any moves are even possible with the stick(dice) number
		// if the next player has no moves possible, a slight hint gets added to the screen
		// we run calculateMove on any piece of the player and if it returns null,
		// the next player has no moves possible

		if (gameState == Turn.PLAYERWHITE) {
			// check if any moves are possible for the white player
			// if not, add a hint to the screen
			checkForNoMoves(Turn.PLAYERWHITE);
			// if yes, remove the hint from the screen
		} else {
			// check if any moves are possible for the black player
			// if not, add a hint to the screen
			// if yes, remove the hint from the screen
			checkForNoMoves(Turn.PLAYERBLACK);
		}

		// ----------------- end of help part

		renderBoard();

		// update move logo
		updateLogoStage();

		renderBoard();
		Gdx.input.setInputProcessor(currentStage);
	}

	private static void checkForNoMoves(Turn turn) {

		hintStage.clear();
		// this method checks if the player has any moves possible
		// if not, it adds a "No moves possible" Actor to the hint screen
		boolean noMoves = true;
		Tile[] gameBoard = Board.getBoard();
		Piece.Color currentTurn = turn == Turn.PLAYERWHITE ? Piece.Color.WHITE : Piece.Color.BLACK;
		for(Tile tiles: gameBoard){
			if(tiles.hasPiece()){
				if(tiles.getPiece().getColour() == currentTurn){
					// if the piece is of the current turn
					// check if it has any moves possible
					if(tiles.isMoveValid(tiles.getPosition(), currentStickValue)){
						// if yes, set noMoves to false
						noMoves = false;
						possibleMoves.add(tiles.getPosition()+currentStickValue);
					}
				}
			}
		}
		if(noMoves){
			// if noMoves is true, add a hint to the screen
			Image noMove = new Image(noMovesTexture);
			// add at Position middle low
			noMove.setPosition((float) Gdx.graphics.getWidth() /2, tileSize*2);
			hintStage.addActor(noMove);

			// add a typewriter of ANGER or CONFUSED or SAD
			// TODO

			// render the board so that the possible Moves can be displayed on the screen
			renderBoard();
		}
	}

	// resize override
	@Override
	public void resize(int width, int height) {
		// from scene2dUi
		currentStage.getViewport().update(width, height, true);
		if (showOptions) {
			OptionsStage.getViewport().update(width, height, true);
		}
		if (showCredits) {
			CreditsStage.getViewport().update(width, height, true);
		}
		if(inGame){
			mapStage.getViewport().update(width, height, true);
			stickStage.getViewport().update(width, height, true);
			typeWriterStage.getViewport().update(width, height, true);
			hitStage.getViewport().update(width, height, true);
			helpOverlayStage.getViewport().update(width, height, true);
			handStage.getViewport().update(width, height, true);
			gameEndStage.getViewport().update(width, height, true);
			extraTurnStage.getViewport().update(width, height, true);
			stickValueStage.getViewport().update(width, height, true);
			deciderStage.getViewport().update(width, height, true);
			hintStage.getViewport().update(width, height, true);
			currentTurnStage.getViewport().update(width, height, true);
		}
	}

	private void updateLogoStage() {
		// updates the logo according to the turn constant

		// remove the old logo
		currentTurnStage.clear();

		Image currentTurner;
		// add the new logo
		if(gameState == Turn.PLAYERWHITE){
			currentTurner = new Image(whitePlays);
		} else {
			currentTurner = new Image(blackPlays);
		}
		// upper left corner
		currentTurner.setSize(tileSize*3, tileSize*2);
		currentTurner.setPosition(tileSize*2, tileSize * 8);
		currentTurnStage.addActor(currentTurner);

	}

	private void decideStarter() {

		deciderStarted = true;

		// random decide if turn White or black
		int random = (int) (Math.random() * 2);
		if(random == 0){
			gameState = Turn.PLAYERWHITE;
		} else {
			gameState = Turn.PLAYERBLACK;
		}
		// adds the Decider to the stage
		Decider decider = new Decider(gameState);
		deciderStage.addActor(decider);
	}

	private class Decider extends Actor{
		// after 2 seconds, it will set startDecided to true
		float X;
		float Y;
		// elapsed time since addition to stage
		float elapsed = 0;
		// this is the maximum duration that the bubble will be on the screen
		final float MAX_DURATION = 2f;
		// this is the stack of the bubble
		Stack stack;

		public Decider(Turn turn){
			this.stack = new Stack();
			stack.setSize(tileSize*4, tileSize*2);

			if(turn == Turn.PLAYERWHITE){
				stack.addActor(new Image(whiteStarts));
			} else {
				stack.addActor(new Image(blackStarts));
			}

			this.X = tileSize*8;
			this.Y = tileSize*2;
		}

		@Override
		public void act(float delta) {
			/*
			 * this method is called every frame to update the Actor
			 */
			super.act(delta);
			elapsed += delta;
			if (elapsed > MAX_DURATION) {
				startUndecided = false;
				remove(); // This will remove the actor from the stage
			}
		}

		// Override the draw method to add the stack at the correct position
		@Override
		public void draw(Batch batch, float parentAlpha) {
			/*
			This method is called every frame to draw the starter indicator
			 */
			super.draw(batch, parentAlpha);
			stack.setPosition(X, Y);
			stack.draw(batch, parentAlpha);
		}

	}

	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
		// dispose of all textures
		blackpiece.dispose();
		whitepiece.dispose();
		happy.dispose();
		water.dispose();
		safe.dispose();
		rebirth.dispose();
		rebirthProtection.dispose();
		logo.dispose();
		tileTexture.dispose();
		emptyTexture.dispose();
		extraTurnTexture.dispose();
		blackStick.dispose();
		whiteStick.dispose();
		number0.dispose();
		number1.dispose();
		number2.dispose();
		number3.dispose();
		number4.dispose();
		number6.dispose();
		whiteStarts.dispose();
		blackStarts.dispose();
	}

	public static Coordinate calculatePXbyTile(int x, int y) {
		// the screen is 1536 to 896. The board is 3x10 tiles and each tile is 80x80px.
		// the board is centered on the screen
		int screenWidth = 1536;
		int screenHeight = 896;
		int boardWidth = (int) (tileSize * 10);
		int boardHeight = (int) (tileSize * 3);

		// Calculate starting position of the board
		int boardStartX = (screenWidth - boardWidth) / 2;
		int boardStartY = (screenHeight - boardHeight) / 2;

		// Calculate tile position
		int posX, posY;

		if (y == 1) { // Middle row (reversed)
			posX = (int) (boardStartX + (9 - x) * tileSize);
		} else { // Top and bottom rows
			posX = (int) (boardStartX + x * tileSize);
		}

		posY = (int) (boardStartY + y * tileSize);

		return new Coordinate(posX, posY);
	}

	public static int calculateTilebyPx(int x, int y) {
		int screenWidth = 1536;
		int screenHeight = 896;
		int inFuncTileSize = (int) tileSize;
		int boardWidth = (inFuncTileSize * 10);
		int boardHeight = (inFuncTileSize * 3);

		// Calculate starting position (upper left corner) of the board
		int boardStartX = (screenWidth - boardWidth) / 2;
		int boardStartY = (screenHeight - boardHeight) / 2;

		// Adjust the y-coordinate to reflect libGDX's top-left origin
		int adjustedY = screenHeight - y;

		// Calculate which tile
		int tileX = (int) ((x - boardStartX) / inFuncTileSize);
		int tileY = (int) ((adjustedY - boardStartY) / inFuncTileSize);

		int tile;
		if (tileY == 0) { // Top row
			tile = tileX; // Tiles 0 to 9
		} else if (tileY == 1) { // Middle row (reversed)
			tile = 19 - tileX; // Tiles 19 to 10, in reverse
		} else if (tileY == 2) { // Bottom row
			tile = 20 + tileX; // Tiles 20 to 29
		} else {
			// Handle the case where the coordinates are outside the board
			tile = -1;
		}

		return tile;
	}


	public static void createHelpStage() {
		// load texture help
		Image help = new Image(helpTexture);
		help.setPosition(tileSize*13.1f, tileSize*2.7f);
		help.setSize(tileSize*7, tileSize*9);
		// add it to help stage
		helpOverlayStage.addActor(help);
	}

	public static void createOptionsStage() {
	}

	public static Skin getSkin() {
		return skin;
	}

	public static void renderBoard() {

		stickValueStage.clear();
		stickValueStage = SenetBoom.drawStickValue(currentStickValue);

		mapStage.clear();
		mapStage = GameStage.drawMap();

		currentStage.clear();
		currentStage = GameStage.drawBoard();

		Gdx.input.setInputProcessor(currentStage);
	}

	private static Stage drawStickValue(int currentStickValue) {

		Stage stage = new Stage();

		// scene 2D UI stuff
		Stack rawValue = new Stack();
		Table sticks = new Table();

		Image number;
		// switch statement for currentStickValue 1,2,3,4,6
		switch(currentStickValue){
			case 1:
				// draw 3 black sticks
				// draw 1 white sticks
				sticks.add(new Image(blackStick));
				sticks.add(new Image(blackStick));
				sticks.add(new Image(blackStick));
				sticks.add(new Image(whiteStick));
				number = new Image(number1);
				rawValue.add(number);
				break;
			case 2:
				// draw 2 black sticks
				// draw 2 white sticks
				sticks.add(new Image(blackStick));
				sticks.add(new Image(blackStick));
				sticks.add(new Image(whiteStick));
				sticks.add(new Image(whiteStick));
				number = new Image(number2);
				rawValue.add(number);
				break;
			case 3:
				// draw 1 black sticks
				// draw 3 white sticks
				sticks.add(new Image(blackStick));
				sticks.add(new Image(whiteStick));
				sticks.add(new Image(whiteStick));
				sticks.add(new Image(whiteStick));
				number = new Image(number3);
				rawValue.add(number);
				break;
			case 4:
				// draw 0 black sticks
				// draw 4 white sticks
				sticks.add(new Image(whiteStick));
				sticks.add(new Image(whiteStick));
				sticks.add(new Image(whiteStick));
				sticks.add(new Image(whiteStick));
				number = new Image(number4);
				rawValue.add(number);
				break;
			case 6:
				// draw 4 black sticks
				// draw 0 white sticks
				sticks.add(new Image(blackStick));
				sticks.add(new Image(blackStick));
				sticks.add(new Image(blackStick));
				sticks.add(new Image(blackStick));
				number = new Image(number6);
				rawValue.add(number);
				break;
		}

		sticks.setPosition(tileSize, tileSize*2);
		sticks.setSize(tileSize*2, tileSize*2);
		stage.addActor(sticks);

		rawValue.setPosition(tileSize, tileSize*2.25f);
		stage.addActor(rawValue);
		return stage;
	}

	public static void createMenu() {
		currentStage = MainMenu.createMenu();
		Gdx.input.setInputProcessor(currentStage);
		inGame = false;
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

	public static void addExtraTurnActor(){
		ExtraTurnActor newTurn = new ExtraTurnActor();
		extraTurnStage.addActor(newTurn);
		System.out.println("Added ExtraTurn Actor to Screen\n");
	}
}
