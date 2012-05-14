package com.sadbagel.checkers.gui;

import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JFileChooser;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.thingle.spi.ThingleException;
import org.newdawn.slick.thingle.util.FileChooser;
import org.newdawn.slick.thingle.util.FileChooserListener;

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
	
	public int haltButtons = 1;

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
		activated = (activated + 1) % 2;
	}

	//TODO: I like this code, but it's easier to code the logic per screen because
	// of the overlays. :( Albeit, I could make all of the overlays and menu static,
	// but I already started doing it the aforementioned way.
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
					//Globals.GAME.enterState(Globals.GAME.GAMESCREENSTATE, new FadeOutTransition(), new FadeInTransition());
				}
				else{
					//GameSreen Case
				}
				break; 
			
			case SAVEGAME: 			
				
			//if(Globals.CURRENTSTATE == Globals.GAME.TITLESCREENSTATE){
			if(Globals.GAME.getCurrentStateID() == Globals.GAME.TITLESCREENSTATE){
				//Can't exactly save during the title screen.
				
			}
			else{
				//Logic for saving the current game.
				
				if(haltButtons != 0){
					File savesDirectory = new File("./saves");
					
					 if( savesDirectory.list() == null ){
					 
					 	savesDirectory.mkdir();
					 }
					
					 Calendar time = Calendar.getInstance();
					 
						FileWriter save = null;
						
						
						File file =  new File("./saves/LastGame.checkers" );
						
						
						try {
							file.createNewFile();
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						
						try {
							save = new FileWriter( file );
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					 
					try {
						save.write( ( (GameScreen) Globals.GAME.getCurrentState() ).toFileString() );
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					try {
						save.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
								
			}break;
			
			case LOADGAME: 
				if(Globals.GAME.getCurrentStateID() == Globals.GAME.TITLESCREENSTATE){
					
				}
				else{
					//Load game from Game Screen
					if(haltButtons != 0){
						File savesDirectory = new File("./saves");
						
						 if( savesDirectory.list() == null ){
						 
						 	savesDirectory.mkdir();
						 }
						
						File file =  new File("./saves/LastGame.checkers" );

						if( file.exists() ){
							
							Scanner scanner = null;
							
							try {
								scanner = new Scanner( file );
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							System.out.println( ( (GameScreen) Globals.GAME.getCurrentState() ).setState( scanner.nextLine() ) );
						}
						
					}
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
					//shouldExit = true;
				}
				break;
			}
		}

	}

}
