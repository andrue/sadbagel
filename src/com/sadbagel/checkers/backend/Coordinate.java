package com.sadbagel.checkers.backend;

public class Coordinate{
	
	private int x;
	private int y;
	
	public Coordinate(int x, int y){
		
		this.x = x;
		this.y = y;
	}
	
	public int getX(){
		
		return this.x;
	}
	
	public int getY(){
		
		return this.y;
	}
	
	public Coordinate bottomLeft() {
		
		//gets the coordinates of the square  to the bottom left
		return new Coordinate( this.x - 1, this.y + 1);
	}

	
	public Coordinate bottomRight() {
		
		//gets the coordinates of the square  to the bottom right
		return new Coordinate( this.x + 1, this.y + 1);
	}
	
	
	public Coordinate topLeft() {
		
		//gets the coordinates of the square  to the top left
		return new Coordinate( this.x - 1, this.y - 1);
	}

	
	public Coordinate topRight() {
		
		return new Coordinate( this.x + 1, this.y - 1);
	}
	
	
	public boolean equals( Coordinate other){
		
		if( this.x == other.x && this.y == other.y ){
			
			return true;
		}
		
		return false;
	}
	
	public String toString(){
		
		String s = "";
		
		s+= "( " + this.x + ", " + this.y + " )";
		
		return s;
	}
	
}
