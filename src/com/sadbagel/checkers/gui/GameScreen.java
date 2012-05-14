package com.sadbagel.checkers.gui;

import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.sadbagel.checkers.backend.CheckersAI;
import com.sadbagel.checkers.backend.CheckersBoard;
import com.sadbagel.checkers.backend.Coordinate;
import com.sadbagel.checkers.backend.GUIMovement;
import com.sadbagel.checkers.backend.Move;

public class GameScreen extends BasicGameState implements ComponentListener{

	//State ID
	int stateID = -1;

	GameScreen(int stateID){
		this.stateID = stateID;
	}

	//Background Image
	Image background = null;
	Image infoBox = null;
	Image optionsMenu = null;

	//Image variables for game pieces
	Image redPiece = null;
	Image redKing = null;
	Image blackPiece = null;
	Image blackKing = null;

	//Board Images
	Image blackSpace = null;
	Image whiteSpace = null;
	Image glowForced = null;
	Image glowSelection = null;

	//Variables for tracking current move
	final int RED = 0;
	final int BLACK = 1;
	int currentMove = RED;

	//Menu Object that changes game options/states
	Menu menu = null;

	//Piece Codes
	final int E = 0;
	final int R = 1;
	final int B = 2;
	final int RK = 3;
	final int BK = 4;

	//Board Configuration
	MouseOverArea board[][] = new MouseOverArea[8][8];//directions are -->,VVV
	int boardX = 60;
	int boardY = 60;

	private Music backgroundMusic;
	private GameContainer container;

	//Board Draw Top-Left
	int boardTLX = 0;
	int boardTLY = 0;

	//Button Functions
	Image optionsImage = null;
	MouseOverArea options;
	private boolean toggleMenu = false;

	//StatGUI
	Image statisticsImage = null;
	MouseOverArea showStatistics;
	StatGUI stat = new StatGUI();

	//Surrender Button
	MouseOverArea surrender;

	//Prompt Boxes
	PromptBox surrenderBox;
	PromptBox newGameBox;
	PromptBox quitGameBox;
	GameTypePrompt gameTypeBox;

	//Checkers Game and Board Objects
	CheckersBoard backendBoard;
	int turn;
	boolean gameOver = false;
	int totalTurns = 0;
	ArrayList<Move> possibleMoves;
	Move move = null;
	Coordinate jumpFrom;
	CheckersAI AI;	
	boolean playerTwoAI = true;
	Integer[][] guiBoard;
	int winner = 0;
	Image winnerImage;

	//Animating Piece Movement
	ArrayList<GUIMovement> moveList = new ArrayList<GUIMovement>();
	GUIMovement lastMove = null;
	PieceAnimation pieceMovement = null;

	//Player Movement
	ArrayList<Coordinate> playerOneMove = new ArrayList<Coordinate>();
	ArrayList<Coordinate> playerTwoMove = new ArrayList<Coordinate>();

