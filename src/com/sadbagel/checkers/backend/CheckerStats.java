package com.sadbagel.checkers.backend;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.newdawn.slick.muffin.FileMuffin;

import com.sadbagel.checkers.gui.GameScreen;



public class CheckerStats {


	private static double compWins = 0, compLosses= 0, compWinRate= 0, compLossRate= 0, compTotalGames= 0, lowestTurns= 0;
	private static double humWins= 0, humLosses= 0, humWinRate= 0, humLossRate= 0, humTotalGames= 0;
	private static final String fileName = "stats.txt";
	//private ArrayList<SavedGame> savedGames = new ArrayList<SavedGames>();

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

	/**
	 * p1human is going to be true no matter what
	 * p2robot is different because I assumed it meant human before
	 *   but it really means robot
	 *   so that's confusing
	 *   robots are confusing
	 */
	public static void save(int winner, boolean p1human, boolean p2robot, int turnCount)
	{
		System.out.println("Saving" + p2robot);

		File test = new File(fileName);
		FileReader fr = null;
		try {
			fr = new FileReader(test);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//System.out.println("Permissions: read"+test.canRead()+" write"+test.canWrite());
		initFromFile(fr);
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(test));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}






		{
			System.out.println(((winner!=1&&!p1human)||(winner!=2&&p2robot))+"Testing the expression. ");
			String o = "";
			o+=  ((winner==1&&!p1human)||(winner==2&&p2robot)  ?1.:0.)+compWins+"\n";//CWINS
			o+=  ((winner!=1&&!p1human)||(winner!=2&&p2robot)  ?1.:0.)+compLosses+"\n";//CLOSS
			o+= ((p1human==p2robot&&(winner==1&&p1human)||(winner==2&&!p2robot))  ?1.5:0.)+humWins+"\n";//HWINS
			o+= ((p1human==p2robot&&(winner!=1&&p1human)||(winner!=2&&!p2robot))  ?1.:0.)+humLosses+"\n";//HLOSS

			try {
				out.write(o);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*
			hash = new HashMap<String,Double>();
			hash.put(fields.CWINS.name(), (  (winner==1&&!p1human)||(winner==2&&!p2human)  ?1.:0.));
			hash.put(fields.CLOSS.name(), (  (winner!=1&&!p1human)||(winner!=2&&!p2human)  ?1.:0.));
			hash.put(fields.HWINS.name(), (  (p1human!=p2human&&(winner==1&&p1human)||(winner==2&&p2human))  ?1.:0.));
			hash.put(fields.HLOSS.name(), (  (p1human!=p2human&&(winner!=1&&p1human)||(winner!=2&&p2human))  ?1.:0.));
			hash.put(fields.PVPCOUNT.name(), ( p1human==p2human ?1.:0.));
			hash.put(fields.HIGHTURNS.name(), (double)turnCount);
			hash.put(fields.LOWTURNS.name(), (double)turnCount);
			try {
				fm.saveFile(hash, fileName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("Tried saving in !test.exists() in CheckerStats to file "+fileName);
				e.printStackTrace();
			}
			 */
		}
	}

	public static void initFromFile(FileReader fr)
	{
		try {
			//System.out.println(fr.getCanonicalPath());


			System.out.println(fr.ready());
			BufferedReader scan = new BufferedReader(fr);
			//			System.out.println("at least I've opened the scanner"+scan.ready());
			compWins = Double.parseDouble(scan.readLine());
			//	System.out.println("cw"+compWins);
			compLosses = Double.parseDouble(scan.readLine());
			//System.out.println("cl"+compLosses);
			humWins = Double.parseDouble(scan.readLine());
			humLosses = Double.parseDouble(scan.readLine());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (NullPointerException p){
			System.out.println("I GIVE NULL FUCKS");
			compWins = 0.;
			compLosses = 0.;
			humWins = 0.;
			humLosses = 0.;
		} catch (IOException e) {
			System.out.println("IO NO FUCKS");
			e.printStackTrace();
		}

	}

	/**
	 * toHash
	 * @return Hashmap<String,Double> of stats
	 * hashmap is needed for FileMuffin
	 */
	public void fromHash(HashMap<String, Double> b)
	{

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

	public void calcMissingValues()
	{
		compTotalGames = compWins + compLosses;
		compWinRate = compWins / compTotalGames;
		compLossRate = compLosses / compTotalGames;
		humTotalGames = humWins + humLosses;
		humWinRate = humWins / humTotalGames;
		humLossRate = humLosses / humTotalGames;
	}

	public String toString(){
		String result = "";
		try {
			initFromFile(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		calcMissingValues();
		result += "Player: " /*+ this.name*/ + "\n";
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
