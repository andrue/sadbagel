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
	
	//Title Screen-Menu Variables
	static final int NEWGAME = 0;
	static final int LOADGAME = 1;
	static final int STATISTICS = 2;
	static final int QUITGAME = 3;
	
	//Music
	Music backgroundMusic = null;
	
	//Menu
	Menu menu = null;
	
	//Pieces
	Image redPiece = null;
	Image blackPiece = null;
	
	private GameContainer container;
	
	@Override
	public void init(GameContainer container, StateBasedGame sbg)
			throws SlickException {
		
		//Init Resources
		Checkers.initRessources();
		menu = new Menu(0,0);
				
		//Setup Menu
		this.container = container;
		for (int i=0;i<4;i++) {
			switch(i){
				case 0:	menu.areas[i] = new MouseOverArea(this.container, ResourceManager.getImage("startgame"), 265,  320 + (i*50), 270, 42, this);
						break;
				case 1: menu.areas[i] = new MouseOverArea(this.container, ResourceManager.getImage("loadgame"), 265,  320 + (i*50), 270, 42, this);
						break;
				case 2: menu.areas[i] = new MouseOverArea(this.container, ResourceManager.getImage("stat"), 265,  320 + (i*50), 270, 42, this);
						break;
				case 3: menu.areas[i] = new MouseOverArea(this.container, ResourceManager.getImage("quitgame"), 265,  320 + (i*50), 270, 42, this);
						break;				
			}
			menu.areas[i].setNormalColor(new Color(1,1,1,0.8f));
			menu.areas[i].setMouseOverColor(new Color(1,1,1,0.9f));
		}
		
		//Sets up Title Menu Areas, Variables, images, etc.
		background = new Image("data/images/bg3.jpg");
		title = ResourceManager.getImage("title");
		banner = ResourceManager.getImage("banner");
		
		redPiece = ResourceManager.getImage("red");
		blackPiece = ResourceManager.getImage("black");
		
		backgroundMusic = ResourceManager.getMusic("normal");
		backgroundMusic.loop();
		
	}

	
	int animateTitle;
	int bannerY = 0;
	float bannerAlpha = 0.0f;
	
	float bgX = -800.0f;
	float bgY = -200.0f;
	
	@Override
	public void render(GameContainer container, StateBasedGame arg1, Graphics g)
			throws SlickException {
		//Renders the current screen
		
		//Render Background
		background.draw(bgX, bgY);
		bgX += .01f;
		
		//Update background draw
		
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
		
		//Render Menu
		menu.render(container, g);
		
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
			throws SlickException {
		//Updates variables/options based on user-input
	}

	@Override
	public int getID() {
		//Returns the ID of the GameState
		return stateID;
	}

	@Override
	public void componentActivated(AbstractComponent arg0) {
		//Handles the Mouse Interactions
	}
	
}
