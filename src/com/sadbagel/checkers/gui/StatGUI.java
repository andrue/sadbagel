package com.sadbagel.checkers.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class StatGUI {
	
	private int activated = 0;
	
	public void toggle(){
		activated = (activated + 1) % 2;
	}
	
	public boolean isActivated(){
		return (activated == 1 ? true : false);
	}
	
	public void update(GameContainer container, int delta)
			throws SlickException {
		//Updates options of the menu
	}
	
	public void render(GameContainer container, Graphics g) throws SlickException {
		//Renders the menu on the current container
		if(isActivated()){
			g.setColor(new Color(0,0,0,200));
			g.fillRect(0, 0, 800, 600);
			
			
			
		}
	}
	
	

}
