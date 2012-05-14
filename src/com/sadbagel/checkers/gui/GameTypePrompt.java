package com.sadbagel.checkers.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.MouseOverArea;

public class GameTypePrompt {
	
	public int activated = 0;
	private String message;
	private String typeMessage = "";
	private int length = 0;
	
	//GameType Buttons
	public MouseOverArea pvpButton = null; //Player vs Player
	public MouseOverArea pvaiButton = null; //Player vs AI
	
	GameTypePrompt(){
		message = "Please Select a Game Type.\n" +
				"Choose '1 Player' if you want to play against the AI.\n" +
				"Or if you're cool and have friends, choose '2 Player'\n" +
				"to challenge them (to the death.)";
	}
	
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		//Renders the menu on the current container
		if(isActivated()){
			g.setColor(new Color(0,0,0,200));
			g.fillRect(100, 150, 600, 300);
			
			if(length < message.length()){
				typeMessage = typeMessage + message.charAt(length);
				length++;
			}
			g.setColor(Color.white);
			g.drawString(typeMessage, 110, 200);
			
			if(pvpButton != null && pvaiButton != null){
				pvpButton.render(container, g);
				pvaiButton.render(container, g);	
			}
			
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
