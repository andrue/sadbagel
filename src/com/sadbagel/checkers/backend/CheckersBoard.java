package com.sadbagel.checkers.backend;

import java.util.ArrayList;

/**
 * Class Header Goes Here
 */
public class CheckersBoard
{
	private Checker board[ ][ ];
	private final int MAX_ROWS = 8;
	private final int MAX_COLUMNS = 8;
	private GUIMovement lastMove;
	
	/**
	 * 
	 */
	public CheckersBoard( )
	{
		//creates Board
		board = new Checker[ MAX_ROWS ][ MAX_COLUMNS ];
		
		//places player one's pieces
		for( int row = 0 ; row < 3 ; row++ )
		{
			
			for( int column = (row + 1) % 2 ; column < MAX_COLUMNS ; column += 2 )
			{
				board[ row ][ column ] = Checker.ONE;
			}
		}
			
		//places player two's pieces
		for( int row = 5 ; row < MAX_ROWS ; row++ )
		{
			for( int column = (row + 1) % 2 ; column < MAX_COLUMNS ; column += 2 )
			{
				board[ row ][ column ] = Checker.TWO;
			}
		}	
	}
	
	/**
	 * 
	 * @param other
	 */
	public CheckersBoard( CheckersBoard other )
	{
		this.board = new Checker[ MAX_ROWS ][ MAX_COLUMNS ];
		
		for( int row = 0 ; row < MAX_ROWS ; row++ )
	    {
	    	for( int column = ( row + 1 ) % 2 ; column < MAX_COLUMNS ; column += 2 )
	    	{
	    		
	    		if( other.board[ row ][ column ] != null )
	    		{	    		
	    			this.board[ row ][ column ] = other.board[ row ][ column ].copy( );
	    		}
	    		else
	    		{
	    			this.board[ row ][ column ] = null;
	    		}
	    	}
	    }

	}
	
	/**
	 * 
	 */
	public void promote( )
	{
		//promotes player2's pieces
		for( int column = 1 ; column < MAX_COLUMNS ; column += 2 )
		{
			if( board[ 0 ][ column ] != null && board[ 0 ][ column ].toString().equals( Checker.TWO.toString() ) )
			{
				board[ 0 ][ column ] = Checker.TWO_KING;
			}
		}
			
		//promotes player1's pieces
		for( int column = 0 ; column < MAX_COLUMNS ; column += 2 )
		{
			if( board[ 7 ][ column ] != null && board[ 7 ][ column ].toString().equals( Checker.ONE.toString() ) )
			{
				board[ 7 ][ column ] = Checker.ONE_KING;
			}
		}
	}
	
	public GUIMovement getLastMove(){
		if(lastMove != null && lastMove.status()){
			lastMove.jumper = 0;
			lastMove.deactivate();
			return lastMove;
		}
		return null;
	}
	
	/**
	 * 
	 * @param move
	 * @return
	 */
	public Coordinate move( Move move )
	{
		Coordinate jumped = null;
		int jumpedPiece = 0;
		int movePiece = 0;
		
		//makes sure the move is valid
		if( isValidCoordinate( move.getStart( ) ) && 
				isValidCoordinate( move.getEnd( ) ) && 
				!isEmpty( move.getStart( ) ) && 
				isEmpty( move.getEnd( ) ) )
		{
			
			//removes jumped piece
			if( ( jumped = getJumpped( move ) ) != null )
			{	
				if(board[jumped.getY()][jumped.getX()] != null){
					jumpedPiece = Integer.parseInt(board[jumped.getY()][jumped.getX()].toString());
				}
				
				removePiece(jumped);
			}
			
			//moves the piece from its starting location to end location
			board[ move.getEnd( ).getY( ) ][ move.getEnd( ).getX( ) ] =  
					board[ move.getStart( ).getY( ) ][ move.getStart( ).getX( ) ];
			
			movePiece = Integer.parseInt(board[move.getEnd().getY()][move.getEnd().getX()].toString());

			removePiece( move.getStart() );
			//board[ move.getStart( ).getY( ) ][ move.getStart( ).getX( ) ] = null;
		}
		
		lastMove = new GUIMovement(move, jumped, movePiece, jumpedPiece);
		
		if(jumped == null){
			return null;
		}
		
		return move.getEnd( );
	}
	
