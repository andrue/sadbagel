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
	float speed = 7f;
	int jX, jY;
	
	//NW, NE, SW, SE: 0, 1, 2, 3
	int direction;
	
	public MouseOverArea okayButton = null;
	public MouseOverArea cancelButton = null;
	
	PieceAnimation(GUIMovement move){
		//Setup Piece Images
		if(move.getJumper() == 1){
			jumper = ResourceManager.getImage("red");
		}
		else if(move.getJumper() == 2){
			jumper = ResourceManager.getImage("black");
		}
		else if(move.getJumper() == 3){
			jumper = ResourceManager.getImage("redking");
		}
		else if(move.getJumper() == 4){
			jumper = ResourceManager.getImage("blackking");
		}
		if(move.getJumpee() == 1){
			jumpee = ResourceManager.getImage("red");
		}
		else if(move.getJumpee() == 2){
			jumpee = ResourceManager.getImage("black");
		}
		else if(move.getJumpee() == 3){
			jumpee = ResourceManager.getImage("redking");
		}
		else if(move.getJumpee() == 4){
			jumpee = ResourceManager.getImage("blackking");
		}
		
		//Setup Coordinates
		//draw(boardX + (j*64) + 9, boardY + (i*64) + 9)
		
//		System.out.println("Jumper: " + move.getJumper());
//		System.out.println("Jumpee: " + move.getJumpee());
//		System.out.println("PA: Y:" + move.getMove().getStart().getY());
//		System.out.println("PA: X:" + move.getMove().getStart().getX());
//		System.out.println("PA: Y:" + move.getMove().getEnd().getY());
//		System.out.println("PA: X:" + move.getMove().getEnd().getX());
		
//		x = (60 + (7 - move.getMove().getStart().getX())*64 + 9);
//		y = (60 + (7 - move.getMove().getStart().getY())*64 + 9);
//		endX = (60 + (7 - move.getMove().getEnd().getX())*64 + 9);
//		endY = (60 + (7 - move.getMove().getEnd().getY())*64 + 9);
		
		x = (60 + (7 - move.getMove().getStart().getX())*64 + 8);
		y = (60 + (7 - move.getMove().getStart().getY())*64 + 8);
		endX = (60 + (7 - move.getMove().getEnd().getX())*64 + 8);
		endY = (60 + (7 - move.getMove().getEnd().getY())*64 + 8);
		
		//Determine Direction
		if(x > endX){
			if(y > endY){
				//NW
				direction = 0;
			}
			else{
				//SW
				direction = 2;
			}
		}
		else{
			if(y > endY){
				//NE
				direction = 1;
			}
			else{
				//SE
				direction = 3;
			}
		}
		
		if(move.getJumpee() != 0){
			jX = (60 + (7 - move.getCoordinate().getX())*64 + 8);
			jY = (60 + (7 - move.getCoordinate().getY())*64 + 8);
		}
		
	}
	
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		//Renders the menu on the current container
		if(isActivated()){
			if(direction == 0){
				x -= speed;
				y -= speed;
				if(x < endX)
					x = endX;
				if(y < endY)
					y = endY;
			}
			else if(direction == 1){
				x += speed;
				y -= speed;
				if(x > endX)
					x = endX;
				if(y < endY)
					y = endY;
			}
			else if(direction == 2){
				x -= speed;
				y += speed;
				if(x < endX)
					x = endX;
				if(y > endY)
					y = endY;
			}
			else if(direction == 3){
				x += speed;
				y += speed;
				if(x > endX)
					x = endX;
				if(y > endY)
					y = endY;
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
