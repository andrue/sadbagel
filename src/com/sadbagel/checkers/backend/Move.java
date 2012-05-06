package com.sadbagel.checkers.backend;

public class Move {

	private Coordinate start;
	private Coordinate end;
	
	public Move(Coordinate start, Coordinate end){
		
		//stores the move coordinates passed in
		this.start = start;
		this.end = end;
	}
	
	
	public Coordinate getStart(){
		
		return this.start;
	}
	
	
	public Coordinate getEnd(){
		
		return this.end;
	}
	
	public boolean equals(Move other){
		
		if(other.start.equals(this.start) && other.end.equals(this.end)){
			
			return true;
		}
		
		return false;
	}
	
	public boolean equals( Object other ){
		
		Move other2 = (Move) other;
		
		if( other2.start.equals(this.start) && other2.end.equals(this.end)){
			
			return true;
		}
		
		return false;
	}
	
	public String getMove(){
		
		//returns the stored move
		return "" + this.start.getY() + this.start.getX() + this.end.getX() + this.end.getY();
	}
	
	public String toString(){
		String s = "";
		
		s+= this.start.toString() + " " + this.end.toString();
		
		return s;
	}
	
}
