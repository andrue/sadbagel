package com.sadbagel.checkers.backend;

import java.util.ArrayList;

public class CheckersAI {

	CheckersBoard board;
	
	public CheckersAI( CheckersBoard board ){
		
		this.board = board;
	}
	
	
	public Move playerAI( int player ){
		
		ArrayList<Move> moves;
		ArrayList<Move> enemyMoves;
		
		CheckersBoard temp = new CheckersBoard( board );
		
		//gets current players possible jumps
		moves = temp.getJumps( player );
		
		//gets current players possible moves if there are no jumps
		if( moves.isEmpty() ){
			
			moves = temp.getMoves( player );
		}
		
		Move bestMove = null;
		double bestScore = Integer.MIN_VALUE;
		double currentScore;
		
		//simulates every possible move for the current player
		for( Move move : moves){
			
			//performs each move
			temp.move( move );
			
			//gets enemy players possible jumps
			enemyMoves = temp.getJumps( ( player % 2) + 1 );

			//gets current players possible moves if there are no jumps
			if( enemyMoves.isEmpty() ){
				
				enemyMoves = temp.getMoves( ( player % 2) + 1 );
			}
			
			
			CheckersBoard temp2 = new CheckersBoard( temp ); 
			
			
			double worstScore = Integer.MAX_VALUE;
			
			
			//simulates every possible move for the current player
			for( Move enemyMove : enemyMoves){
				
				temp2.move( enemyMove );
				
				currentScore = temp2.boardValue( player );
				
				//determines the boards score after the opponent makes the best move
				if( currentScore < worstScore ){
					
					worstScore = currentScore;
				}
				
			}
			
			//determines the best move for the AI, assuming the opponent makes their best move
			if( worstScore > bestScore ){
				
				bestScore = worstScore;
				
				bestMove = move;
			}
			
			
		}
		
		
		
		return bestMove;
		
	}
	
	
	
}
