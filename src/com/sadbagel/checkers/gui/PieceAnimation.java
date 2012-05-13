package com.sadbagel.checkers.gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.MouseOverArea;

import com.sadbagel.checkers.backend.GUIMovement;

public class PieceAnimation {
	
	int activated = 0;
	
	//Images
	Image jumper = null;
	Image jumpee = null;
	
	float x, y;
	float endX, endY;
	float speed = 1.0f;
	int jX, jY;
	
	public MouseOverArea okayButton = null;
	public MouseOverArea cancelButton = null;
	
	PieceAnimation(GUIMovement move){
		//Setup Piece Images
		
		if(move.getJumper() > 0){
			jumper = ResourceManager.getImage(imageName(move.getJumper()));
		}
		
		if(move.getJumpee() > 0){
			jumpee = ResourceManager.getImage(imageName(move.getJumpee()));
		}
		
		jumper = ResourceManager.getImage("red");
		jumpee = ResourceManager.getImage("red");
		
		//Setup Coordinates
		//draw(boardX + (j*64) + 9, boardY + (i*64) + 9)
		
		System.out.println("Jumper: " + move.getJumper());
		System.out.println("Jumpee: " + move.getJumpee());
		System.out.println("PA: Y:" + move.getMove().getStart().getY());
		System.out.println("PA: X:" + move.getMove().getStart().getX());
		System.out.println("PA: Y:" + move.getMove().getEnd().getY());
		System.out.println("PA: X:" + move.getMove().getEnd().getX());
		
		x = (60 + (7 - move.getMove().getStart().getX())*64 + 9);
		y = (60 + (7 - move.getMove().getStart().getY())*64 + 9);
		endX = (60 + (7 - move.getMove().getEnd().getX())*64 + 9);
		endY = (60 + (7 - move.getMove().getEnd().getY())*64 + 9);
		
		if(move.getJumpee() != 0){
			jX = (60 + (7 - move.getCoordinate().getX())*64 + 9);
			jY = (60 + (7 - move.getCoordinate().getY())*64 + 9);
		}
		
	}

	private String imageName(int x){
		if(x == 1){
			return "red";
		}
		else if(x == 2){
			return "black";
		}
		else if(x == 3){
			return "redking";
		}
		else if(x == 4){
			return "blackking";
		}
		return "red";
	}
	
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		//Renders the menu on the current container
		if(isActivated()){
			if(x != endX){
				if(x > endX){
					x -= speed;
				}
				else{
					x += speed;
				}
			}
			if(y != endY){
				if(y > endY){
					y -= speed;
				}
				else{
					y += speed;
				}
			}

			if(jumpee != null)
				jumpee.draw(jX, jY);
			
			if(jumper != null)
				jumper.draw(x,y);
			
			
			if((Math.floor(x) == endX || Math.ceil(x) == endX) && 
					(Math.floor(y) == endY || Math.ceil(y) == endY)){
				this.toggle();
			}
			
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
