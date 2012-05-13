package com.sadbagel.checkers.gui;

import java.util.ArrayList;

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
	
	//Image variables for game pieces
	Image redPiece = null;
	Image redKing = null;
	Image blackPiece = null;
	Image blackKing = null;
	Image blackSpace = null;
	Image whiteSpace = null;
	
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
	
	
	//Checkers Game and Board Objects
	CheckersBoard backendBoard;
	int turn;
	boolean gameOver = false;
	int totalTurns = 0;
	ArrayList<Move> possibleMoves;
	Move move = null;
	Coordinate jumpFrom;
	CheckersAI AI;	
	boolean playerOneAI = true;
	boolean playerTwoAI = true;
	Integer[][] guiBoard;
	
	//Animating Piece Movement
	GUIMovement lastMove = null;
	PieceAnimation pieceMovement = null;
	
	@Override
	public void init(GameContainer container, StateBasedGame sbg)
			throws SlickException {
		//Setup GameBoard and Images
		if(!Globals.RESOURCES_INITIATED){
			Checkers.initRessources();
		}
		
		this.container = container;
		
		background = new Image("data/images/bg3.jpg");
		
		redPiece = ResourceManager.getImage("red");
		blackPiece = ResourceManager.getImage("black");
		redKing = ResourceManager.getImage("redking");
		blackKing = ResourceManager.getImage("blackking");
		blackSpace = ResourceManager.getImage("blackspace");
		whiteSpace = ResourceManager.getImage("whitespace");
		
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
				"Otherwise, choose 'CANCEL' if you want to try to be a winner!");//TODO: Make better prompt boxes...
		
		newGameBox = new PromptBox("Warning: Starting a new game when a game is currently in progress\n" +
				"will result in a loss.\n" +
				"\n" +
				"Are you sure you want to continue and lose?\n\n" +
				"Just click 'OKAY' to lose and start a new game,\n" +
				"or click 'CANCEL' if you want to try to win.");
		
		//Prompt Boxes
		//TODO: Create method for PromptBox that does this automatically...
		surrenderBox.okayButton = new MouseOverArea(container, ResourceManager.getImage("okay"), 125, 405, 150, 25, this);
		surrenderBox.cancelButton = new MouseOverArea(container, ResourceManager.getImage("cancel"), 525, 405, 150, 25, this);
		newGameBox.okayButton = new MouseOverArea(container, ResourceManager.getImage("okay"), 125, 405, 150, 25, this);
		newGameBox.cancelButton = new MouseOverArea(container, ResourceManager.getImage("cancel"), 525, 405, 150, 25, this);
		
		//End Prompt Boxes
		
		//Setup Menu
		//TODO: Change Start Game -> New Game image		
		menu = new Menu(265,320, 5);
		this.container = container;//Ryan: changed menus.areas[i] = bleh --> menus.setArea(i, object) so that we can add listeners
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
		
		//Turn off Menu
		menu.toggle();
		
		
		//Checkers Board
		//TODO: Code this mother
		backendBoard = new CheckersBoard();
		turn = 1; //Player 1
		AI = new CheckersAI(backendBoard);
		guiBoard = new Integer[8][8];
		
		//Blank GUIBoard
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++)
				guiBoard[i][j] = 0;
		
		//Music Options
		backgroundMusic = ResourceManager.getMusic("normal");
		backgroundMusic.loop();
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
		
				
		//TODO: Andy will make pieces able to move/jump using magic
		//movement.render(arg0, arg2);
		if(pieceMovement != null && pieceMovement.isActivated()){
			pieceMovement.render(container, g);
		}
		
		
		//Render Surrender Button
		surrender.render(container, g);
		
		//Options Menu Stuff
		if(menu.isActivated()){
			g.setColor(new Color(0,0,0,200));
			//g.setColor(Color.black);
			g.fillRect(0, 0, 800, 600);
			menu.render(container, g);
		}
		options.render(container, g);
		stat.render(container, g);
		if(stat.isActivated()){
			showStatistics.render(container, g);
		}
		
		//SurrenderBox Prompt
		surrenderBox.render(container, g);
		
		//NewGame Prompt
		newGameBox.render(container, g);
		
		g.setColor(Color.white);
		String temp =  "";
		temp += totalTurns;
		g.drawString(temp, 110, 200);
		
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
			throws SlickException {
		
		//Check if the game wants to exit
		if(menu.shouldExit){
			container.exit();	
		}
			
		//Check if menu needs to be toggled
		if(toggleMenu){
			toggleMenu = false;
			menu.toggle();
		}
		
		//TODO: Add all the game logic-ness here
		possibleMoves = backendBoard.getPossibleMoves(turn); //Get Possible Moves for Current Player
		
		if(possibleMoves.isEmpty() || gameOver){
			gameOver = true; //No moves, game is over
		}
		else if(pieceMovement == null || !pieceMovement.isActivated()){
			//gets a valid move
			do{
				if( (playerOneAI && turn == 1) || (playerTwoAI && turn == 2) ){
					move = AI.playerAI(turn);
				}
				else{
					//move = moveInput( possibleMoves );
				}
			}
			while( !possibleMoves.contains(move) );
			
			//makes move and promotes pieces
			jumpFrom = backendBoard.move(move);
			backendBoard.promote();
			
			possibleMoves = backendBoard.getJumps(jumpFrom, turn);			
			
			//if the last move was a jump and has another jump, allow the player to make the jump
			while( !possibleMoves.isEmpty() ){
				do{
					if( (playerOneAI && turn == 1) || (playerTwoAI && turn == 2) ){
						
						move = possibleMoves.get(0);
					}
					else{
						//move = moveInput( possibleMoves );
					}
					
				}
				while( !possibleMoves.contains(move) );
				
				//makes the multiple jump
				jumpFrom = backendBoard.move( move );
				backendBoard.promote();
					
				possibleMoves = backendBoard.getJumps( jumpFrom,turn );
				
			}
			
			turn %= 2;
			turn++;
			totalTurns++;
			
			if(totalTurns < 105){
				System.out.println( backendBoard );
				
				lastMove = backendBoard.getLastMove();
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
			else{
				gameOver = true;
			}
			
		}
		
		//Piece Movement Animation
		if(lastMove != null){
			if(lastMove.getJumper() == 0)
				lastMove.setJumper(guiBoard[7-lastMove.getMove().getEnd().getY()][7-lastMove.getMove().getEnd().getX()]);
			
			guiBoard[7-lastMove.getMove().getEnd().getY()][7-lastMove.getMove().getEnd().getX()] = 0; //Blank out			
			pieceMovement = new PieceAnimation(lastMove);
			pieceMovement.toggle();
			lastMove = null;
		}		
	}

	@Override
	public int getID() {
		//Return GameState ID
		return stateID;
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
		System.out.println("On the board, " + x + ", " + y + " has been clicked.");
	}
	
	//TODO: Evaluate whether this is still necessary
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
				//Logic for starting a New Game and losing
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
		if(source == options && !stat.isActivated() && !surrenderBox.isActivated() && !newGameBox.isActivated()) {
			toggleMenu = true;
		}
		
		//Listens for "Show Staistics" (closes Stat Overlay)
		if(source == showStatistics && stat.isActivated()){
			stat.toggle();
		}

		//Being lazy and coding the menu stuff here...
		if(menu.isActivated() && !newGameBox.isActivated()){
			if(!stat.isActivated()){ //TODO: add logic to check other overlays
				if(source == menu.areas[Menu.NEWGAME]){
					//Logic for New Game while playing a Game
					newGameBox.toggle();//Should only toggle if game is not finished
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
					menu.shouldExit = true; //Will redo this
				}
			}
		}
		
	}
	
	//Was gonna have a listener in each square, have that activate the GameScreen so it can handle what's up
	private class SquareListener implements ComponentListener
	{
		private int x;
		private int y;
		private GameScreen game;//Yeah, it's kinda redundant. Feel free to come up with a better solution; this is only to call GameScreen's componentActivated anyways.
		public SquareListener(int x, int y, GameScreen game)
		{
			this.x = x;
			this.y = y;
			this.game = game;
		}

		@Override
		public void componentActivated(AbstractComponent arg0) {
			// TODO Auto-generated method stub
			System.out.println("SquareListener at board x"+x+" y"+y+" activated.");
			//Gamescreen.activate;//Notify GameScreen that this square has been clicked, and let it handle that.
			//Only accept mouse clicks when Menu is not activated
			if(!menu.isActivated() && !surrenderBox.isActivated())
				game.squareClicked(x,y);
		}
		
	}

}