	/**
	 * 
	 * @param move
	 * @return
	 */
	private Coordinate getJumpped( Move move )
	{
		int jumpedX = 0;
		int jumpedY = 0;
		
		//checks if the move is a jump
		if( move.getStart( ).getX( ) - move.getEnd( ).getX( ) == 2 || move.getStart( ).getX( ) - move.getEnd( ).getX( ) == -2)
		{
			//finds the position of jumped piece
			jumpedX = ( move.getStart( ).getX( ) + move.getEnd( ).getX( ) ) / 2;
			
			jumpedY = ( move.getStart( ).getY( ) + move.getEnd( ).getY( ) ) / 2;
		}
		
		//returns null if move was not a jump
		if( jumpedX == 0 && jumpedY == 0 )
		{
			return null;
		}
		
		return new Coordinate( jumpedX , jumpedY );
	}

	/**
	 * 
	 * @param current
	 * @return
	 */
	public ArrayList< Move > playerTwoJump( Coordinate current )
	{
		ArrayList< Move > jumps = new ArrayList< Move >( );
		
		//checks for player 2 king checker's downward moves
		if( isValidCoordinate( current ) && !isEmpty( current ) && board[ current.getY( ) ][ current.getX( ) ].toString( ).equals( Checker.TWO_KING.toString() ) )
		{
			//Checks if the bottom left square contains an enemy piece
			if( isPlayerOne( current.bottomLeft( ) ) )
			{
				//checks if it is possible to jump over the bottom left enemy
				if( isEmpty( current.bottomLeft( ).bottomLeft( ) ) )
				{
					//adds the possible jump to a list of moves
					jumps.add( new Move( current, current.bottomLeft( ).bottomLeft( ) ) );
				}
			}
		
			//Checks if the bottom right square contains an enemy piece
			if( isPlayerOne( current.bottomRight( ) ) )
			{
				//checks if it is possible to jump over the bottom right enemy
				if( isEmpty( current.bottomRight( ).bottomRight( ) ) )
				{
					//adds the possible jump to a list of moves
					jumps.add( new Move( current , current.bottomRight( ).bottomRight( ) ) );
				}
			}
		}//end king jump checking
		
		//checks for player 2 upward jumps (done by all player 2 pieces)
		if( isPlayerTwo( current ) )
		{
			//Checks if the top left square contains an enemy piece
			if( isPlayerOne( current.topLeft(  ))  )
			{
				//checks if it is possible to jump over the top left enemy
				if( isEmpty( current.topLeft( ).topLeft( ) ) )
				{
					//adds the possible jump to a list of moves
					jumps.add( new Move( current , current.topLeft( ).topLeft( ) ) );
				}
			}
			
			//Checks if the bottom right square contains an enemy piece
			if( isPlayerOne( current.topRight( ) ) )
			{
				//checks if it is possible to jump over the bottom right enemy
				if( isEmpty( current.topRight( ).topRight( ) ) )
				{
					//adds the possible jump to a list of moves
					jumps.add( new Move( current , current.topRight( ).topRight( ) ) );
				}
			}
		}//end normal jump checking
		
		return jumps;
	}
	
