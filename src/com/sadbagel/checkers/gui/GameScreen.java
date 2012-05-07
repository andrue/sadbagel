package com.sadbagel.checkers.gui;

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
	StatGUI stat = new StatGUI();
	
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
		options = new MouseOverArea(container, optionsImage, 637, 550, 163, 29, this);
		stat.init();
		
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
		
				
		//TODO: Andy will make pieces able to move/jump using magic
		//movement.render(arg0, arg2);
		
		//Options Menu Stuff
		if(menu.isActivated()){
			g.setColor(new Color(0,0,0,200));
			//g.setColor(Color.black);
			g.fillRect(0, 0, 800, 600);
			menu.render(container, g);
		}
		options.render(container, g);
		
		stat.render(container, g);		
		
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
	//Was lazy, so added the options menu button directly to GameScreen :D
	public void componentActivated(AbstractComponent source) {
		//Check if Options Button is Clicked
		if (source == options) {
			toggleMenu = true;
		}

		//Being lazy and coding the Statistics Button here...
		if(source == menu.areas[Menu.STATISTICS]){
			/*Do something to deactivate the menu-buttons
			  while the statistics-overlay being shown */
			stat.toggle();
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
			if(!menu.isActivated())
				game.squareClicked(x,y);
		}
		
	}

}
