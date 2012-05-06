package com.sadbagel.checkers.backend;

public enum Checker{
	
	
	ONE("1"),
	TWO("2"),
	ONE_KING("3"),
	TWO_KING("4");
	
	private String description;

	private Checker(String description) {
		this.description = description;
	}
	
	//private Checker( Checker other){
	//	
	//	this.description = other.description;
	//}
	
	public Checker copy(){
		
		if( this.description.equals( ONE.toString()) ){
			return ONE;
		}
		else if( this.description.equals( TWO.toString()) ){
			return TWO;
		}
		else if( this.description.equals( ONE_KING.toString()) ){
			return ONE_KING;
		}
		else if( this.description.equals( TWO_KING.toString()) ){
			return TWO_KING;
		}
		//Checker newChecker = Checker.ONE;
		///newChecker.description = new String(this.description);
		//System.out.println(newChecker);
		return null;
	}
	
	/**
	 * Returns a string representation of the cookie flavor. 
	 */
	public String toString() {
		return this.description;
	}

}
