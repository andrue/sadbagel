

import java.util.ArrayList;

public class CheckerStats {
	
	private double compWins, compLosses, compWinRate, compLossRate, compTotalGames, lowestTurns;
	private double humWins, humLosses, humWinRate, humLossRate, humTotalGames;
	
	private ArrayList<SavedGame> savedGames = new ArrayList<SavedGames>();
	
	String name;
	
	public CheckerStats(String name){
		this.compWins = 0;
		this.compLosses = 0;
		this.compTotalGames = 0;
		
		this.humWins = 0;
		this.humLosses = 0;
		this.humTotalGames = 0;
		
		this.lowestTurns = Integer.MAX_VALUE;
		
		this.name = name;
	}
	
	//update the wins, losses, ratios
	public void gameOver(Boolean outcome, OneGameStats game){
		
		if(game.getType()){
			if(outcome){
				this.humWins++;
			}
			else{
				this.humLosses++;
			}
			
			this.humTotalGames++;
			
			this.humWinRate = humWins / humTotalGames;
			this.humLossRate = humLosses / humTotalGames;
		}
		else{
			if(outcome){
				this.compWins++;
			}
			else{
				this.compLosses++;
			}
			
			this.compTotalGames++;
			
			this.compWinRate = compWins / compTotalGames;
			this.compLossRate = compLosses / compTotalGames;
		}
		
		if(this.lowestTurns < game.getTurns()){
			this.lowestTurns = game.getTurns();
		}
	}

	public String toString(){
		String result = "";
		
		result += "Player: " + this.name + "\n";
		result += "vs COMPUTER: \n";
		result += "Games: " + this.compTotalGames + "\n";
		result += "Win %: " + this.compWinRate + "\n";
		result += "Loss %: " + this.compLossRate + "\n";
		result += "vs HUMAN: \n";
		result += "Games: "+ this.humTotalGames + "\n";
		result += "Win %: " + this.humWinRate + "\n";
		result += "Loss %: " + this.humLossRate + "\n";
		
		return result;
	}
}