	@Override
	public void init(GameContainer container, StateBasedGame sbg)
			throws SlickException {
		//Setup GameBoard and Images
		if(!Globals.RESOURCES_INITIATED){
			Checkers.initRessources();
		}

		this.container = container;

		//Setup Images
		background = ResourceManager.getImage("bg3");
		infoBox = ResourceManager.getImage("infoBox");
		redPiece = ResourceManager.getImage("red");
		redKing = ResourceManager.getImage("redking");
		blackPiece = ResourceManager.getImage("black");
		blackKing = ResourceManager.getImage("blackking");

		//Board Images
		whiteSpace = ResourceManager.getImage("whitespace");
		blackSpace = ResourceManager.getImage("blackspace");
		glowSelection = ResourceManager.getImage("glowSelection");
		glowForced = ResourceManager.getImage("glowForced");

		optionsImage = ResourceManager.getImage("options");

		//Init Board
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<8;j++)
			{
				board[i][j]=new MouseOverArea(
						this.container,
						((i+j)%2==0?whiteSpace:blackSpace)/*Alternating white and black squares. Formula is arbitrary.*/,
						boardX+64*i,
						boardY+64*j,
						64,
						64);
				board[i][j].addListener(new SquareListener(i,j,this));
			}
		}

		//Setup Options and Overlays
		options = new MouseOverArea(container, ResourceManager.getImage("showOptions"), 637, 550, 163, 29, this);
		showStatistics = new MouseOverArea(container, ResourceManager.getImage("showStatistics"), 637, 550, 163, 29, this);
		surrender = new MouseOverArea(container, ResourceManager.getImage("surrender"), 675, 510, 130, 29, this);
		stat.init();

		//Prompt Boxes
		surrenderBox = new PromptBox("Warning: Surrendering will result in a loss.\n" +
				"\n" +
				"Are you sure you want to lose?!\n" +
				"ARE YOU!? What are you, some big loser?\n\n" +
				"Well, just choose 'OKAY' if you want to give up and lose.\n" +
				"Otherwise, choose 'CANCEL' if you want to try to be a winner!");

		newGameBox = new PromptBox("Warning: Starting a new game when a game is currently in progress\n" +
				"will result in a loss.\n" +
				"\n" +
				"Are you sure you want to continue and lose?\n\n" +
				"Just click 'OKAY' to lose and start a new game,\n" +
				"or click 'CANCEL' if you want to try to win.");

		gameTypeBox = new GameTypePrompt();

		//Prompt Boxes
		surrenderBox.okayButton = new MouseOverArea(container, ResourceManager.getImage("okay"), 125, 405, 150, 25, this);
		surrenderBox.cancelButton = new MouseOverArea(container, ResourceManager.getImage("cancel"), 525, 405, 150, 25, this);
		newGameBox.okayButton = new MouseOverArea(container, ResourceManager.getImage("okay"), 125, 405, 150, 25, this);
		newGameBox.cancelButton = new MouseOverArea(container, ResourceManager.getImage("cancel"), 525, 405, 150, 25, this);
		//End Prompt Boxes

		//GameType Box
		gameTypeBox.pvaiButton = new MouseOverArea(container, ResourceManager.getImage("1player"), 125, 405, 150, 25, this);
		gameTypeBox.pvpButton = new MouseOverArea(container, ResourceManager.getImage("2player"), 290, 405, 150, 25, this);

		//Setup Menu Buttons		
		menu = new Menu(265,320, 5);
		this.container = container;
		for (int i=0;i<5;i++) {
			if( i == Menu.NEWGAME){
				menu.setArea(i, new MouseOverArea(this.container, ResourceManager.getImage("startgame"), menu.x,  menu.y + (i*50), 270, 42, this));				
			}
			else if (i == Menu.SAVEGAME){
				menu.setArea(i, new MouseOverArea(this.container, ResourceManager.getImage("savegame"), menu.x,  menu.y + (i*50), 270, 42, this));
			}
			else if (i == Menu.LOADGAME){
				menu.setArea(i, new MouseOverArea(this.container, ResourceManager.getImage("loadgame"), menu.x,  menu.y + (i*50), 270, 42, this));
			}
			else if (i == Menu.STATISTICS){
				menu.setArea(i, new MouseOverArea(this.container, ResourceManager.getImage("stat"), menu.x,  menu.y + (i*50), 270, 42, this));				
			}
			else if (i == Menu.QUITGAME){
				menu.setArea(i, new MouseOverArea(this.container, ResourceManager.getImage("quitgame"), menu.x,  menu.y + (i*50), 270, 42, this));
			}
			menu.areas[i].setNormalColor(new Color(1,1,1,0.8f));
			menu.areas[i].setMouseOverColor(new Color(1,1,1,0.9f));
		}

		//Reset for new game
		reset();

		//Music Options
		backgroundMusic = ResourceManager.getMusic("normal");
		backgroundMusic.loop();
	}

	private void reset() {
		//Turn off Menu if activated
		if(menu.isActivated())
			menu.toggle();

		//Checkers Board
		backendBoard = new CheckersBoard();
		turn = 1; //Player 1
		AI = new CheckersAI(backendBoard);
		guiBoard = new Integer[8][8];

		//Blank GUIBoard
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++)
				guiBoard[i][j] = 0;

		//Reset Game Variables based on Globals
		if(Globals.loadGame){

		}
		else{
			gameOver = false;
			totalTurns = 0;
			possibleMoves = null;
			move = null;
			jumpFrom = null;

			playerTwoAI = Globals.playerTwoAI;
			winner = 0;

			lastMove = null;
			pieceMovement = null;

			winnerImage = null;

			//Reset Player Movement
			playerOneMove.clear();
			playerTwoMove.clear();
		}		
	}

	@Override
	public void render(GameContainer container, StateBasedGame sbg, Graphics g)
			throws SlickException {
		//Render the background, game board, pieces, and menu (if activated)

		background.draw(0, 0);
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<8;j++)
			{
				board[i][j].render(container,g);
			}
		}

		//TODO: Draw the Pieces on top of the game board
		//Game Board tiles will be 64 x 64, pieces will be 48x48
		//So there will be a trim of 6px around the pieces on all sides
		int padding = 8;

		for(int i=0; i < 8; i++){
			for(int j=0; j < 8; j++){
				if(guiBoard[i][j] == R){
					//Draw Red
					redPiece.draw(boardX + (j*64) + padding, boardY + (i*64) + padding);
				}
				else if(guiBoard[i][j] == B){
					//Draw Black
					blackPiece.draw(boardX + (j*64) + padding, boardY + (i*64) + padding);
				}
				else if(guiBoard[i][j] == RK){
					//Draw RedKing
					redKing.draw(boardX + (j*64) + padding, boardY + (i*64) + padding);
				}
				else if(guiBoard[i][j] == BK){
					//Draw BlackKing
					blackKing.draw(boardX + (j*64) + padding, boardY + (i*64) + padding);
				}
				else if(guiBoard[i][j] == E){
					//do not draw
				}
			}
		}
		
		//If no move has been made
		if(playerOneMove.size()==0 && playerTwoMove.size()==0)
		{
			//turn;
			for(Move m: backendBoard.getJumps(turn))
			{
				int x, y;
				x = (8-m.getStart().getX())*64-4;
				y = (8-m.getStart().getY())*64-4;
				glowForced.draw(x,y);
			}
			
			
			
		}
		

		//Highlighting Glow
		if(playerOneMove.size() == 1){
			int x, y;
			x = (boardX + (7-playerOneMove.get(0).getX())*64);
			y = (boardY + (7-playerOneMove.get(0).getY())*64);
			glowSelection.draw(x,y);
			//start Prediction
			Iterator<Move> iter;
			if(!backendBoard.playerOneJump(playerOneMove.get(0)).isEmpty() || !backendBoard.playerOneMove(playerOneMove.get(0)).isEmpty())
			{
				if(!backendBoard.playerOneJump(playerOneMove.get(0)).isEmpty())
				{
					iter = backendBoard.playerOneJump(playerOneMove.get(0)).iterator();
				}
				else
				{
					iter = backendBoard.playerOneMove(playerOneMove.get(0)).iterator();
				}
				while(iter.hasNext())
				{
					Move m = iter.next();
					x = (boardX + (7-m.getEnd().getX())*64);
					y = (boardY + (7-m.getEnd().getY())*64);
					glowForced.draw(x,y);
				}

			}
			//end Prediction

		}
		else if(playerTwoMove.size() == 1){
			int x, y;
			x = (boardY + (7-playerTwoMove.get(0).getX())*64);//changed to playerTwoMove, might have been an issue.
			y = (boardX + (7-playerTwoMove.get(0).getY())*64);
			glowSelection.draw(x,y);
			
			//start Prediction
			Iterator<Move> iter;
			if(!backendBoard.playerTwoJump(playerTwoMove.get(0)).isEmpty() || !backendBoard.playerTwoMove(playerTwoMove.get(0)).isEmpty())
			{
				if(!backendBoard.playerTwoJump(playerTwoMove.get(0)).isEmpty())
				{
					iter = backendBoard.playerTwoJump(playerTwoMove.get(0)).iterator();
				}
				else
				{
					iter = backendBoard.playerTwoMove(playerTwoMove.get(0)).iterator();
				}
				while(iter.hasNext())
				{
					Move m = iter.next();
					x = (boardX + (7-m.getEnd().getX())*64);
					y = (boardY + (7-m.getEnd().getY())*64);
					glowForced.draw(x,y);
				}

			}
			//end Prediction
		}

		//TODO: Andy will make pieces able to move/jump using magic
		//movement.render(arg0, arg2);
		if(pieceMovement != null && pieceMovement.isActivated()){
			pieceMovement.render(container, g);
		}

		//Turn Information
		infoBox.draw(600, 190);
		g.setColor(Color.white);
		String temp = "";
		temp += totalTurns;
		g.drawString(temp, 660, 201);
		if(turn == 1){
			redPiece.draw(646, 250);
		}
		else{
			blackPiece.draw(646, 250);
		}		

		//Render Surrender Button
		surrender.render(container, g);

		//Options Menu Stuff
		if(menu.isActivated()){
			g.setColor(new Color(0,0,0,200));
			g.fillRect(0, 0, 800, 600);
			menu.render(container, g);
			optionsImage.draw(250,50);
		}
		options.render(container, g);
		stat.render(container, g);
		if(stat.isActivated()){
			showStatistics.render(container, g);
		}

		//Display Winner if any
		if(winnerImage != null)
			winnerImage.draw(250, 250);

		//PromptBoxes
		surrenderBox.render(container, g);
		newGameBox.render(container, g);
		gameTypeBox.render(container, g);

	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
			throws SlickException {

		//Make Sure AI set correctly
		if(Globals.playerTwoAI != playerTwoAI)
			playerTwoAI = Globals.playerTwoAI;

		//Check if Board is Blank
		boolean isBlank = true;
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++)
				if(guiBoard[j][i] > 0)
					isBlank = false;
		if(isBlank){
			//Update GUIBoard stupidly
			String tempBoard = backendBoard.toGUI();
			for(int i=0; i < 8; i++){
				for(int j=0; j < 8; j++){
					String x = "";
					x += tempBoard.charAt(i*8 + j);
					guiBoard[i][j] = Integer.parseInt(x);
				}				
			}
		}

		//Check if the game wants to exit
		if(menu.shouldExit){
			container.exit();
		}

		//Check if menu needs to be toggled
		if(toggleMenu){
			toggleMenu = false;
			menu.toggle();
		}

		//Game Code
		possibleMoves = backendBoard.getPossibleMoves(turn); //Get Possible Moves for Current Player

		if(possibleMoves.isEmpty() || gameOver || winner > 0){
			gameOver = true;
		}
		//Only accept moves when not animating
		else if(pieceMovement == null || !pieceMovement.isActivated()){
			//Check if AI or Player
			if(playerTwoAI && turn == 2){
				do{
					move = AI.playerAI(turn);
				}
				while( !possibleMoves.contains(move) );

				jumpFrom = backendBoard.move(move);
				backendBoard.promote();

				//Check for Consecutive Jumping
				possibleMoves = backendBoard.getJumps(jumpFrom, turn);

			}
			else if(turn == 2 && !playerTwoAI){
				//Player 2 is Human
				if(playerTwoMove.size() == 2){
					//Player has a possible move
					Move temp = new Move(playerTwoMove.get(0),playerTwoMove.get(1));
					if(possibleMoves.contains(temp)){
						//Valid Move
						//makes move and promotes pieces
						jumpFrom = backendBoard.move(temp);
						backendBoard.promote();

						//Check for Consecutive-Jumping
						possibleMoves = backendBoard.getJumps(jumpFrom, turn);						
						playerTwoMove.clear();
					}
					else{
						//Not Valid Move
						playerTwoMove.clear();
					}
				}
			}
			else if(turn == 1){
				if(playerOneMove.size() == 2){
					//Player has a possible move
					Move temp = new Move(playerOneMove.get(0),playerOneMove.get(1));
					if(possibleMoves.contains(temp)){
						//Valid Move
						//makes move and promotes pieces
						jumpFrom = backendBoard.move(temp);
						backendBoard.promote();

						//Check for Consecutive-Jumping
						possibleMoves = backendBoard.getJumps(jumpFrom, turn);						
						playerOneMove.clear();
					}
					else{
						//Not Valid Move
						playerOneMove.clear();
					}
				}
			}

			//Player has no Multi-Jump, increment turn
			if(possibleMoves.isEmpty()){
				turn %= 2;
				turn++;
				totalTurns++;
			}

			//Player Animation
			lastMove = (!gameOver ? backendBoard.getLastMove() : null);
			if(lastMove != null)
				lastMove.setJumper(guiBoard[7-lastMove.getMove().getStart().getY()][7-lastMove.getMove().getStart().getX()]);

			//Update GUIBoard stupidly
			String tempBoard = backendBoard.toGUI();
			for(int i=0; i < 8; i++){
				for(int j=0; j < 8; j++){
					String x = "";
					x += tempBoard.charAt(i*8 + j);
					guiBoard[i][j] = Integer.parseInt(x);
				}				
			}
		}

		//Piece Movement Animation Setup
		if(lastMove != null && !gameOver){
			if(lastMove.getJumper() == 0)
				lastMove.setJumper(guiBoard[7-lastMove.getMove().getEnd().getY()][7-lastMove.getMove().getEnd().getX()]);

			guiBoard[7-lastMove.getMove().getEnd().getY()][7-lastMove.getMove().getEnd().getX()] = 0; //Blank out			
			pieceMovement = new PieceAnimation(lastMove);
			pieceMovement.toggle();
			lastMove = null;
		}
		else if(gameOver && lastMove == null && !pieceMovement.isActivated()){
			//Show Final Board
			String tempBoard = backendBoard.toGUI();
			for(int i=0; i < 8; i++){
				for(int j=0; j < 8; j++){
					String x = "";
					x += tempBoard.charAt(i*8 + j);
					guiBoard[i][j] = Integer.parseInt(x);
				}				
			}

			//Determine Winner
			if(winner < 1){
				if(turn%2 == 0){
					winner = 1;
				}
				else{
					winner = 2;
				}
				menu.toggle(); //Show menu because the game has ended
			}

			//Show winning screen
			if(winner == 1){
				winnerImage = ResourceManager.getImage("p1win");
			}
			else{
				winnerImage = ResourceManager.getImage("p2win");
			}


		}
	}

	@Override
	public int getID() {
		//Return GameState ID
		return stateID;
	}

	public boolean validSquare(int x, int y){
		if(y%2 == 0){
			if(x%2 == 0){
				return false;
			}
			else{
				return true;
			}
		}
		else{
			if(x%2 == 1){
				return false;
			}
			else{
				return true;
			}
		}
	}

	/**
	 * squareClicked
	 * @param x - Location of square on board
	 * @param y - Location of square on board
	 * 
	 * Handles the event when board square (X,Y) is clicked.
	 * In the future, it might communicate with the backend.
	 *   Convoluted process to reach this method enables the board to be rendered differently while leaving this method the same.
	 */
	public void squareClicked(int x, int y) {
		if(turn == 1){
			if(playerOneMove.size() < 2){
				//Check if valid square, clear move if not.
				if(validSquare(x,y)){
					//Check if player owns piece
					int piece = guiBoard[y][x];
					if(playerOneMove.size() == 0){
						if(piece == R || piece == RK){
							playerOneMove.add(new Coordinate(7-x,7-y));
							System.out.println("Coord: " + playerOneMove.get(playerOneMove.size()-1).getX() + " " + playerOneMove.get(playerOneMove.size()-1).getY());
						}
					}
					else{
						playerOneMove.add(new Coordinate(7-x,7-y));
						System.out.println("Coord: " + playerOneMove.get(playerOneMove.size()-1).getX() + " " + playerOneMove.get(playerOneMove.size()-1).getY());
					}
				}
				else{
					//Clear Move if not Valid
					playerOneMove.clear();
				}
			}
			else{
				//Shouldn't get this high, just a safety net.
				playerOneMove.clear();
			}
		}
		else if(turn == 2 && !playerTwoAI){
			if(playerTwoMove.size() < 2){
				//Check if valid square, clear move if not.
				if(validSquare(x,y)){
					//Check if player owns piece
					int piece = guiBoard[y][x];
					if(playerTwoMove.size() == 0){
						if(piece == B || piece == BK){
							playerTwoMove.add(new Coordinate(7-x,7-y));
							System.out.println("Coord: " + playerTwoMove.get(playerTwoMove.size()-1).getX() + " " + playerTwoMove.get(playerTwoMove.size()-1).getY());
						}
					}
					else{
						playerTwoMove.add(new Coordinate(7-x,7-y));
						System.out.println("Coord: " + playerTwoMove.get(playerTwoMove.size()-1).getX() + " " + playerTwoMove.get(playerTwoMove.size()-1).getY());
					}
				}
				else{
					//Clear Move if not Valid
					playerTwoMove.clear();
				}
			}
			else{
				//Shouldn't get this high, just a safety net.
				playerTwoMove.clear();
			}
		}		
	}

	//Was lazy, so added the options menu button directly to GameScreen :D
	//Coding Menu stuff per Screen because I can.
	public void componentActivated(AbstractComponent source) {

		//Prompt Boxes
		if(surrenderBox.isActivated()){
			if(source == surrenderBox.cancelButton){
				surrenderBox.toggle();
			}
			else if(source == surrenderBox.okayButton){
				//Logic for Surrendering
			}
		}

		if(newGameBox.isActivated()){
			if(source == newGameBox.cancelButton){
				newGameBox.toggle();
			}
			else if(source == newGameBox.okayButton){
				//TODO: This Area
				//Logic for starting a New Game and losing
				//AddLoss
				//SaveStatFile
				newGameBox.toggle();
				gameTypeBox.toggle();
			}
		}

		if(gameTypeBox.isActivated()){
			if(source == gameTypeBox.pvpButton){
				//Setup Globals for PVP
				Globals.playerTwoAI = false;
				gameTypeBox.toggle();
				reset();
			}
			else if(source == gameTypeBox.pvaiButton){
				//Setup Globals for PvAI
				Globals.playerTwoAI = true;
				gameTypeBox.toggle();
				reset();
			}
		}

		//Surrender
		if(source == surrender && !menu.isActivated()){
			//Prompt User of Consequences
			//Save Statistics
			//Prompt User for New Game or Exit Game
			surrenderBox.toggle();
		}

		//Listens for "Show Options"
		if(source == options && !stat.isActivated() && !surrenderBox.isActivated() 
				&& !newGameBox.isActivated() && !gameTypeBox.isActivated()) {
			toggleMenu = true;
		}

		//Listens for "Show Staistics" (closes Stat Overlay)
		if(source == showStatistics && stat.isActivated()){
			stat.toggle();
		}

		//Menu Only works if prompts not active
		if(menu.isActivated() && !newGameBox.isActivated() && 
				!surrenderBox.isActivated() && !gameTypeBox.isActivated()){
			if(!stat.isActivated()){ //TODO: add logic to check other overlays
				if(source == menu.areas[Menu.NEWGAME]){
					//Logic for New Game while playing a Game
					if(!gameOver){
						newGameBox.toggle();
					}
					else{
						gameTypeBox.toggle();
					}
				}
				else if (source == menu.areas[Menu.SAVEGAME]){
					//Logic for Saving Game
				}
				else if (source == menu.areas[Menu.LOADGAME]){
					//Logic for Loading a Game
				}
				else if(source == menu.areas[Menu.STATISTICS]){
					//Toggle the Statistics Overlay
					stat.toggle();
				}
				else if (source == menu.areas[Menu.QUITGAME]){
					//Logic for Quitting Game
					if(gameOver){
						//Okay to exit
						menu.shouldExit = true;
					}
					else{
						//Prompt user about exiting
					}
				}
			}
		}

	}

	/**
	 *This private class handles all the interactions for the GameBoard.
	 */
	private class SquareListener implements ComponentListener
	{
		private int x;
		private int y;
		private GameScreen game;
		public SquareListener(int x, int y, GameScreen game)
		{
			this.x = x;
			this.y = y;
			this.game = game;
		}

		@Override
		public void componentActivated(AbstractComponent arg0) {
			if(!menu.isActivated() && !surrenderBox.isActivated())
				game.squareClicked(x,y);
		}

	}

}
