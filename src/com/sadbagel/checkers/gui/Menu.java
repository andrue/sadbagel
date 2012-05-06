package com.sadbagel.checkers.gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class Menu{

	//Menu Button Images
	Image buttons[] = null;

	//Menu Button Variables
	static private int NUMBUTTONS = 5;//Felt like the hardcoded 4 was a bit much, y'know?
	static final int NEWGAME = 0;
	static final int SAVEGAME = 1;
	static final int LOADGAME = 2;
	static final int STATISTICS = 3;
	static final int QUITGAME = 4;
	
	//Need close menu and surrender game buttons
	
	//Misc Variables
	public boolean shouldExit = false;

	//Clickable Regions
	public MouseOverArea[] areas = new MouseOverArea[NUMBUTTONS];
	public ComponentListener[] listeners = new ComponentListener[NUMBUTTONS];//can totes be refactored to a better name
	//Menu-Coordinates
	int x, y;
	
	//Activated (used for rendering on gamescreen/deactivating other components)
	private int activated = 1;

	public Menu(int x, int y, int buttons){
		NUMBUTTONS = buttons;
		this.x = x;
		this.y = y;
	}
	
	public Menu(int x, int y)//Default call (ie TitleScreen)
	{
		this(x,y,4);
	}

	public void render(GameContainer container, Graphics g)
			throws SlickException {
		//Renders the menu on the current container
		if(isActivated()){
			for(int i=0;i<NUMBUTTONS;i++){
				areas[i].render(container, g);
			}
		}

	}

	public void update(GameContainer container, int delta)
			throws SlickException {
		//Updates options of the menu
	}



	public void setArea(int i, MouseOverArea mouseOverArea) {
		// TODO Auto-generated method stub
		areas[i]=mouseOverArea;
		listeners[i]=new ButtonListener(i); //Since we're changing the button, we ought to recycle the old listener, too.
		areas[i].addListener(listeners[i]);
	}
	
	public boolean isActivated(){
		return (activated == 1 ? true : false);
	}
	
	/**
	 * Toggles a menu to render
	 */
	public void toggle(){
		activated += 1;
		activated %= 2;
	}



	private class ButtonListener implements ComponentListener
	{
		private int buttonNum = -1; 
		public ButtonListener(int slot)
		{
			buttonNum = slot;	
		}
		public void componentActivated(AbstractComponent arg0) {
			// TODO Auto-generated method stub
			System.out.println(this+" was activated");
			//TODO: Add condition code for the menu options.
			switch(buttonNum)
			{
			case -1: throw new Error("For some god-awful reason, a ButtonListener was either not initialized or initialized to -1."); /*Putting a break here makes it complain about unreachable code*/ 
			case NEWGAME: System.out.println("New game, eh?");
				//Check if TitleScreen in order to use spiffy fades
				if(Globals.CURRENTSTATE == Globals.GAME.TITLESCREENSTATE){
					Globals.GAME.enterState(Globals.GAME.GAMESCREENSTATE, new FadeOutTransition(), new FadeInTransition());
				}
				else{
					//Assumes in a GameState
					//Check if Game is Finished
						//If-Finished, start new game
						//If-Not-Finished, notify user that game will count as loss, ask to continue
						//Invoke StatAPI if start new game, update appropriately.
					Globals.GAME.enterState(Globals.GAME.GAMESCREENSTATE);
				}
				break; 
			
			case SAVEGAME: 				
			if(Globals.CURRENTSTATE == Globals.GAME.TITLESCREENSTATE){
				//Can't exactly save during the title screen.
			}
			else{
				//Logic for saving the current game.
			}break;
			
			case LOADGAME: 
				if(Globals.CURRENTSTATE == Globals.GAME.TITLESCREENSTATE){
					//Load game from Title Screen
				}
				else{
					//Load game from Game Screen
				}
				break;
			case STATISTICS: 
				if(Globals.CURRENTSTATE == Globals.GAME.TITLESCREENSTATE){
					//Show 'stics from Title Screen
				}
				else{
					//Show 'stics from Game Screen
				}
				break;
			case QUITGAME: 
				if(Globals.CURRENTSTATE == Globals.GAME.TITLESCREENSTATE){
					//Code to quit the game...
					//Exiting code is handled by a states update() method
					shouldExit = true;
				}
				else{
					//stupid code for checking if game is over, etc, etc...
					shouldExit = true;
				}
				break;
			}
		}

	}

}
