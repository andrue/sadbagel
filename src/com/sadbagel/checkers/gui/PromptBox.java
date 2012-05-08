package com.sadbagel.checkers.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class PromptBox {
	
	public int activated = 0;
	private String message;
	private String typeMessage = "";
	private int length = 0;
	
	PromptBox(String message){
		this.message = message;
	}
	
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		//Renders the menu on the current container
		if(isActivated()){
			g.setColor(new Color(0,0,0,200));
			g.fillRect(100, 150, 600, 300);
			
			//Write Warning Message all shiney-like
			if(length < message.length()){
				typeMessage = typeMessage + message.charAt(length);
				length++;
			}
			g.setColor(Color.white);
			g.drawString(typeMessage, 110, 200);
		}
		else{
			length = 0;
			typeMessage = "";
		}

	}

	public void update(GameContainer container, int delta)
			throws SlickException {
		//Updates options of the menu
	}
	
	public void toggle(){
		activated = (activated + 1) % 2;
	}
	
	public boolean isActivated(){
		return (activated == 1 ? true : false);
	}
	
}
