package com.sadbagel.checkers.backend;

public class GUIMovement {
	int jumper;
	int jumpee;
	Move move = null;
	Coordinate jumped = null;
	
	boolean canUse = true;
	
	
	GUIMovement(Move move, Coordinate jumped, int jumper, int jumpee){
		this.jumper = jumper;
		this.jumpee = jumpee;
		this.move = move;
		this.jumped = jumped;
	}
	
	//TODO: Accessors...
	public int getJumper(){
		return jumper;
	}
	
	public int getJumpee(){
		return jumpee;
	}
	
	public void setJumper(int x){
		jumper = x;
	}
	
	public void setJumpee(int x){
		jumpee = x;
	}
	
	public Move getMove(){
		return move;
	}
	
	public Coordinate getCoordinate(){
		return jumped;
	}
	
	public boolean status(){
		return canUse;
	}
	
	public void activate(){
		canUse = true;
	}
	
	public void deactivate(){
		canUse = false;
	}
}