	/**
	 * 
	 * @param current
	 * @return
	 */
	public ArrayList< Move > playerOneJump( Coordinate current )
	{
		ArrayList< Move > jumps = new ArrayList< Move >( );
		
		//checks for player 1 downward jumps (done by all player 1 pieces)
		if( isPlayerOne( current ) )
		{
			//Checks if the bottom left square contains an enemy piece
			if( isPlayerTwo( current.bottomLeft( ) ) )
			{
				//checks if it is possible to jump over the bottom left enemy
				if( isEmpty( current.bottomLeft( ).bottomLeft( ) ) )
				{
					//adds the possible jump to a list of moves
					jumps.add( new Move( current , current.bottomLeft( ).bottomLeft( ) ) );
				}
			}
	
			//Checks if the bottom right square contains an enemy piece
			if( isPlayerTwo( current.bottomRight( ) ) )
			{
				//checks if it is possible to jump over the bottom right enemy
				if( isEmpty( current.bottomRight( ).bottomRight( ) ) )
				{
					//adds the possible jump to a list of moves
					jumps.add( new Move(current, current.bottomRight( ).bottomRight( ) ) );
				}
			}
		
			//checks for player 1 king checkers upward moves
			if( isValidCoordinate( current ) && board[ current.getY( ) ][ current.getX( ) ].toString( ).equals( Checker.ONE_KING.toString() ) )
			{	
				//Checks if the top left square contains an enemy piece
				if( isPlayerTwo( current.topLeft( ) ) )
				{	
					//checks if it is possible to jump over the top left enemy
					if( isEmpty( current.topLeft( ).topLeft( ) ) )
					{
						//adds the possible jump to a list of moves
						jumps.add( new Move(current , current.topLeft( ).topLeft( ) ) );
					}
				}
			
				//Checks if the top right square contains an enemy piece
				if( isPlayerTwo( current.topRight( ) ) )
				{	
					//checks if it is possible to jump over the top right enemy
					if( isEmpty( current.topRight( ).topRight( ) ) )
					{
						//adds the possible jump to a list of moves
						jumps.add( new Move( current , current.topRight( ).topRight( ) ) );
					}	
				}
			}//end king jump checking
		
		}//end normal jump checking
		
		return jumps;
	}
	
	/**
	 * 
	 * @param turn
	 * @return
	 */
	public ArrayList< Move > getJumps( int turn )
	{	
		ArrayList< Move > jumps = new ArrayList< Move >( );
		
		//cycles through every row in the checker board
		for( int row = 0 ; row < 8; row++ )
		{	
			//cycles through every other column in the checker board
			for( int column = (row + 1) % 2 ; column < MAX_COLUMNS ; column += 2 )
			{	
				Coordinate current = new Coordinate( row,  column );
				
				//checks for player 1 jumps
				if( turn == 1 )
				{	
					jumps.addAll( playerOneJump( current ) );	
				}
				
				//Checks for player 2 jumps
				if( turn == 2 )
				{	
					jumps.addAll( playerTwoJump( current ) );	
				}
			}					
		}
			
		return jumps;
	}
	
	/**
	 * 
	 * @param current
	 * @return
	 */
	public ArrayList< Move > playerOneMove( Coordinate current )
	{
		ArrayList< Move > moves = new ArrayList< Move >( );
		
		//gets player 1 king moves
		if( isPlayerOne( current ) )
		{
			//checks move to the bottom right of current checker
			if( isEmpty( current.bottomRight( ) ) )
			{
				//adds the possible move to a list of moves
				moves.add( new Move( current , current.bottomRight( ) ) );
			}
			
			//checks move to the bottom left of current checker
			if( isEmpty( current.bottomLeft( ) ) )
			{
				//adds the possible move to a list of moves
				moves.add( new Move( current , current.bottomLeft( ) ) );
			}
		
		
			if( isValidCoordinate( current ) && board[ current.getY( ) ][ current.getX( ) ].toString( ).equals( Checker.ONE_KING.toString() ) )
			{
				//checks move to the top right of current checker
				if( isEmpty( current.topRight( ) )  )
				{
					//adds the possible jump to a list of moves
					moves.add( new Move( current , current.topRight( ) ) );
				}
			
				//checks move to the top left of current checker
				if( isEmpty( current.topLeft()) )
				{
					//adds the possible jump to a list of moves
					moves.add( new Move( current , current.topLeft( ) ) );
				}
			}
		}
		return moves;
	}
	
