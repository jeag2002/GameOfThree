package com.gofthree.engine;

import java.util.Random;

/**
 * Internal engine of the game. Manager of the seed and the operations of GameOfThree
 * @author Usuario
 *
 */

public class GameEngine {
	
	private int seed;
	private Random random;
	
	private static final int MAX_SEED = 300;
	private static final int MIN_SEED = 3;
	
	
	
	public GameEngine(){
		seed = 0;
		random = new Random();
	}
	
	/**
	 * Seed Generator (any int number from 3 to 100). Initializer of the Game
	 * @return
	 */
	
	public int generateSeed(){
	    long range = (long)MAX_SEED - (long)MIN_SEED + 1;
	    long fraction = (long)(range * random.nextDouble());
	    seed =  (int)(fraction + MIN_SEED);  
	    return seed;
	}
	
	/**
	 * Set seed value (for testing purposes)
	 * @param _seed
	 */
	
	public void setSeed(int _seed){
		seed = _seed;
	}
	
	/**
	 * Get seed value
	 * @return
	 */
	public int getSeed(){
		return seed;
	}
	
	/**
	 * Mechanical internal of the Game. (sum + seed)/3; being sum (-1,+1,0) 
	 * @param sum
	 * @return
	 */
	
	public int operation(int sum){
		
		int result = 1;
		
		try{
			result = (seed + sum)/3;
			seed = result;
		}catch(Exception e){
			System.out.println("Cannot operate with (" + seed + ") and (" + sum + ") (" + e.getMessage() + ")");
		}
		
		return result;
	}
}
