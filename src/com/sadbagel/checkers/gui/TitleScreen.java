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
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class TitleScreen extends BasicGameState implements ComponentListener{
	
	//State ID
	int stateID = -1;
	
	TitleScreen(int stateID){
		this.stateID = stateID;
	}
	
	//Title Screen Images
	Image background = null;
	Image title = null;
	Image banner = null;
	
	//Music
	Music backgroundMusic = null;
	
	//Menu
	Menu menu = null;
	
	//Pieces
	Image redPiece = null;
	Image blackPiece = null;
	
	
	TextField text = null;
	private String message = "Hi Ryan.";
	
	private GameContainer container;
	
	@Override
	public void init(GameContainer container, StateBasedGame sbg)
			throws SlickException {
		
		//Init Resources
		if(!Globals.RESOURCES_INITIATED){
			Checkers.initRessources();
		}
		
		menu = new Menu(265,320, 5);
				
		//Setup Menu
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
			
			if(i == Menu.SAVEGAME){
				menu.areas[i].setNormalColor(new Color(1,1,1,0.3f));
				menu.areas[i].setMouseOverColor(new Color(1,1,1,0.3f));
			}else{
				menu.areas[i].setNormalColor(new Color(1,1,1,0.8f));
				menu.areas[i].setMouseOverColor(new Color(1,1,1,0.9f));
			}				
		}
		
		//Sets up Title Menu Areas, Variables, images, etc.
		background = ResourceManager.getImage("bg3");
		title = ResourceManager.getImage("title");
		banner = ResourceManager.getImage("banner");
		
		redPiece = ResourceManager.getImage("red");
		blackPiece = ResourceManager.getImage("black");
		
		backgroundMusic = ResourceManager.getMusic("normal");
		//backgroundMusic.loop();
		
	}

	
	int animateTitle;
	int bannerY = 0;
	float bannerAlpha = 0.0f;
	
	float bgX = -800.0f;
	float bgY = -200.0f;
	float bgAngle = 0.0f;
	
	@Override
	public void render(GameContainer container, StateBasedGame sbg, Graphics g)
			throws SlickException {
		
		//Render Background
		background.draw(bgX, bgY);
		background.rotate(bgAngle);
		bgAngle += .000025f;
		bgAngle %= 360.0f;
		
		//Render Title
		if(animateTitle == 0){
			if(bannerAlpha < 0.5f){
				banner.draw(0, 90, new Color(1,1,1,bannerAlpha));
				bannerAlpha += 0.02f;
			}
			else{
				banner.draw(0, 90, new Color(1,1,1,0.5f));
				animateTitle = 1;
				bannerAlpha = 0;
			}
		}
		else if(animateTitle == 1){
			banner.draw(0, 90, new Color(1,1,1,0.5f));
			if(bannerAlpha < 1.0f){
				title.draw(50, 100, new Color(1,1,1,bannerAlpha));
				bannerAlpha += 0.025f;
			}
			else{
				title.draw(50, 100, new Color(1,1,1,bannerAlpha));
				animateTitle++;
			}
		}
		else{
			banner.draw(0, 90, new Color(1,1,1,0.5f));
			title.draw(50, 100);
		}
		
		redPiece.draw(750, 400);
		blackPiece.draw(750, 450);
		
		menu.render(container, g);
		
		g.drawString(message, 200, 550);
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame sbg, int delta)
			throws SlickException {
		//Updates variables/options based on user-input
		if(menu.shouldExit){
			container.exit();	
		}
	}

	@Override
	public int getID() {
		//Returns the ID of the GameState
		return stateID;
	}

	@Override
	public void componentActivated(AbstractComponent source) {
		//Handles the Mouse Interactions
	}
	
}
