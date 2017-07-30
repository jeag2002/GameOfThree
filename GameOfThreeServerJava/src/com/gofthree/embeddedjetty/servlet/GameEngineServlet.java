package com.gofthree.embeddedjetty.servlet;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

import com.gofthree.engine.GameEngine;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Asynchronous JAX-RS 2.0 Servlet. Server part of AJAX Long polling architechture 
 * Manage all the messages and part of the administration part of the game
 * @author Usuario
 */


@Path("/gengine")
public class GameEngineServlet {
	
	//FLAGS
	//Generate SEED
	private static final String SEED="SEED";
	private static final String NOSEED = "NOSEED";
	
	//Block other incoming players after we get two players.
	private static final String VOID = "VOID";
	
	//Buttons of play
	private static final String PLUS="PLUS";
	private static final String MINUS="MINUS";
	private static final String ZERO="ZERO";
	
	//End of GAME
	private static final String ENDOK="ENDOK";
	
	//Collection of loose incoming connection between server and clients
    final static Map<String, AsyncResponse> waiters = new ConcurrentHashMap<>();
    
    //Concurrent Service. Forces synchronization of outgoing messages to clients
    final static ExecutorService ex = Executors.newSingleThreadExecutor();
    
    //Internal engine of the Game
    final static GameEngine gE = new GameEngine();

    /**
     * Manage incoming messages from clients to server
     * @param asyncResp
     * @param nick
     */

    @Path("/msg/{nick}")
    @GET
    @Produces("text/plain")
    public void manageInputMsg(@Suspended AsyncResponse asyncResp, @PathParam("nick") String nick) {
    	waiters.put(nick, asyncResp);
    }
    
    
    /**
     * Accept Players. Activate initial seed of first player
     * @param asyncResp
     * @param nick
     */
    
    @Path("/{nick}")
    @GET
    @Produces("text/plain")
    public void hangUp(@Suspended AsyncResponse asyncResp, @PathParam("nick") String nick) {
    	
    	String msg="";
    	
    	//need only two players for the game of three
    	if (waiters.size() < 2){
    		waiters.put(nick, asyncResp);
    		System.out.println("Player (" + waiters.size() + ") NICK (" + nick + ")");
    		
    		if (waiters.size()==1){ //--> first player have the job of generate initial SEED
    			msg=SEED;
    			asyncResp.resume(msg);
    		}else{
    			msg=NOSEED;
    			asyncResp.resume(msg);
    		}
    		
    	}else{
    		System.out.println("Don't admit more players");
    		msg=VOID;
    		asyncResp.resume(msg);
    	}
    	
    }
    
    /**
     * Dispatch output messages from server to clients
     * @param nick
     * @param message
     * @return
     */

    @Path("/{nick}")
    @POST
    @Produces("text/plain")
    @Consumes("text/plain")
    public String sendMessage(final @PathParam("nick") String nick, final String message) {
    		
    	String msgInput = message.toUpperCase();
    	
    	//Cannot generate SEED if there is only one player
    	if (msgInput.equalsIgnoreCase(SEED) &&(waiters.size() == 1)){
    		System.out.println("sendMessage (" + nick + ") :: Cannot SEED. Not enough players");
    		return NOSEED;
    		
    	}else{
    		
    		String msg = "";
    		String spoolMsg = "";
    		
    		//SEED Generation. Send to player1 to player2
    		if (msgInput.equalsIgnoreCase(SEED)){
    			int seed = gE.generateSeed();
    			spoolMsg = "nick (" + nick + ") generate next seed (" + seed + "). your turn";
    		
    		//operation +1,0,-1 over seed
    		}else if (msgInput.equalsIgnoreCase(PLUS) || msgInput.equalsIgnoreCase(ZERO) || msgInput.equalsIgnoreCase(MINUS)){
    			
    			int operation = 0;
    			
    			//generate seed. Number between 3 and 300
    			int prevSeed = gE.getSeed();
    			
    			if (msgInput.equalsIgnoreCase(PLUS)){
    				operation = 1;
    			}else if (msgInput.equalsIgnoreCase(MINUS)){
    				operation = -1;
    			}else{
    				operation = 0;
    			}
    			
    			//Send operation
    			int result = gE.operation(operation);
    			
    			if (result <= 1){
    				msg = ENDOK;
    				spoolMsg = "nick (" + nick + ") get Seed (" + prevSeed + ") operate and WIN!";
    			}else{
    				msg = "Step Sended";
    				spoolMsg = "nick (" + nick + ") get Seed (" + prevSeed + ") operate (" + result + ") your turn";
    			}
    			
    		}else{
    			msg = "Message Send";
    		}
    		
    		System.out.println(spoolMsg);
    		
    		final String spoolData = spoolMsg;
    		
    		//dispatch outgoing messages
    		ex.submit(new Runnable() {
	            @Override
	            public void run() {
	                Set<String> nicks = waiters.keySet();
	                for (String n : nicks) {
	                    // Sends message to all, except sender
	                    if (!n.equalsIgnoreCase(nick)){
	                        waiters.get(n).resume(spoolData);
	                    }
	                }
	            }
	        });
    		
    		//if is the end of the game, remove all the information
    		if (msg.equalsIgnoreCase(ENDOK)){
    			System.out.println("End of the game. Remove all the information for a new use");
    			waiters.clear();
    		}
    		
    		return msg;
    	}    
    }
}
