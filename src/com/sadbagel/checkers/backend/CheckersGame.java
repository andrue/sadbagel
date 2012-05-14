package com.sadbagel.checkers.backend;


//TODO: MOTHERSNIFFING DELETE
import java.util.ArrayList;

public class CheckersGame {

	CheckersBoard board;
	int turn;
	boolean playerOneAI;
	boolean playerTwoAI;
	
	public CheckersGame( boolean playerOneAI, boolean playerTwoAI ){
		
		this.playerOneAI = playerOneAI;
		this.playerTwoAI = playerTwoAI;
		
		this.board = new CheckersBoard();
		this.turn = 1;
	}
	
	public void play(){
		
		boolean gameOver = false;
		
		int totalTurns = 0;
		ArrayList<Move> possibleMoves;
		Move move = null;
		Coordinate jumpFrom;
		CheckersAI AI = new CheckersAI( board );
		
		while( !gameOver ){
			
			//gets possible moves for current player
			possibleMoves = this.board.getPossibleMoves( this.turn );
		
			//makes sure there is a possible move
			if( !possibleMoves.isEmpty() ){
				
				//gets a valid move
				do{
					if( (playerOneAI && turn == 1) || (playerTwoAI && turn == 2) ){
						
						move = AI.playerAI( turn );
					}
					else{
					//move = moveInput( possibleMoves );
					}
					
				}
				while( !possibleMoves.contains(move) );
				
				
				//makes move and promotes pieces
				jumpFrom = board.move( move );
				board.promote();
				
				
				possibleMoves = board.getJumps( jumpFrom,turn );
				
				
				//if the last move was a jump and has another jump, allow the player to make the jump
				while( !possibleMoves.isEmpty() ){
					
					
					do{
						if( (playerOneAI && turn == 1) || (playerTwoAI && turn == 2) ){
							
							move = possibleMoves.get(0);
						}
						else{
							//move = moveInput( possibleMoves );
						}
						
					}
					while( !possibleMoves.contains(move) );
					
					//makes the multiple jump
					jumpFrom = board.move( move );
					board.promote();
						
					possibleMoves = board.getJumps( jumpFrom,turn );
					
				}
				
				
			}
			else{
				
				gameOver = true;
			}
			
			turn %= 2;
			turn++;
			totalTurns++;
			
			if(totalTurns < 105)
			System.out.println( board );
			
			
			
		}
		
		totalTurns--;
		
		System.out.println("WINNER IS: " + turn);
		endGame();
		
	}
	
	/**
	 * endGame
	 * @param
	 * Calls some extra end-game wrap up pingas.
	 */
	private void endGame()
	{
		
	}
	
	public static void main( String[ ] args ){
		
		 CheckersGame game = new CheckersGame(true,true);
		 
		 game.play();
	}
	
}
