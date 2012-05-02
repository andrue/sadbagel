package com.sadbagel.checkers;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
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
	int board[][] = new int[8][8];
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		//Setup GameBoard and Images
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2)
			throws SlickException {
		//Render the background, game board, pieces, and menu (if activated)
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
	
	@Override
	public void componentActivated(AbstractComponent arg0) {
		//Handle the clicking of pieces, and of the menu
	}

}
