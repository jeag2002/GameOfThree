package com.gofthree.engine;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.gofthree.engine.GameEngine;

public class GameEngineTest {
	
	
	@Test
	public void GameEngineCalculus(){
		try{
			
			GameEngine gE = new GameEngine();
			gE.setSeed(99);
			int data = gE.operation(0);
			assertEquals("res 33", 33, data);
			
			data = gE.operation(0);
			assertEquals("res 11", 11, data);
			
			data = gE.operation(1);
			assertEquals("res 4", 4, data);
			
			data = gE.operation(-1);
			assertEquals("res 1", 1, data);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
}
