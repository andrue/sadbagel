package com.sadbagel.checkers.backend;


import java.io.*;


public class OneGameStats {
	
	private int turns;
	
	static String saveState = "saveState";
	
	//playersTurn is true during player's turn, false during computer's turn
	//opponentType is true if facing a human, false if facing a computer
	private boolean playersTurn, opponentType;
	
	public OneGameStats(boolean whoStarts, boolean opponent){
		this.turns = 0;
		
		this.playersTurn = whoStarts;
		
		this.opponentType = opponentType;
	}
	
	//switches the current turn
	public void turnPass(){
		this.playersTurn = !playersTurn;	
	}
	
	public int getTurns(){
		return this.turns;
	}
	
	public boolean getType(){
		return this.opponentType;
	}

}
