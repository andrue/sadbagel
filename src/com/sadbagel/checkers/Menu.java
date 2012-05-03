package com.sadbagel.checkers;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;

public class Menu{

	//Menu Button Images
	Image buttons[] = null;

	//Menu Button Variables
	static private int NUMBUTTONS = 4;//Felt like the hardcoded 4 was a bit much, y'know?
	static final int NEWGAME = 0;
	static final int SAVEGAME = 1;
	static final int LOADGAME = 2;
	static final int STATISTICS = 3;
	static final int QUITGAME = 4;

	//Clickable Regions
	public MouseOverArea[] areas = new MouseOverArea[NUMBUTTONS];
	public ComponentListener[] listeners = new ComponentListener[NUMBUTTONS];//can totes be refactored to a better name
	//Menu-Coordinates
	int x, y;

	public Menu(int x, int y, int buttons){
		NUMBUTTONS = buttons;
		this.x = x;
		this.y = y;

		/*
		//but they're null at this point...
		for(int i=0;i<NUMBUTTONS;i++)
		{
			areas[i].addListener(areasListener[i]);
		}
		 */
	}
	
	public Menu(int x, int y)//Default call (ie TitleScreen)
	{
		this(x,y,4);
	}

	public void render(GameContainer container, Graphics g)
			throws SlickException {
		//Renders the menu on the current container
		for(int i=0;i<NUMBUTTONS;i++){
			areas[i].render(container, g);

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
			switch(buttonNum)
			{
			case -1: throw new Error("For some god-awful reason, a ButtonListener was either not initialized or initialized to -1."); /*Putting a break here makes it complain about unreachable code*/ 
			case NEWGAME: System.out.println("New game, eh?"); Globals.GAME.enterState(Globals.GAME.GAMESCREENSTATE); break; //Check if currently in a game (for statistic system reasons)
			case SAVEGAME: break;
			case LOADGAME: break;
			case STATISTICS: break;
			case QUITGAME: break;
			}
		}

	}

}