	/**
	 * 
	 * @param current
	 * @return
	 */
	public ArrayList< Move > playerTwoMove( Coordinate current )
	{
		ArrayList< Move > moves = new ArrayList< Move >( );
		
		
		//gets player 2 normal moves
	if( isPlayerTwo( current) )
	{	
		//checks move to the top right of current checker
		if( isEmpty( current.topRight( ) ) )
		{	
			//adds the possible move to a list of moves
			moves.add( new Move( current , current.topRight( ) ) );
		}
		
		//checks move to the top left of current checker
		if( isEmpty( current.topLeft( ) ) )
		{
			//adds the possible jump to a list of moves
			moves.add( new Move( current, current.topLeft( ) ) );
		}
	
			//gets player 2 king moves
			if( isValidCoordinate( current ) && board[ current.getY( ) ][ current.getX( ) ].toString( ).equals( Checker.TWO_KING.toString() ) )
			{	
				//checks move to the bottom right of current checker
				if( isEmpty( current.bottomRight( ) ) )
				{
					//adds the possible move to a list of moves
					moves.add( new Move( current , current.bottomRight( ) ) );
				}
			
				//checks move to the bottom left of current checker
				if( isEmpty( current.bottomLeft( ) ) )
				{	
					//adds the possible move to a list of moves
					moves.add( new Move( current , current.bottomLeft( ) ) );
				}
		
			}
			
		}
		return moves;
	}
	
	/**
	 * 
	 * @param turn
	 * @return
	 */
	public ArrayList< Move > getMoves( int turn )
	{
		ArrayList< Move > moves = new ArrayList< Move >( );
		
		//cycles through every row in the checker board
		for( int row = 0 ; row < MAX_ROWS ; row++)
		{	
			//cycles through every other column in the checker board
			for( int column = (row + 1) % 2 ; column < MAX_COLUMNS ; column += 2 )
			{	
				Coordinate current = new Coordinate( row , column );
				
				//checks for player 1 jumps
				if( turn == 1 )
				{	
					//gets all possible moves from current space
					moves.addAll( playerOneMove( current ) );	
				}
				
				//Checks for player 2 jumps
				if( turn == 2 )
				{
					//gets all possible moves from current space
					moves.addAll( playerTwoMove( current ) );	
				}
			}					
		}
			
		return moves;
	}
		
