package com.sadbagel.checkers;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.MouseOverArea;

public class Menu{

	//Menu Button Images
	Image buttons[] = null;
	
	//Menu Button Variables
	static final int NEWGAME = 0;
	static final int SAVEGAME = 1;
	static final int LOADGAME = 2;
	static final int STATISTICS = 3;
	static final int QUITGAME = 4;
	
	
	//Clickable Regions
	public MouseOverArea[] areas = new MouseOverArea[4];
	
	//Menu-Coordinates
	int x, y;
	
	public Menu(int x, int y){
		this.x = x;
		this.y = y;
	}

	public void render(GameContainer container, Graphics g)
			throws SlickException {
		//Renders the menu on the current container
		for(int i=0;i<4;i++){
			areas[i].render(container, g);
		}
	}

	public void update(GameContainer container, int delta)
			throws SlickException {
		//Updates options of the menu
	}
	
}
