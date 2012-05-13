package com.sadbagel.checkers.backend;

public class GUIMovement {
	int jumper;
	int jumpee;
	Move move = null;
	Coordinate jumped = null;
	
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
	
	public Move getMove(){
		return move;
	}
	
	public Coordinate getCoordinate(){
		return jumped;
	}

}
