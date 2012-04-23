package com.sadbagel.checkers;

import java.util.ArrayList;
import java.util.Stack;

public class ourAI {

	private ArrayList<Stack<Piece>> jumpingPieces;
	private ArrayList<Integer> priorityList;
	public ourAI() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * This assumes that the piece class has some coordinates hidden inside it somewhere.
	 * 
	 * primary method takes: copy of board + (indication of which side to evaluate for, or list of our side's pieces(most probably their coordinates)
	 * primary method{
	 * 	1: If necessary, construct list of all "our" pieces.
	 *  2: Iterate through list of pieces, flagging (i.e. tossing into jumpingPieces) all those that can jump an enemy piece.
	 *  3: if jumpingPieces.size > 0 {return canJump(ArrayList <Piece> jumpingPieces);}
	 *  4: if !(jumpingPieces.size > 0) {return boringMove(<whateveryoufeelitneedstotake>);}
	 *  	}
	 *  v--The bit I will be doing.
	 *  v
	 *  canJump(ArrayList<Piece> jumpingPieces){
	 *  	ArrayList<Piece> jumpsAgain
	 *  	for each jumpingPiece{
	 *  		if (someconditional){
	 *  				priority+=x;
	 *  		}
	 *  		if (canjumpagain){
	 *  			jumpsAgain.add(thisPiece)
	 *  		}
	 *  	}
	 *  	if jumpsAgain.size() =/= 0
	 *  		canJump(jumpsAgain);
	 *  	return max(priority)'s index
	 *  }
	 *  
	 *  YOUR BIT:
	 *  boringMove(whateverParameters){
	 *  	for ALL pieces{
	 *  		if (conditional)
	 *  			priority: +/- appropriately (+1 towards ally, +1 towards enemy, +2 save an ally, +4 save allied King -2 endanger(can be jumped) ally, etc.)
	 *  		//Note: Checking for if an ally is endangered may want to be recursive to weight more heavily towards defending from chain jumps.
	 *  		// I will also be using this "will this endanger ally" method, so I'll see if I can scratch one out.
	 *  	}
	 *  	return (index of max priority(the piece to which that priority corresponds should have the same index);
	**/

}