	/**
	 * 
	 * @param coord
	 * @return
	 */
	public boolean isPlayerTwo( Coordinate coord )
	{
		//checks if the given coordinate on the board contains a player 2 piece
		if( isValidCoordinate( coord ) && 
				board[ coord.getY( ) ][ coord.getX( ) ] != null && 
				( board[ coord.getY( ) ][ coord.getX( ) ].toString( ).equals( Checker.TWO.toString() ) || 
						board[ coord.getY( ) ][ coord.getX( ) ].toString( ).equals( Checker.TWO_KING.toString() ) ) )
		{	
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param coord
	 * @return
	 */
	public boolean isPlayerOne( Coordinate coord )
	{
		//checks if the given coordinate on the board contains a player 1 piece
		if( isValidCoordinate( coord ) && 
				!isEmpty( coord ) && 
				( board[coord.getY( ) ][ coord.getX( ) ].toString( ).equals( Checker.ONE.toString() ) 
						|| board[ coord.getY( ) ][ coord.getX( ) ].toString( ).equals( Checker.ONE_KING.toString() ) ) )
		{
			return true;
		}
		
		return false;
	}

	/**
	 * 
	 * @param coord
	 * @return
	 */
	public boolean isEmpty(Coordinate coord)
	{
		//checks if the given coordinate on the board is empty
		if( isValidCoordinate( coord ) && board[ coord.getY( ) ][ coord.getX( ) ] == null )
		{
			return true;
		}
		
		return false;
	}

	/**
	 * 
	 * @param coord
	 * @return
	 */
	public boolean isValidCoordinate( Coordinate coord )
	{
		//checks if the coordinate is on the board and a valid position
		if( coord.getY( ) < 0 || 
				coord.getY( ) > 7 || 
				coord.getX( ) < 0 || 
				coord.getX( ) > 7  )
				//( || ( coord.getY( ) + 1 ) % 2 ) != ( coord.getX( ) % 2 ) )
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * 
	 */
	public String toString( )
	{	
		String message = "";
		
		//cycles through every row in the checker board
		for( int row = 0; row < MAX_ROWS; row++ )
		{
			//cycles through every other column in the checker board
			for(int column = (row + 1) % 2; column < MAX_COLUMNS; column += 2)
			{
				if( (row + 1) % 2  == 1)
				{
					message += "-";
				}
				
				if( board[row][column] == null )
				{
					
					message += " ";
				}
				else
				{
					message += board[row][column].toString();
				}
				
				if( (row + 1) % 2  == 0)
				{
					message += "-";
				}
			}
			
			message += "\n";
		}
			
		return message;
	}
	
	/**
	 * 
	 */
	public String toGUI(){
		String message = "";
		
		for( int row = MAX_ROWS-1; row >= 0; row-- )
		{
			for(int column = MAX_COLUMNS-1; column >= 0; column--)
			{				
				if( board[row][column] == null )
				{	
					message += "0";
				}
				else
				{
					message += board[row][column].toString();
				}
			}
		}
			
		return message;
	}
	
	/**
	 * Removes a piece from the board.
	 */
	private void removePiece( Coordinate coordinate )
	{
		board[ coordinate.getY( ) ][ coordinate.getX( ) ] = null;
	}
	
	/**
	 * Unit testing for CheckersBoard Class.
	 * 
	 * @param args command line arguments if there are any
	 */
	public static void main( String[ ] args ) 
	{
		// Creating a new Checkers Board
		CheckersBoard board = new CheckersBoard( );
		
		// Testing toString( )
		System.out.println( "Testing toString( ):");
		System.out.println( board.toString( ) );
		
		// Testing isValidCoordinate()
		System.out.println( "Testing isValidCoordinate( ):");
		System.out.println( "Typical test ( 5 , 6 ): " + board.isValidCoordinate( new Coordinate( 5 , 6 ) ) );
		System.out.println( "Border test ( 0 , 0 ): " + board.isValidCoordinate( new Coordinate( 0 , 0 ) ) );
		System.out.println( "Border test ( 7 , 7 ): " + board.isValidCoordinate( new Coordinate( 7 , 7 ) ) );
		System.out.println( "Border test ( 8 , 7 ): " + board.isValidCoordinate( new Coordinate( 8 , 7 ) ) );
		System.out.println( "Border test ( 7 , 8 ): " + board.isValidCoordinate( new Coordinate( 7 , 8 ) ) );
		System.out.println( "Atypical test ( 5000 , 1 ): " + board.isValidCoordinate( new Coordinate( 5000 , 1 ) ) );
		System.out.println( );
		
		// Testing getMoves( )
		System.out.println( "Testing getMoves( ):");
		System.out.println( "Getting moves for Player 1: " + board.getMoves( 1 ) );
		System.out.println( "Getting moves for Player 2: " + board.getMoves( 2 ) );
		System.out.println( );
		
		// Testing isEmpty( )
		System.out.println( "Testing isEmpty( ):");
		System.out.println( "True case: " + board.isEmpty( new Coordinate( 0 , 0 ) ) );
		System.out.println( "False case: " + board.isEmpty( new Coordinate( 1 , 0 ) ) );
		board.removePiece(new Coordinate( 1 , 0 ) );
		System.out.println( "Atypical case (should be true): " + board.isEmpty( new Coordinate( 1 , 0 ) ) );
		System.out.println( );
		
		//Testing isPlayerOne( ) and isPlayerTwo( )
		System.out.println( "Testing isPlayerOne( ) and isPlayerTwo( ):");
		System.out.println( "Owner of spot ( 2 , 2 ) should be neither (false): " + board.isPlayerOne( new Coordinate( 2 , 2 ) ) );
		System.out.println( "Owner of spot ( 2 , 2 ) should be neither (false): " + board.isPlayerTwo( new Coordinate( 2 , 2 ) ) );
		System.out.println( "Owner of spot ( 2 , 1 ) should be 1 (true): " + board.isPlayerOne( new Coordinate( 2 , 1 ) ) );
		System.out.println( "Owner of spot ( 2 , 1 ) should be 1 (false): " + board.isPlayerTwo( new Coordinate( 2 , 1 ) ) );
		System.out.println( );
		
		// Testing playerOneMove( ) and playerTwoMove( ) and move( )
		System.out.println( "Testing playerOneMove( ) and playerTwoMove( ) and move( ):");
		System.out.println( "Desired: ( 1 , 4 )  Output: " + board.playerTwoMove( new Coordinate( 0 , 5 ) ).get( 0 ).getEnd( ) );
		board.move( new Move( new Coordinate( 0 , 5 ) , new Coordinate( 1 , 4 ) ) );
		System.out.println( "Desired: ( 0 , 3 ) , ( 2 , 3 )  Output: " + board.playerTwoMove( new Coordinate( 1 , 4 ) ).get( 0 ).getEnd( ) 
				+ " " + board.playerTwoMove( new Coordinate( 1 , 4 ) ).get( 1 ).getEnd( ) );
		System.out.println( "Desired: ( 6 , 3 )  Output: " + board.playerOneMove( new Coordinate( 7 , 2 ) ).get( 0 ).getEnd( ) );
		board.move( new Move( new Coordinate( 7 , 2 ) , new Coordinate( 6 , 3 ) ) );
		System.out.println( "Desired: ( 5 , 4 ) , ( 7 , 4 )  Output: " + board.playerOneMove( new Coordinate( 6 , 3 ) ).get( 0 ).getEnd( ) 
				+ " " + board.playerOneMove( new Coordinate( 6 , 3 ) ).get( 1 ).getEnd( ) );
		System.out.println( );
		
		// Testing getJumpped( )
		System.out.println( "Testing getJumpped( ):");
		board.move( new Move( new Coordinate( 2 , 5 ) , new Coordinate( 3 , 4 ) ) );
		System.out.println( "Desired (null): " + board.getJumpped( new Move( new Coordinate( 5 , 2 ) , new Coordinate( 4 , 3 ) ) ) );
		board.move( new Move( new Coordinate( 5 , 2 ) , new Coordinate( 4 , 3 ) ) );
		System.out.println( "Desired ( 4 , 3 ): " + board.getJumpped( new Move( new Coordinate( 3 , 4 ) , new Coordinate( 5 , 2 ) ) ) );
		board.move( new Move( new Coordinate( 3 , 4 ) , new Coordinate( 5 , 2 ) ) );
		System.out.println( "Atypical case 1 (null): " + board.getJumpped( new Move( new Coordinate( 5 , 2 ) , new Coordinate( 0 , 2 ) ) ) );
		System.out.println( "Atypical case 2 (null): " + board.getJumpped( new Move( new Coordinate( 5 , 2 ) , new Coordinate( 5 , 2 ) ) ) );
		System.out.println( "Boundary case (null): " + board.getJumpped( new Move( new Coordinate( 5 , 2 ) , new Coordinate( -125 , 0 ) ) ) );
		System.out.println( );
		
		// Testing getJumps( )
		System.out.println( "Testing getJumps( ):");
		board.move( new Move( new Coordinate( 6 , 3 ) , new Coordinate( 7 , 4 ) ) );
		System.out.println( "Desired: ( 4 , 3 ) , ( 6 , 3 ): " + board.getJumps( 1 ).get( 0 ).getEnd( ) 
				+ board.getJumps( 1 ).get( 1 ).getEnd( ) );
		board.move( new Move( new Coordinate( 7 , 4 ) , new Coordinate( 6 , 3 ) ) );
		System.out.println( "Desired: ( 4 , 3 ): " + board.getJumps( 1 ).get( 0 ).getEnd( ) );
		System.out.println( );
		
		// Testing playerOneJump( ) and playerTwoJump( ) and promote( )
		// Have yet to implement playerOneJump
		System.out.println("Testing playerOneJump( ) and playerTwoJump( ) and promote( ): ");
		System.out.println( "Desired ( 4 , 3 ): " + board.playerOneJump( new Coordinate( 6 , 1 ) ).get( 0 ).getEnd( ) );
		board.move( new Move( new Coordinate( 6 , 1 ) , new Coordinate( 7 , 2 ) ) );
		board.move( new Move( new Coordinate( 7 , 0 ) , new Coordinate( 6 , 1 ) ) );
		board.move( new Move( new Coordinate( 5 , 2 ) , new Coordinate( 7 , 0 ) ) );
		board.promote( );
		
		// Testing if after promotion the piece still belongs to the player two
		System.out.println( "False case: " + board.isPlayerOne( new Coordinate( 7 , 0 ) ) );
		System.out.println( "True case: " + board.isPlayerTwo( new Coordinate( 7 , 0 ) ) );
		// Testing whether king can jump piece landing out of the board
		System.out.println( "Desired: []: " + board.getJumps( 1 ));
		System.out.println( "Desired: []: " + board.getJumps( 2 ));
		// Testing whether the king piece can jump backwards
		board.move( new Move( new Coordinate( 7 , 0 ) , new Coordinate( 6 , 1 ) ) );
		board.move( new Move( new Coordinate( 6 , 1 ) , new Coordinate( 5 , 2 ) ) );
		System.out.println( "Desired ( 7 , 4 ): " + board.playerTwoJump( new Coordinate( 5 , 2 ) ).get( 0 ).getEnd( ) );
		System.out.println( "Desired ( 6 , 3 ): " + board.getJumpped( new Move( new Coordinate( 5 , 2 ) , new Coordinate( 7 , 4 ) ) ) );
		
		CheckersAI AI = new CheckersAI( board );
		System.out.println(AI.playerAI(1));
		System.out.println(board);
		
		// How can i get a checker piece from the board and print out the checker piece enum to make sure it displays 2k?
	}

	public double boardValue(int player) {
		
		double score = 0;
		
			//calcs score for player 1
			score += Math.pow( 1.7, getJumps( 1 ).size() );
			score += Math.pow( 1.35, getMoves( 1 ).size() );
			score += Math.pow( 1.15, numPieces( 1 ) );
			score += Math.pow( 5, numKings( 1 ) );
			
			//calc score for player 2
			score -= Math.pow( 1.7, getJumps( 2 ).size() );
			score -= Math.pow( 1.35, getMoves( 2 ).size() );
			score -= Math.pow( 1.15, numPieces( 2 ) );
			score -= Math.pow( 5, numKings( 2 ) );
		
			if(player == 1){
				
				return score;
			}
		
		
		return -1 * score;
	}

	private int numPieces( int player ) {
		
		int total = 0;
		
		if( player == 1 ){
			
			//counts how many pieces are player ones
			for( int row = 0 ; row < 8 ; row++ )
			{
			
				for( int column = (row + 1) % 2 ; column < MAX_COLUMNS ; column += 2 )
				{
					if( isPlayerOne( new Coordinate( column, row) ) ){
				
						total++;
					}
				}
			}
			
			return total;
		}
		
		//counts how many pieces are player twos
		for( int row = 0 ; row < 8 ; row++ )
		{
			
			for( int column = (row + 1) % 2 ; column < MAX_COLUMNS ; column += 2 )
			{
				if( isPlayerTwo( new Coordinate( column, row) ) ){
				
					total++;
				}
			}
		}
		
		return total;
		
	}
	
	
	private int numKings( int player ) {
		
		int total = 0;
		
		if( player == 1 ){
			
			//counts how many pieces are player ones
			for( int row = 0 ; row < 8 ; row++ )
			{
			
				for( int column = (row + 1) % 2 ; column < MAX_COLUMNS ; column += 2 )
				{
					if( isPlayerOne( new Coordinate( column, row) ) ){
						
						if( board[ row ][ column ].toString().equals(Checker.ONE_KING.toString()) ){
							
							total++;
						}
						
					}
				}
			}
			
			return total;
		}
		
		//counts how many pieces are player twos
		for( int row = 0 ; row < 8 ; row++ )
		{
			
			for( int column = (row + 1) % 2 ; column < MAX_COLUMNS ; column += 2 )
			{
				if( isPlayerTwo( new Coordinate( column, row) ) ){
					
					if( board[ row ][ column ].toString().equals(Checker.TWO_KING.toString()) ){
						
						total++;
					}
					
				}
			}
		}
		
		return total;
		
	}
	
	public ArrayList<Move> getJumps( Coordinate current, int player ){
		
		if( current == null){
			
			return new ArrayList<Move>();
		}
		
		if( player == 1){
			
			return playerOneJump( current );
		}
		
		if( player == 2){
			
			return playerTwoJump( current );
		}
		
		return null;
		
	}
	
	
	public ArrayList<Move> getPossibleMoves( int player ){
		
		ArrayList<Move> Moves;
		
		Moves = getJumps( player );
			
		if( Moves.isEmpty() ){
				
			Moves = getMoves( player );
		}
		
		return Moves;
		
	}
	
}

