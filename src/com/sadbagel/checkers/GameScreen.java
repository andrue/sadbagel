package com.sadbagel.checkers;

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
	final int R = 0;
	final int RK = 1;
	final int B = 2;
	final int BK = 3;
	
	
	//Board Configuration
	MouseOverArea board[][] = new MouseOverArea[8][8];//directions are -->,VVV
	int boardX = 60;
	int boardY = 60;
	
	//Probably remove these...
	private Image title;
	private Image banner;

	private Music backgroundMusic;
	private GameContainer container;
	
	//Board Draw Top-Left
	int boardTLX = 0;
	int boardTLY = 0;
	
	
	@Override
	public void init(GameContainer container, StateBasedGame sbg)
			throws SlickException {
		//Setup GameBoard and Images
		if(!Globals.RESOURCES_INITIATED){
			Checkers.initRessources();
		}
		
		this.container = container;
		
		background = new Image("data/images/bg3.jpg");
		title = ResourceManager.getImage("title");
		banner = ResourceManager.getImage("banner");
		
		redPiece = ResourceManager.getImage("red");
		blackPiece = ResourceManager.getImage("black");
		blackSpace = ResourceManager.getImage("blackspace");
		whiteSpace = ResourceManager.getImage("whitespace");
		
		//Init Board
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<8;j++)
			{
				board[i][j]=new MouseOverArea(
						this.container,
							((i+j)%2==0?whiteSpace:blackSpace)/*Alternating white and black squares. Formula is arbitrary.*/,
							boardX+32*i,
							boardY+32*j,
							32,
							32);
				board[i][j].addListener(new SquareListener(i,j,this));
			}
		}
		
		backgroundMusic = ResourceManager.getMusic("normal");
		backgroundMusic.loop();
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2)
			throws SlickException {
		//Render the background, game board, pieces, and menu (if activated)

				background.draw(0, 0);
				for(int i=0;i<8;i++)
				{
					for(int j=0;j<8;j++)
					{
						board[i][j].render(arg0,arg2);
					}
				}
				
		
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
			throws SlickException {
		//Update piece positions based on Back-end
		//Animate the pieces jumping one another
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
		System.out.println("On the board, "+x+", "+y+" has been clicked.");
		board[x][y].setNormalImage(redPiece);//Debug behavior, arbitrary
	}
	
	//TODO: Evaluate whether this is still necessary
	public void componentActivated(AbstractComponent arg0) {
		//Handle the clicking of pieces, and of the menu
		System.out.println("Lol, GameScreen got activated");
		int bx;//Board coordinates of the clicked square
		int by;
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
			game.squareClicked(x,y);
		}
		
	}

}
